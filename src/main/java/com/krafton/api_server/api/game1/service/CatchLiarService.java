package com.krafton.api_server.api.game1.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.game1.domain.CatchLiarGame;
import com.krafton.api_server.api.game1.domain.CatchLiarKeyword;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import com.krafton.api_server.api.game1.dto.CatchLiarInfoListResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarInfoResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarResultResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarVoteCandidatesResponseDto;
import com.krafton.api_server.api.game1.repository.CatchLiarKeywordRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarGameRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarUserRepository;
import com.krafton.api_server.api.photo.domain.AwsS3;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.krafton.api_server.api.game1.dto.CatchLiarRequest.*;
import static com.krafton.api_server.api.game1.dto.CatchLiarRequest.CatchLiarInfoRequestDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CatchLiarService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final CatchLiarGameRepository catchLiarGameRepository;
    private final CatchLiarKeywordRepository catchLiarKeywordRepository;
    private final CatchLiarUserRepository catchLiarUserRepository;

    public Long catchLiarStart(CatchLiarStartRequestDto request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(NoSuchElementException::new);

        if (room.getParticipants().isEmpty()) {
            throw new NoSuchElementException();  //todo custom exception 제작
        }
        Random random = new Random();
        int randomIndex = random.nextInt(room.getParticipants().size());

        CatchLiarKeyword keyword = catchLiarKeywordRepository.findRandomCatchLiarKeyword();

        CatchLiarGame game = CatchLiarGame.builder()
                .round(1)
                .build();
        CatchLiarGame startedGame = catchLiarGameRepository.save(game);

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < room.getParticipants().size(); i++) {
            numbers.add(i + 1);
        }
//        Collections.shuffle(numbers);
        String[] colors = {"red", "green", "blue", "purple"};

        for (int i = 0; i < room.getParticipants().size(); i++) {
            User participant = room.getParticipants().get(i);

            CatchLiarUser user = CatchLiarUser.builder()
                    .userId(participant.getId())
                    .isLiar(i == 2)
                    .keyword(i == 2 ? keyword.getLiarKeyword() : keyword.getKeyword())
                    .drawOrder(numbers.get(i))
                    .userColor(colors[i])
                    .build();
            catchLiarUserRepository.save(user);

            game.addUsers(user);
        }

        return startedGame.getId();
    }

    public CatchLiarInfoResponseDto catchLiarInfo(CatchLiarInfoRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);
        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser matchingUser = catchLiarUsers.stream()
                .filter(user -> user.getUserId().equals(request.getUserId()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        CatchLiarUser thisTurnUser = catchLiarUsers.stream()
                .filter(user -> user.getDrawOrder().equals(request.getRound()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(thisTurnUser.getUserId())
                .orElseThrow(IllegalArgumentException::new);

        return CatchLiarInfoResponseDto.from(matchingUser, request.getRound(), catchLiarUsers.size(), user);
    }

    public List<CatchLiarInfoListResponseDto> catchLiarInfoList(Long gameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(gameId)
                .orElseThrow(NoSuchElementException::new);
        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        List<CatchLiarInfoListResponseDto> response = catchLiarUsers.stream()
                .map((CatchLiarInfoListResponseDto::from))
                .toList();
        return response;
    }


    public List<CatchLiarVoteCandidatesResponseDto> catchLiarVoteCandidates(Long catchLiarGameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(catchLiarGameId)
                .orElseThrow(NoSuchElementException::new);
        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        List<CatchLiarVoteCandidatesResponseDto> response = catchLiarUsers.stream()
                .map((CatchLiarVoteCandidatesResponseDto::from))
                .collect(Collectors.toList());
        return response;
    }

    public void catchLiarVote(CatchLiarVoteRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser matchingUser = catchLiarUsers.stream()
                .filter(user -> user.getUserId().equals(request.getVotingUserId()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        matchingUser.updateVotedCount();
    }

    public List<CatchLiarResultResponseDto> catchLiarResult(CatchLiarResultRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser userWithMaxVotedCount = catchLiarUsers.stream()
                .max(Comparator.comparingInt(CatchLiarUser::getVotedCount))
                .orElseThrow(NoSuchElementException::new);
        CatchLiarUser liarUser = catchLiarUsers.stream()
                .filter(CatchLiarUser::isLiar)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        boolean gameResult = userWithMaxVotedCount.equals(liarUser);

        List<CatchLiarResultResponseDto> response = new ArrayList<>();
        for (CatchLiarUser catchLiarUser : catchLiarUsers) {
            User user = userRepository.findById(catchLiarUser.getUserId())
                    .orElseThrow(IllegalArgumentException::new);
            if (gameResult && !catchLiarUser.getIsLiar()) {
                catchLiarUser.updateResult(true);
            } else if ( !gameResult && catchLiarUser.getIsLiar() ) {
                catchLiarUser.updateResult(true);
            } else {
                catchLiarUser.updateResult(false);
            }

            CatchLiarResultResponseDto res = CatchLiarResultResponseDto.from(catchLiarUser, user);
            response.add(res);
        }

        return response;
    }

    public HashMap<String, String> catchLiarImgS3upload(Long userId, Long gameId, MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        String objectName = randomFileName(file, "catchLiar");

        // put s3
        amazonS3.putObject(new PutObjectRequest(bucket, objectName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        String path = amazonS3.getUrl(bucket, objectName).toString();
        file.delete();

        CatchLiarGame game = catchLiarGameRepository.findById(gameId)
                .orElseThrow(NoSuchElementException::new);
        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser matchingUser = catchLiarUsers.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        matchingUser.uploadImageS3(objectName, path);

        HashMap<String, String> response = new HashMap<>();
        response.put("imageKey", matchingUser.getImageKey());
        response.put("imagePath", matchingUser.getImagePath());
        return response;
    }
    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile){
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        try {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(file);
            }
        } catch (IOException e) {
            if (file.exists()) {
                file.delete();
            }
            throw new RuntimeException("Failed to convert MultipartFile to File", e);
        }
        return Optional.empty();
    }

    private String randomFileName(File file, String dirName) {
        return dirName + "/" + UUID.randomUUID() + file.getName();
    }

    public void catchLiarImgS3Remove(AwsS3 awsS3) {
        if (!amazonS3.doesObjectExist(bucket, awsS3.getKey())) {
            throw new AmazonS3Exception("Object " +awsS3.getKey()+ " does not exist!");
        }
        amazonS3.deleteObject(bucket, awsS3.getKey());
    }


}

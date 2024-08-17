package com.krafton.api_server.api.game1.service;


import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.external.service.AwsS3Service;
import com.krafton.api_server.api.external.vo.AwsS3;
import com.krafton.api_server.api.game1.domain.CatchLiarGame;
import com.krafton.api_server.api.game1.domain.CatchLiarKeyword;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import com.krafton.api_server.api.game1.dto.response.*;
import com.krafton.api_server.api.game1.repository.CatchLiarGameRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarKeywordRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarUserRepository;
import com.krafton.api_server.api.game1.vo.CatchLiarColor;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatchLiarService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final CatchLiarGameRepository catchLiarGameRepository;
    private final CatchLiarKeywordRepository catchLiarKeywordRepository;
    private final CatchLiarUserRepository catchLiarUserRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public Long catchLiarStart(CatchLiarStartRequestDto request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(NoSuchElementException::new);
        if (room.getParticipants().isEmpty()) throw new NoSuchElementException();  //todo custom exception 제작

        CatchLiarKeyword keyword = catchLiarKeywordRepository.findRandomCatchLiarKeyword();
        CatchLiarGame game = CatchLiarGame.builder()
                                    .round(1)
                                    .catchLiarKeyword(keyword)
                                    .build();
        CatchLiarGame startedGame = catchLiarGameRepository.save(game);

        int randomLiarIndex = getRandomLiarIndex(room);
        List<Integer> orders = randomizeOrder(room);
        CatchLiarColor[] colors = CatchLiarColor.values();
        for (int i = 0; i < room.getParticipants().size(); i++) {
            User participant = room.getParticipants().get(i);

            CatchLiarUser user = CatchLiarUser.builder()
                                    .userId(participant.getId())
                                    .isLiar(i == randomLiarIndex)
                                    .keyword(i == randomLiarIndex ? keyword.getLiarKeyword() : keyword.getKeyword())
                                    .drawOrder(orders.get(i))
                                    .userColor(colors[i].toString())
                                    .build();
            catchLiarUserRepository.save(user);

            game.addUsers(user);
        }

        return startedGame.getId();
    }

    private int getRandomLiarIndex(Room room) {
        Random random = new Random();
        return random.nextInt(room.getParticipants().size());
    }

    private List<Integer> randomizeOrder(Room room) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < room.getParticipants().size(); i++) {
            numbers.add(i + 1);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    @Transactional(readOnly = true)
    public CatchLiarInfoResponseDto catchLiarInfo(CatchLiarInfoRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                                    .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser matchingUser = catchLiarUsers.stream()
                                        .filter(user -> user.getUserId().equals(request.getUserId()))
                                        .findFirst()
                                        .orElseThrow(NoSuchElementException::new);

        CatchLiarUser thisTurnCatchLiarUser = catchLiarUsers.stream()
                                                .filter(user -> user.getDrawOrder().equals(request.getRound()))
                                                .findFirst()
                                                .orElseThrow(NoSuchElementException::new);
        User thisTurnUser = userRepository.findById(thisTurnCatchLiarUser.getUserId())
                                .orElseThrow(IllegalArgumentException::new);

        return CatchLiarInfoResponseDto.from(matchingUser, request.getRound(), catchLiarUsers.size(), thisTurnUser);
    }

    @Transactional(readOnly = true)
    public List<CatchLiarInfoListResponseDto> catchLiarInfoList(Long gameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(gameId)
                                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        return catchLiarUsers.stream()
                        .map((CatchLiarInfoListResponseDto::from))
                        .toList();
    }

    @Transactional(readOnly = true)
    public List<CatchLiarVoteCandidatesResponseDto> catchLiarVoteCandidates(Long catchLiarGameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(catchLiarGameId)
                                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        return catchLiarUsers.stream()
                        .map((CatchLiarVoteCandidatesResponseDto::from))
                        .collect(Collectors.toList());
    }

    public synchronized void catchLiarVote(CatchLiarVoteRequestDto request) {
        CatchLiarUser matchingUser = catchLiarUserRepository.findByIdAndCatchLiarGameId(request.getVotingUserId(), request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);
        matchingUser.updateVotedCount();
        catchLiarUserRepository.save(matchingUser);
    }

    @Transactional
    public void catchLiarVotePessimistic(CatchLiarVoteRequestDto request) {
        CatchLiarUser matchingUser = catchLiarUserRepository.findByIdAndCatchLiarGameIdPessimistic(request.getVotingUserId(), request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);
        matchingUser.updateVotedCount();
    }

    @Transactional
    public void catchLiarVoteOptimistic(CatchLiarVoteRequestDto request) {
        CatchLiarUser matchingUser = catchLiarUserRepository.findByIdAndCatchLiarGameIdOptimistic(request.getVotingUserId(), request.getCatchLiarGameId())
                .orElseThrow(NoSuchElementException::new);

        matchingUser.updateVotedCount();
        catchLiarUserRepository.save(matchingUser);
    }

    @Transactional
    public List<CatchLiarResultResponseDto> catchLiarResult(CatchLiarResultRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser maxVotedUser = catchLiarUsers.stream()
                                                .max(Comparator.comparingInt(CatchLiarUser::getVotedCount))
                                                .orElseThrow(NoSuchElementException::new);
        CatchLiarUser liarUser = catchLiarUsers.stream()
                                        .filter(CatchLiarUser::isLiar)
                                        .findFirst()
                                        .orElseThrow(NoSuchElementException::new);

        boolean gameResult = maxVotedUser.equals(liarUser);

        List<CatchLiarResultResponseDto> response = new ArrayList<>();
        for (CatchLiarUser catchLiarUser : catchLiarUsers) {
            User user = userRepository.findById(catchLiarUser.getUserId())
                            .orElseThrow(NoSuchElementException::new);

            boolean isSuccessfulCatch = (gameResult && !catchLiarUser.getIsLiar()) || (!gameResult && catchLiarUser.getIsLiar());
            catchLiarUser.updateResult(isSuccessfulCatch);

            response.add(CatchLiarResultResponseDto.from(catchLiarUser, user));
        }

        return response;
    }

    @Transactional
    public CatchLiarImageResponseDto catchLiarImgS3upload(CatchLiarImageUploadRequestDto request) {
        CatchLiarGame game = catchLiarGameRepository.findById(request.getCatchLiarGameId())
                                .orElseThrow(NoSuchElementException::new);

        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        CatchLiarUser matchingUser = catchLiarUsers.stream()
                                        .filter(user -> user.getUserId().equals(request.getUserId()))
                                        .findFirst()
                                        .orElseThrow(NoSuchElementException::new);

        AwsS3 canvasImage = awsS3Service.uploadImageToS3(request.getFile(), "catchLiar");
        matchingUser.updateUploadImageS3(canvasImage);

        return CatchLiarImageResponseDto.from(matchingUser);
    }


    @Transactional
    public void catchLiarImgS3Remove(CatchLiarRemoveRequestDto request) {
        awsS3Service.removeImageFromS3(request.getImageKey());
    }


    @Transactional(readOnly = true)
    public CatchLiarKeywordResponse catchLiarKeyword(Long gameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(gameId)
                                .orElseThrow(NoSuchElementException::new);
        CatchLiarKeyword catchLiarKeyword = game.getCatchLiarKeyword();
        return CatchLiarKeywordResponse.from(catchLiarKeyword);
    }
}

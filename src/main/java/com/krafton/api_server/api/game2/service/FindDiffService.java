package com.krafton.api_server.api.game2.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.krafton.api_server.api.game2.domain.FindDiffGame;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import com.krafton.api_server.api.game2.repository.FindDiffGameRepository;
import com.krafton.api_server.api.game2.repository.FindDiffUserRepository;
import com.krafton.api_server.api.photo.service.GenerateService;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
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

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.FindDiffGeneratedImageResponseDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FindDiffService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final RoomRepository roomRepository;
    private final FindDiffGameRepository findDiffGameRepository;
    private final FindDiffUserRepository findDiffUserRepository;
    private final AmazonS3 amazonS3;
    private final GenerateService generateService;

    public Long startFindDiff(FindDiffRequestDto request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + request.getRoomId()));

        List<FindDiffUser> findDiffUsers = room.getParticipants().stream()
                .map(user -> FindDiffUser.builder()
                        .userId(user.getId())
                        .nickname(user.getUsername())
                        .build()
                ).toList();
        findDiffUserRepository.saveAll(findDiffUsers);

        FindDiffGame game = FindDiffGame.builder()
                .users(findDiffUsers)
                .build();
        findDiffUsers.forEach(user -> user.setGame(game));
        FindDiffGame savedGame = findDiffGameRepository.save(game);

        return savedGame.getId();
    }

    public void callUploadImage(FindDiffImageUploadRequestDto request) throws IOException {
        FindDiffUser user = findDiffUserRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("ID: " + request.getUserId() + "에 해당하는 FindDiffUser를 찾을 수 없습니다"));

        // s3 file upload
        File originalFile = convertMultipartFileToFile(request.getOriginalImage())
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
        String originalObjectName = randomFileName(originalFile, "findDiff_original");
        amazonS3.putObject(new PutObjectRequest(bucket, originalObjectName, originalFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String originalPath = amazonS3.getUrl(bucket, originalObjectName).toString();
        originalFile.delete();

        File maskingFile = convertMultipartFileToFile(request.getMaskingImage())
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
        String maskingObjectName = randomFileName(maskingFile, "findDiff_masking");
        amazonS3.putObject(new PutObjectRequest(bucket, maskingObjectName, maskingFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String maskingPath = amazonS3.getUrl(bucket, maskingObjectName).toString();
        maskingFile.delete();

        user.uploadOriginalImage(originalObjectName, originalPath, maskingObjectName, maskingPath);

        saveGeneratedImage(user, request);
    }

//    @Async
//    @Transactional(Require_new)
    public void saveGeneratedImage(FindDiffUser user, FindDiffImageUploadRequestDto request) throws IOException {
        MultipartFile processedImage = generateService.processImage(
                request.getOriginalImage(),
                request.getMaskX1(),
                request.getMaskY1(),
                request.getMaskX2(),
                request.getMaskY2()
        );

        // s3 file upload
        File file = convertMultipartFileToFile(processedImage)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
        String objectName = randomFileName(file, "findDiff_generated_ai");

        // put s3
        amazonS3.putObject(new PutObjectRequest(bucket, objectName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        String path = amazonS3.getUrl(bucket, objectName).toString();
        file.delete();

        user.uploadAiGeneratedImage(objectName, path, request.getMaskX1(), request.getMaskY1(),request.getMaskX2(), request.getMaskY2());
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

    @Transactional(readOnly = true)
    public List<String> getOriginalImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        List<FindDiffUser> users = game.getUsers();
        for (FindDiffUser user : users) {
            log.warn("박현민 : {}", user.toString());
        }

        return game.getUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(FindDiffUser::getOriginalImageUrl)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FindDiffGeneratedImageResponseDto> getGeneratedImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        List<FindDiffGeneratedImageResponseDto> images = game.getUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(FindDiffGeneratedImageResponseDto::from)
                .toList();

        return images;
    }

    public List<FindDiffResultDto> callFindDiffResultList(Long gameId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        List<FindDiffResultDto> images = game.getUsers().stream()
                .map(FindDiffResultDto::from)
                .toList();
        return images;
    }

    public void updateChance(FindDiffChanceRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.updateScore(request.getChance() * 10);
    }

    public void updateCorrect(FindDiffCorrectRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.updateScore(request.getCorrect() * 100);
    }

    public List<FindDiffScoreDto> getLeaderboard(Long gameId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        return game.getUsers().stream()
                .map(FindDiffScoreDto::from)
                .toList();

    }
}

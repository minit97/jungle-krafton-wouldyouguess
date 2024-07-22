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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindDiffService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final RoomRepository roomRepository;
    private final FindDiffGameRepository findDiffGameRepository;
    private final FindDiffUserRepository findDiffUserRepository;
    private final GenerateService generateService;
    private final AmazonS3 amazonS3;

    @Transactional
    public Long startFindDiff(FindDiffRequestDto request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + request.getRoomId()));

        List<FindDiffUser> findDiffUsers = room.getParticipants().stream()
                                                .map(user -> FindDiffUser.builder()
                                                                .userId(user.getId())
                                                                .nickname(user.getUsername())
                                                                .build())
                                                .toList();

        FindDiffGame game = FindDiffGame.builder()
                                .users(new ArrayList<>()).build();
        findDiffUsers.forEach(game::addUser);
        FindDiffGame savedGame = findDiffGameRepository.save(game);

        return savedGame.getId();
    }

    @Transactional
    public FindDiffAiGeneratedImageResponseDto callUploadImage(FindDiffImageUploadRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByGameIdAndUserId(request.getGameId(), request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("ID: " + request.getUserId() + "에 해당하는 FindDiffUser를 찾을 수 없습니다"));

        uploadS3Image(user, request.getOriginalImage(), request.getMaskingImage());
        saveGeneratedImage(user, request);

        return FindDiffAiGeneratedImageResponseDto.from(user);
    }

    @Async
    public void uploadS3Image(FindDiffUser user, MultipartFile originalImage, MultipartFile maskingImage) {
        // s3 file upload
        File originalFile = convertMultipartFileToFile(originalImage)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile originalImage -> File convert fail"));
        String originalObjectName = randomFileName(originalFile, "findDiff_original");
        amazonS3.putObject(new PutObjectRequest(bucket, originalObjectName, originalFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String originalPath = amazonS3.getUrl(bucket, originalObjectName).toString();
        originalFile.delete();

        File maskingFile = convertMultipartFileToFile(maskingImage)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile maskingImage -> File convert fail"));
        String maskingObjectName = randomFileName(maskingFile, "findDiff_masking");
        amazonS3.putObject(new PutObjectRequest(bucket, maskingObjectName, maskingFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String maskingPath = amazonS3.getUrl(bucket, maskingObjectName).toString();
        maskingFile.delete();

        uploadS3ImageUrlUpdate(user, originalObjectName, originalPath, maskingObjectName, maskingPath);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void uploadS3ImageUrlUpdate(FindDiffUser user, String originalObjectName, String originalPath, String maskingObjectName, String maskingPath) {
        user.uploadOriginalImage(originalObjectName, originalPath, maskingObjectName, maskingPath);
    }


    public void saveGeneratedImage(FindDiffUser user, FindDiffImageUploadRequestDto request) {
        MultipartFile processedImage = null;
        try {
            processedImage = generateService.processImage(
                    request.getOriginalImage(),
                    request.getMaskX1(),
                    request.getMaskY1(),
                    request.getMaskX2(),
                    request.getMaskY2()
            );
        } catch (IOException e) {
            log.warn("Failed to generated AI image.");
            throw new RuntimeException("Failed to generated AI image", e);
        }

        // s3 file upload
        File aiGeneratedFile = convertMultipartFileToFile(processedImage)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile aiGeneratedFile -> File convert fail"));
        String objectName = randomFileName(aiGeneratedFile, "findDiff_generated_ai");

        // put s3
        amazonS3.putObject(new PutObjectRequest(bucket, objectName, aiGeneratedFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String path = amazonS3.getUrl(bucket, objectName).toString();
        aiGeneratedFile.delete();

        user.uploadAiGeneratedImage(objectName, path, request.getMaskX1(), request.getMaskY1(),request.getMaskX2(), request.getMaskY2());
    }

    @Transactional(readOnly = true)
    public List<FindDiffGameImagesDto> getFindDiffGameImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        return game.getUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(FindDiffGameImagesDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FindDiffResultDto> callFindDiffResultList(Long gameId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        List<FindDiffResultDto> images = game.getUsers().stream()
                .sorted(Comparator.comparingInt(FindDiffUser::getScore).reversed())
                .map(FindDiffResultDto::from)
                .toList();
        return images;
    }

    @Transactional
    public void updateChance(FindDiffScoreRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        int foundScore = request.getIsFound() ? 100 : 0;
        int totalScore = foundScore + request.getChance() * 10;

        user.updateScore(totalScore);
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

}

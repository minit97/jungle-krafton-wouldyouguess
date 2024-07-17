package com.krafton.api_server.api.game2.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game2.domain.FindDiffGame;
import com.krafton.api_server.api.game2.domain.FindDiffImage;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import com.krafton.api_server.api.game2.repository.FindDiffGameRepository;
import com.krafton.api_server.api.game2.repository.FindDiffImageRepository;
import com.krafton.api_server.api.game2.repository.FindDiffUserRepository;
import com.krafton.api_server.api.photo.service.GenerateService;
import com.krafton.api_server.api.photo.service.AwsS3Service;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.FindDiffGeneratedImageResponseDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FindDiffService {

    private final RoomRepository roomRepository;
    private final FindDiffGameRepository findDiffGameRepository;
    private final FindDiffUserRepository findDiffUserRepository;
    private final FindDiffImageRepository findDiffImageRepository;
    private final AwsS3Service awsS3Service;
    private final GenerateService generateService;

    public Long startFindDiff(FindDiffRequestDto request) {

        log.debug("Starting find diff game with roomId: {}", request.getRoomId());
        if (request.getRoomId() == null) {
            throw new IllegalArgumentException("Room ID must not be null");
        }

        FindDiffGame game = findDiffGameRepository.save(new FindDiffGame());

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + request.getRoomId()));

        for (User participant : room.getParticipants()) {
            log.warn("participant : {}",participant);
            FindDiffUser user = FindDiffUser.builder()
                    .userId(participant.getId())
                    .build();

            findDiffUserRepository.save(user);
            game.addUser(user);
        }

        return game.getId();
    }

    public void saveImage(FindDiffImageRequestDto request) throws IOException {
        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 null일 수 없습니다");
        }

        FindDiffUser user = findDiffUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("ID: " + userId + "에 해당하는 FindDiffUser를 찾을 수 없습니다"));


        String imageUrl = awsS3Service.upload(request.getImage());

        FindDiffImage image = user.getImage();
        if (image == null) {
            image = FindDiffImage.builder()
                    .originalUrl(imageUrl).build();

            image.updateUser(user);
            user.updateImage(image);
            findDiffImageRepository.save(image);
        } else {
            image.updateOriginalUrl(imageUrl);
            findDiffImageRepository.save(image);
        }

        findDiffUserRepository.save(user);
    }

    public void saveGeneratedImage(FindDiffGeneratedImageRequestDto request) throws IOException {
        Long userId = request.getUserId();
        FindDiffUser user = findDiffUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        log.warn("{}, {}, {}, {}", request.getMaskX1(),
                request.getMaskY1(),
                request.getMaskX2(),
                request.getMaskY2());

        MultipartFile processedImage = generateService.processImage(
                request.getImage(),
                request.getMask()
        );

        String generatedUrl = awsS3Service.upload(processedImage);

        FindDiffImage image = user.getImage();

        image.updateGen(generatedUrl);
        image.updateMask(request.getMaskX1(), request.getMaskY1(),request.getMaskX2(), request.getMaskY2());

        image.updateUser(user);
        user.updateImage(image);

        findDiffImageRepository.save(image);
        findDiffUserRepository.save(user);

    }

    @Transactional(readOnly = true)
    public List<String> getOriginalImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        return game.getUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(FindDiffUser::getImage)
                .filter(Objects::nonNull)
                .map(FindDiffImage::getOriginalUrl)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FindDiffGeneratedImageResponseDto> getGeneratedImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(() -> new NoSuchElementException("Game not found"));

        log.warn("Game found: {}", game);
        log.warn("Users in game: {}", game.getUsers());

        List<FindDiffGeneratedImageResponseDto> images = game.getUsers().stream()
                .peek(user -> log.warn("Processing user: {}", user))
                .filter(user -> !user.getUserId().equals(userId))
                .peek(user -> log.warn("User passed filter: {}", user))
                .map(FindDiffUser::getImage)
                .filter(Objects::nonNull)
                .peek(image -> log.warn("Image found: {}", image))
                .map(image -> new FindDiffGeneratedImageResponseDto(
                        image.getGeneratedUrl(),
                        image.getMaskX1(),
                        image.getMaskY1(),
                        image.getMaskX2(),
                        image.getMaskY2()
                ))
                .collect(Collectors.toList());

        log.warn("Generated images: {}", images);
        return images;
    }

}

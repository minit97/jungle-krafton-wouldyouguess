package com.krafton.api_server.api.game2.service;

import com.krafton.api_server.api.game2.domain.FindDiffGame;
import com.krafton.api_server.api.game2.domain.FindDiffUser;
import com.krafton.api_server.api.game2.repository.FindDiffGameRepository;
import com.krafton.api_server.api.game2.repository.FindDiffUserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.krafton.api_server.api.game2.dto.FindDiffRequest.*;
import static com.krafton.api_server.api.game2.dto.FindDiffResponse.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindDiffService {
    private final RoomRepository roomRepository;
    private final FindDiffGameRepository findDiffGameRepository;
    private final FindDiffUserRepository findDiffUserRepository;
    private final FindDiffImageService findDiffImageService;

    private static final int FOUND_SCORE = 100;
    private static final int CHANCE_MULTIPLIER = 10;

    @Transactional
    public Long startFindDiff(FindDiffRequestDto request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(NoSuchElementException::new);

        List<FindDiffUser> findDiffUsers = room.getParticipants().stream()
                                                .map(user -> FindDiffUser.builder()
                                                                .userId(user.getId())
                                                                .nickname(user.getUsername())
                                                                .build())
                                                .toList();

        FindDiffGame game = FindDiffGame.builder()
                                .users(new ArrayList<>())
                                .build();
        findDiffUsers.forEach(game::addUser);
        FindDiffGame savedGame = findDiffGameRepository.save(game);

        return savedGame.getId();
    }

    @Transactional
    public FindDiffAiGeneratedImageResponseDto callUploadImage(FindDiffImageUploadRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByGameIdAndUserId(request.getGameId(), request.getUserId())
                .orElseThrow(NoSuchElementException::new);

        findDiffImageService.uploadS3RequestImage(user, request.getOriginalImage(), request.getMaskingImage());
        findDiffImageService.saveGeneratedAiImage(user, request);

        return FindDiffAiGeneratedImageResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public List<FindDiffGameImagesDto> findDiffGameImages(Long gameId, Long userId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(NoSuchElementException::new);

        return game.getUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .map(FindDiffGameImagesDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FindDiffResultDto> findDiffResultList(Long gameId) {
        FindDiffGame game = findDiffGameRepository.findById(gameId)
                .orElseThrow(NoSuchElementException::new);

        List<FindDiffResultDto> images = game.getUsers().stream()
                .sorted(Comparator.comparingInt(FindDiffUser::getScore).reversed())
                .map(FindDiffResultDto::from)
                .toList();
        return images;
    }

    @Transactional
    public void updateChance(FindDiffScoreRequestDto request) {
        FindDiffUser user = findDiffUserRepository.findByGameIdAndUserId(request.getGameId(), request.getUserId())
                .orElseThrow(NoSuchElementException::new);

        int foundScore = request.getIsFound() ? FOUND_SCORE : 0;
        int totalScore = foundScore + request.getChance() * CHANCE_MULTIPLIER;

        user.updateScore(totalScore);
    }
}

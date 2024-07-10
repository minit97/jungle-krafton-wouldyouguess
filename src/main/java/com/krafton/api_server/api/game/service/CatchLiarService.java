package com.krafton.api_server.api.game.service;


import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.game.domain.CatchLiarGame;
import com.krafton.api_server.api.game.domain.CatchLiarKeyword;
import com.krafton.api_server.api.game.domain.CatchLiarUser;
import com.krafton.api_server.api.game.dto.CatchLiarInfoResponseDto;
import com.krafton.api_server.api.game.repository.CatchLiarKeywordRepository;
import com.krafton.api_server.api.game.repository.CatchLiarGameRepository;
import com.krafton.api_server.api.game.repository.CatchLiarUserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.krafton.api_server.api.game.dto.CatchLiarRequest.*;
import static com.krafton.api_server.api.game.dto.CatchLiarRequest.CatchLiarInfoRequestDto;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CatchLiarService {

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


        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < room.getParticipants().size(); i++) {
            numbers.add(i + 1);
        }
        Collections.shuffle(numbers);

        CatchLiarGame game = CatchLiarGame.builder()
                .round(1)
                .build();
        CatchLiarGame startedGame = catchLiarGameRepository.save(game);

        for (int i = 0; i < room.getParticipants().size(); i++) {
            User participant = room.getParticipants().get(i);

            CatchLiarUser user = CatchLiarUser.builder()
                    .userId(participant.getId())
                    .isLiar(i == randomIndex)
                    .keyword(i == randomIndex ? keyword.getLiarKeyword() : keyword.getKeyword())
                    .drawOrder(numbers.get(i))
                    .build();
            catchLiarUserRepository.save(user);

            startedGame.addUsers(user);
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

        return CatchLiarInfoResponseDto.from(matchingUser, request.getRound());
    }


    public List<Long> catchLiarVoteCandidates(Long catchLiarGameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(catchLiarGameId)
                .orElseThrow(NoSuchElementException::new);
        List<CatchLiarUser> catchLiarUsers = game.getCatchLiarUsers();
        List<Long> userIdList = catchLiarUsers.stream()
                .map(CatchLiarUser::getUserId)
                .collect(Collectors.toList());
        return userIdList;
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

    public String catchLiarResult(CatchLiarResultRequestDto request) {
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

        boolean isLiar = request.getUserId().equals(liarUser.getUserId());
        boolean gameResult = userWithMaxVotedCount.equals(liarUser);

        if (isLiar) {
            return gameResult ? "패배" : "승리";
        } else {
            return gameResult ? "승리" : "패배";
        }
    }

}

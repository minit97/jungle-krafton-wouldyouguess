package com.krafton.api_server.api.game.service;


import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.game.domain.CatchLiarKeyword;
import com.krafton.api_server.api.game.repository.CatchLiarKeywordRepository;
import com.krafton.api_server.api.game.repository.CatchLiarRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.krafton.api_server.api.game.dto.CatchLiarRequest.catchLiarKeywordRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatchLiarService {

    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private CatchLiarRepository catchLiarRepository;
    private CatchLiarKeywordRepository catchLiarKeywordRepository;

    public void catchLiarKeyword(catchLiarKeywordRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(NoSuchElementException::new);

        CatchLiarKeyword keyword = catchLiarKeywordRepository.findRandomCatchLiarKeyword();

    }

    public void catchLiarRole() {

    }

    public void catchLiarVoteCandidate() {

    }

    public void catchLiarVote() {


    }

    public void catchLiarResult() {

    }



}

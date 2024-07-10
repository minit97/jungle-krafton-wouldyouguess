package com.krafton.api_server.api.game.controller;

import com.krafton.api_server.api.game.dto.CatchLiarInfoResponseDto;
import com.krafton.api_server.api.game.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krafton.api_server.api.game.dto.CatchLiarRequest.*;
import static com.krafton.api_server.api.game.dto.CatchLiarRequest.CatchLiarInfoRequestDto;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CatchLiarController {

    private final CatchLiarService catchLiarService;

    @PostMapping("/catchLiar/start")
    public ResponseEntity<Long> callCatchLiarStart(@RequestBody CatchLiarStartRequestDto request) {
        Long gameId = catchLiarService.catchLiarStart(request);
        return ResponseEntity.ok(gameId);
    }

    @GetMapping("/catchLiar/info")
    public ResponseEntity<CatchLiarInfoResponseDto> callCatchLiarKeyword(CatchLiarInfoRequestDto request) {
        CatchLiarInfoResponseDto response = catchLiarService.catchLiarInfo(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/catchLiar/candidates")
    public ResponseEntity<List<Long>> callCatchLiarCandidates(Long catchLiarGameId) {
        List<Long> result = catchLiarService.catchLiarVoteCandidates(catchLiarGameId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/catchLiar/vote")
    public void callCatchLiarVote(@RequestBody CatchLiarVoteRequestDto request) {
        catchLiarService.catchLiarVote(request);
    }

    @GetMapping("/catchLiar/result")
    public ResponseEntity<String> callCatchLiarResult(CatchLiarResultRequestDto request) {
        String result = catchLiarService.catchLiarResult(request);
        return ResponseEntity.ok(result);
    }
}

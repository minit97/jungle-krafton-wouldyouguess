package com.krafton.api_server.api.game1.controller;

import com.krafton.api_server.api.game1.dto.response.*;
import com.krafton.api_server.api.game1.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.*;

@RequiredArgsConstructor
@RequestMapping("/api/catchLiar")
@RestController
public class CatchLiarController {

    private final CatchLiarService catchLiarService;

    @PostMapping("/start")
    public ResponseEntity<Long> callCatchLiarStart(@RequestBody CatchLiarStartRequestDto request) {
        Long gameId = catchLiarService.catchLiarStart(request);
        return ResponseEntity.ok(gameId);
    }

    @GetMapping("/info")
    public ResponseEntity<CatchLiarInfoResponseDto> callCatchLiarInfo(CatchLiarInfoRequestDto request) {
        CatchLiarInfoResponseDto response = catchLiarService.catchLiarInfo(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/infos")
    public ResponseEntity<List<CatchLiarInfoListResponseDto>> callCatchLiarInfoList(@RequestParam("catchLiarGameId") Long gameId) {
        List<CatchLiarInfoListResponseDto> response = catchLiarService.catchLiarInfoList(gameId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<CatchLiarVoteCandidatesResponseDto>> callCatchLiarCandidates(Long catchLiarGameId) {
        List<CatchLiarVoteCandidatesResponseDto> result = catchLiarService.catchLiarVoteCandidates(catchLiarGameId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/vote")
    public void callCatchLiarVote(@RequestBody CatchLiarVoteRequestDto request) {
        catchLiarService.catchLiarVote(request);
    }

    @GetMapping("/result")
    public ResponseEntity<List<CatchLiarResultResponseDto>> callCatchLiarResult(CatchLiarResultRequestDto request) {
        List<CatchLiarResultResponseDto> response = catchLiarService.catchLiarResult(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/image")
    public CatchLiarImageResponseDto callCatchLiarImgS3upload(CatchLiarImageUploadRequestDto request) {
        return catchLiarService.catchLiarImgS3upload(request);
    }

    @DeleteMapping("/image")
    public void callCatchLiarImgS3Remove (CatchLiarRemoveRequestDto request) {
        catchLiarService.catchLiarImgS3Remove(request);
    }

    @GetMapping("/{gameId}/keyword")
    public ResponseEntity<CatchLiarKeywordResponse> callCatchLiarKeyword(@PathVariable Long gameId) {
        CatchLiarKeywordResponse response = catchLiarService.catchLiarKeyword(gameId);
        return ResponseEntity.ok(response);
    }
}

package com.krafton.api_server.api.game1.controller;

import com.krafton.api_server.api.game1.dto.CatchLiarInfoListResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarInfoResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarResultResponseDto;
import com.krafton.api_server.api.game1.dto.CatchLiarVoteCandidatesResponseDto;
import com.krafton.api_server.api.game1.service.CatchLiarService;
import com.krafton.api_server.api.photo.domain.AwsS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.krafton.api_server.api.game1.dto.CatchLiarRequest.*;
import static com.krafton.api_server.api.game1.dto.CatchLiarRequest.CatchLiarInfoRequestDto;

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
    public ResponseEntity<CatchLiarInfoResponseDto> callCatchLiarInfo(CatchLiarInfoRequestDto request) {
        CatchLiarInfoResponseDto response = catchLiarService.catchLiarInfo(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/catchLiar/infos")
    public ResponseEntity<List<CatchLiarInfoListResponseDto>> callCatchLiarInfoList(@RequestParam("catchLiarGameId") Long gameId) {
        List<CatchLiarInfoListResponseDto> response = catchLiarService.catchLiarInfoList(gameId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/catchLiar/candidates")
    public ResponseEntity<List<CatchLiarVoteCandidatesResponseDto>> callCatchLiarCandidates(Long catchLiarGameId) {
        List<CatchLiarVoteCandidatesResponseDto> result = catchLiarService.catchLiarVoteCandidates(catchLiarGameId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/catchLiar/vote")
    public void callCatchLiarVote(@RequestBody CatchLiarVoteRequestDto request) {
        catchLiarService.catchLiarVote(request);
    }

    @GetMapping("/catchLiar/result")
    public ResponseEntity<List<CatchLiarResultResponseDto>> callCatchLiarResult(CatchLiarResultRequestDto request) {
        List<CatchLiarResultResponseDto> response = catchLiarService.catchLiarResult(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/catchLiar/image")
    public HashMap<String, String> callCatchLiarImgS3upload(@RequestParam("userId") Long userId,
                                                            @RequestParam("catchLiarGameId") Long gameId,
                                                            @RequestParam("file") MultipartFile file) throws IOException {
        return catchLiarService.catchLiarImgS3upload(userId, gameId, file);
    }

    @DeleteMapping("/catchLiar/image")
    public void callCatchLiarImgS3Remove (AwsS3 awsS3) {
        catchLiarService.catchLiarImgS3Remove(awsS3);
    }
}

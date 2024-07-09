package com.krafton.api_server.api.game.controller;

import com.krafton.api_server.api.game.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.krafton.api_server.api.game.dto.CatchLiarRequest.catchLiarKeywordRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CatchLiarController {

    private final CatchLiarService catchLiarService;

    @GetMapping("/catchLiar/keyword")
    public void callCatchLiarKeyword(catchLiarKeywordRequest request) {
        catchLiarService.catchLiarKeyword(request);
    }

    @GetMapping("/catchLiar/role")
    public void callCatchLiarRole() {
        catchLiarService.catchLiarRole();
    }

    @GetMapping("/catchLiar/vote")
    public void callCatchLiarVoteCandidate() {
        catchLiarService.catchLiarVoteCandidate();
    }

    @PostMapping("/catchLiar/vote")
    public void callCatchLiarVote() {
        catchLiarService.catchLiarVote();
    }

    @GetMapping("/catchLiar/result")
    public void callCatchLiarResult() {
        catchLiarService.catchLiarResult();
    }
}

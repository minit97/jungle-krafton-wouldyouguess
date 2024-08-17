package com.krafton.api_server.api.game1.facade;

import com.krafton.api_server.api.game1.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.CatchLiarVoteRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockFacade {
    private final CatchLiarService catchLiarService;

    public void retryVoteLogic(CatchLiarVoteRequestDto request) throws InterruptedException {
        while (true) {
            try {
                catchLiarService.catchLiarVoteOptimistic(request);

                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }

}

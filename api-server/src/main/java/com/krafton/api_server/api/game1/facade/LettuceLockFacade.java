package com.krafton.api_server.api.game1.facade;

import com.krafton.api_server.api.game1.dto.request.CatchLiarRequest;
import com.krafton.api_server.api.game1.repository.RedisLockRepository;
import com.krafton.api_server.api.game1.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.*;

@RequiredArgsConstructor
@Component
public class LettuceLockFacade {
    private final RedisLockRepository redisLockRepository;
    private final CatchLiarService catchLiarService;

    public void catchLiarVoteLettuce(CatchLiarVoteRequestDto request) throws InterruptedException {
        while (!redisLockRepository.lock(request.getCatchLiarGameId())) {
            Thread.sleep(100);
        }

        try {
            catchLiarService.catchLiarVoteForRedis(request);
        } finally {
            redisLockRepository.unlock(request.getCatchLiarGameId());
        }
    }


}

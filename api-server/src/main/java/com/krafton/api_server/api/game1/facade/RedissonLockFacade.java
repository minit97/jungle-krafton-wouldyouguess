package com.krafton.api_server.api.game1.facade;

import com.krafton.api_server.api.game1.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockFacade {
    private final RedissonClient redissonClient;
    private final CatchLiarService catchLiarService;

    public void catchLiarVoteRedisson(CatchLiarVoteRequestDto requestDto) {
        RLock lock = redissonClient.getLock(requestDto.getCatchLiarGameId().toString() + requestDto.getVotingUserId().toString());

        try {
            boolean available = lock.tryLock(15, 1, TimeUnit.SECONDS);

            if (!available) {
                log.warn("lock 획득 실패 ");
                return;
            }

            catchLiarService.catchLiarVoteForRedis(requestDto);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}

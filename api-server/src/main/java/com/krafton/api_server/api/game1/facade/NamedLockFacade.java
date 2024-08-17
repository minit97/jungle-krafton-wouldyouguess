package com.krafton.api_server.api.game1.facade;

import com.krafton.api_server.api.game1.repository.LockRepository;
import com.krafton.api_server.api.game1.service.CatchLiarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.krafton.api_server.api.game1.dto.request.CatchLiarRequest.*;


@RequiredArgsConstructor
@Component
public class NamedLockFacade {
    private final LockRepository lockRepository;
    private final CatchLiarService catchLiarService;

    @Transactional
    public void catchLiarVoteNamedLock(CatchLiarVoteRequestDto request) {
        try {
            lockRepository.getLock(request.getCatchLiarGameId().toString() + request.getVotingUserId().toString());
            catchLiarService.catchLiarVoteNamedLock(request);
        } finally {
            lockRepository.releaseLock(request.getCatchLiarGameId().toString() + request.getVotingUserId().toString());
        }
    }
}

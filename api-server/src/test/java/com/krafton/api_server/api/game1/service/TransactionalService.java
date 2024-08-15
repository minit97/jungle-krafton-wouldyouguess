package com.krafton.api_server.api.game1.service;

import com.krafton.api_server.api.game1.domain.CatchLiarGame;
import com.krafton.api_server.api.game1.domain.CatchLiarUser;
import com.krafton.api_server.api.game1.repository.CatchLiarGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalService {
    @Autowired
    private CatchLiarGameRepository catchLiarGameRepository;

    @Transactional
    public int getTotalVoteCnt(Long gameId) {
        CatchLiarGame game = catchLiarGameRepository.findById(gameId).get();
        int totalVoteCnt = game.getCatchLiarUsers().stream()
                .filter(user -> user.getVotedCount() != null)
                .mapToInt(CatchLiarUser::getVotedCount)
                .sum();
        return totalVoteCnt;
    }
}

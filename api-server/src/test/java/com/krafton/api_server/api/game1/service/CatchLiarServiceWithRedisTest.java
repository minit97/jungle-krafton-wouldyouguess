package com.krafton.api_server.api.game1.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.game1.domain.CatchLiarKeyword;
import com.krafton.api_server.api.game1.dto.request.CatchLiarRequest;
import com.krafton.api_server.api.game1.facade.LettuceLockFacade;
import com.krafton.api_server.api.game1.facade.RedissonLockFacade;
import com.krafton.api_server.api.game1.repository.CatchLiarGameRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarKeywordRepository;
import com.krafton.api_server.api.game1.repository.CatchLiarUserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CatchLiarServiceWithRedisTest {

    @Autowired
    private CatchLiarService catchLiarService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CatchLiarKeywordRepository catchLiarKeywordRepository;
    @Autowired
    private CatchLiarGameRepository catchLiarGameRepository;
    @Autowired
    private CatchLiarUserRepository catchLiarUserRepository;
    @Autowired
    private LettuceLockFacade lettuceLockFacade;
    @Autowired
    private RedissonLockFacade redissonLockFacade;

    @BeforeEach
    public void before() {
        CatchLiarKeyword keyword = CatchLiarKeyword.builder()
                .liarKeyword("풋사과")
                .keyword("사과")
                .build();
        catchLiarKeywordRepository.save(keyword);
    }

    @AfterEach
    public void after() {
        catchLiarUserRepository.deleteAll();
        catchLiarGameRepository.deleteAll();
        catchLiarKeywordRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("투표 동시성 문제(4명) 테스트 - redis lettuce spin lock")
    void 동시에_4명_투표하기_lettuce() throws InterruptedException {
        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // given
        // 1. 참가자1 방생성 + 참가자2~3 방참가
        User createdUser = User.builder().username("User1").nickname("Nick1").build();
        Room room = Room.builder().user(createdUser).build();

        List<User> joinedUsers = Arrays.asList(
                User.builder().username("User2").nickname("Nick2").build(),
                User.builder().username("User3").nickname("Nick3").build(),
                User.builder().username("User4").nickname("Nick4").build()
        );
        joinedUsers.forEach(room::joinRoom);

        Room savedRoom = roomRepository.save(room);

        // 2. 게임 시작
        CatchLiarRequest.CatchLiarStartRequestDto startedRequest = CatchLiarRequest.CatchLiarStartRequestDto.builder()
                .roomId(savedRoom.getId()).build();
        Long gameId = catchLiarService.catchLiarStart(startedRequest);


        for (int i = 0; i < threadCount; i ++) {
            executorService.submit(() -> {
                try {
                    // request dto
                    CatchLiarRequest.CatchLiarVoteRequestDto request = CatchLiarRequest.CatchLiarVoteRequestDto.builder()
                            .catchLiarGameId(gameId)
                            .votingUserId(createdUser.getId())
                            .build();

                    // when
                    lettuceLockFacade.catchLiarVoteLettuce(request);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();

        // then
        int totalVoteCnt = catchLiarUserRepository.findById(createdUser.getId()).get().getVotedCount();
        assertEquals(4, totalVoteCnt);
    }

    @Test
    @DisplayName("투표 동시성 문제(4명) 테스트 - redis redisson pub/sub")
    void 동시에_4명_투표하기_redisson() throws InterruptedException {
        int threadCount = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // given
        // 1. 참가자1 방생성 + 참가자2~3 방참가
        User createdUser = User.builder().username("User1").nickname("Nick1").build();
        Room room = Room.builder().user(createdUser).build();

        List<User> joinedUsers = Arrays.asList(
                User.builder().username("User2").nickname("Nick2").build(),
                User.builder().username("User3").nickname("Nick3").build(),
                User.builder().username("User4").nickname("Nick4").build()
        );
        joinedUsers.forEach(room::joinRoom);

        Room savedRoom = roomRepository.save(room);

        // 2. 게임 시작
        CatchLiarRequest.CatchLiarStartRequestDto startedRequest = CatchLiarRequest.CatchLiarStartRequestDto.builder()
                .roomId(savedRoom.getId()).build();
        Long gameId = catchLiarService.catchLiarStart(startedRequest);


        for (int i = 0; i < threadCount; i ++) {
            executorService.submit(() -> {
                try {
                    // request dto
                    CatchLiarRequest.CatchLiarVoteRequestDto request = CatchLiarRequest.CatchLiarVoteRequestDto.builder()
                            .catchLiarGameId(gameId)
                            .votingUserId(createdUser.getId())
                            .build();

                    // when
                    redissonLockFacade.catchLiarVoteRedisson(request);
                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await();

        // then
        int totalVoteCnt = catchLiarUserRepository.findById(createdUser.getId()).get().getVotedCount();
        assertEquals(4, totalVoteCnt);
    }
}

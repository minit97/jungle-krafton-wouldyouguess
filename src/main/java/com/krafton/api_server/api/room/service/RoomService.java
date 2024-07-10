package com.krafton.api_server.api.room.service;

import com.krafton.api_server.api.auth.domain.User;
import com.krafton.api_server.api.auth.repository.UserRepository;
import com.krafton.api_server.api.room.domain.Room;
import com.krafton.api_server.api.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.krafton.api_server.api.room.dto.RoomRequest.RoomCreateRequest;
import static com.krafton.api_server.api.room.dto.RoomRequest.RoomUser;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoomService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Long createRoom(RoomCreateRequest roomCreateRequest) {
        User user = userRepository.findById(roomCreateRequest.getUserId())
                .orElseThrow(NoSuchElementException::new);

        Room room = Room.builder()
                .user(user)
                .build();
        Room createdRoom = roomRepository.save(room);
        return createdRoom.getId();
    }

    public List<RoomUser> getRoomUsers(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        List<User> participants = room.getParticipants();
        return participants.stream().map(RoomUser::from).toList();
    }

    public Long joinRoom(Long roomId, RoomCreateRequest roomCreateRequest) {
        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        User joinedUser = userRepository.findById(roomCreateRequest.getUserId()).orElseThrow(NoSuchElementException::new);
        room.joinRoom(joinedUser);
        return room.getId();
    }


    public void exitRoom(Long roomId, RoomCreateRequest roomCreateRequest) {
        Room room = roomRepository.findById(roomId).orElseThrow(NoSuchElementException::new);
        User exitedUser = userRepository.findById(roomCreateRequest.getUserId()).orElseThrow(NoSuchElementException::new);

        room.exitRoom(exitedUser);
        if (room.getParticipants().size() == 0) {
            roomRepository.delete(room);
        }
    }

}

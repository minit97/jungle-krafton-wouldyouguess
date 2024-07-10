package com.krafton.api_server.api.room.dto;

import com.krafton.api_server.api.auth.domain.User;
import lombok.Getter;

import java.util.List;

public class RoomParticipants {

    @Getter
    public static class Participants {
        private Long roomId;
        private List<User> participants;
    }
}

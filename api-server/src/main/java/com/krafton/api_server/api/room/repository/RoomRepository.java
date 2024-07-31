package com.krafton.api_server.api.room.repository;

import com.krafton.api_server.api.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

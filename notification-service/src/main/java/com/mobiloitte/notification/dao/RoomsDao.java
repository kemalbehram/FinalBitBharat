package com.mobiloitte.notification.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.notification.model.Rooms;

public interface RoomsDao extends JpaRepository<Rooms, String> {

	Optional<Rooms> findByRoomName(String string);

}

package com.example.demo.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Chat;

public interface ChatRepo extends JpaRepository<Chat, Integer>{

	@Query("select c from Chat c where c.user1Id =?1 or c.user2Id =?1")
	List<Chat> findAllByUser(Optional<String> userId);

	@Query("select c from Chat c where (c.user1Id =?1 and c.user2Id =?2) or (c.user2Id =?1 and c.user1Id =?2)")
	Chat findByUsers(int user1Id, int user2Id);

}

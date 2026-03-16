package com.example.demo.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Chat;
import model.Message;

public interface MessageRepo extends JpaRepository<Message, Integer>{

}

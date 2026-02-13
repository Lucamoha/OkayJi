package com.okayji.chat.repository;

import com.okayji.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Page<Message> findByChatId(String chatId, Pageable pageable);
}

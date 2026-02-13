package com.okayji.chat.repository;

import com.okayji.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat,String> {
    Chat findByDirectKey(String directKey);

    @Query("""
    select c
    from Chat c
    join ChatMember cm on cm.chat = c
    where cm.member.id = :userId
    order by c.lastMessageAt desc
    """)
    Page<Chat> findMyChatsOrderByLastMessageAt(@Param("userId") String userId, Pageable pageable);

    @Query("""
    select count(m)
    from Message m, ChatMember cm
    where cm.chat.id = :chatId
    and cm.member.id = :userId
    and m.chat.id = :chatId
    and m.seq > coalesce(cm.lastReadSeq, 0)
    and m.sender.id <> :userId
    """)
    long unreadCount(@Param("userId") String userId, @Param("chatId") String chatId);

    @Query("""
    select coalesce(sum(c.lastMessageSeq - coalesce(cm.lastReadSeq, 0)), 0)
    from ChatMember cm
    join cm.chat c
    where cm.member.id = :userId
    and c.lastMessageSeq > coalesce(cm.lastReadSeq, 0)
    """)
    long unreadCount(@Param("userId") String userId);
}

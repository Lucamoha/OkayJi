package com.okayji.chat.repository;

import com.okayji.chat.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember,Long> {
    boolean existsByChat_IdAndMember_Id(String chat_id, String member_id);

    List<ChatMember> findChatMembersByChat_Id(String chat_id);

    Optional<ChatMember> findByChat_IdAndMember_Id(String chat_id, String member_id);

    @Query("""
    select cm
    from ChatMember cm
    join fetch cm.member
    where cm.chat.id = :chatId
    and cm.chat.type = 'DIRECT'
    and cm.member.id <> :currentUserId
    """)
    ChatMember getOtherInDirectChat(@Param("chatId") String chatId,
                                    @Param("currentUserId") String currentUserId);
}

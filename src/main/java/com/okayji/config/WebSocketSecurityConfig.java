package com.okayji.config;

import com.okayji.chat.repository.ChatMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager(
            ChatMemberRepository chatMemberRepository) {
        var messages = MessageMatcherDelegatingAuthorizationManager.builder();

        messages
                .simpDestMatchers("/app/**").authenticated() // Client -> server (@MessageMapping)

                .simpSubscribeDestMatchers("/user/**").authenticated()

                .simpSubscribeDestMatchers("/topic/chats/{chatId}/**")
                .access((auth, ctx) -> {
                    Principal principal = (Principal) auth.get().getPrincipal();
                    String chatId = String.valueOf(ctx.getVariables().get("chatId"));
                    boolean ok = chatMemberRepository
                            .existsByChat_IdAndMember_Id(chatId, principal.getName());
                    return new AuthorizationDecision(ok);
                })

                .anyMessage().permitAll();

        return messages.build();
    }
}

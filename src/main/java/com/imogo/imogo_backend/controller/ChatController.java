package com.imogo.imogo_backend.controller;

import com.imogo.imogo_backend.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        System.out.println(message.getContent());
        return message;
    }
}

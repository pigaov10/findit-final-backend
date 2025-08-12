package com.imogo.imogo_backend.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotificationToAgent(Long agentId, String message) {
        // Envia a notificação para um tópico exclusivo do agente
        simpMessagingTemplate.convertAndSend("/topic/notifications/" + agentId, message);
    }
}

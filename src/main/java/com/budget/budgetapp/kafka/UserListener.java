package com.budget.budgetapp.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserListener {

    @KafkaListener(topics = "user-event", groupId = "budget-app")
    public void handle(UserEvent event) {
      log.info("Received user event: {}", event);
    }

}

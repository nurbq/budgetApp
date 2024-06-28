package com.budget.budgetapp.kafka;

public record UserEvent(String username, String password) implements Event{
}

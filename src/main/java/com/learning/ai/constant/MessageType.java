package com.learning.ai.constant;

public enum MessageType {

    USER("USER"), ASSISTANT("ASSISTANT");

    private String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

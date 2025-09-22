package com.learning.ai.model;

import com.learning.ai.constant.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collation = "chat_message")
public class ChatMessage {

    @Id
    private String id;
    private String userId;
    private String chatSessionId;
    private MessageType messageType;
    private String message;
    private Instant timestamp;
    private Long tokenCount;

    private ChatMessage(String id, String userId,
                        String chatSessionId, MessageType messageType,
                        String message, Instant timestamp, Long tokenCount){
        this.id = id;
        this.userId = userId;
        this.chatSessionId = chatSessionId;
        this.messageType = messageType;
        this.message = message;
        this.timestamp = timestamp;
        this.tokenCount = tokenCount;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getChatSessionId() {
        return chatSessionId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getTokenCount() {
        return tokenCount;
    }

    public static Builder builder(){
        return new Builder();
    }
    public static final class Builder {
        private String id;
        private String userId;
        private String chatSessionId;
        private MessageType messageType;
        private String message;
        private Instant timestamp;
        private Long tokenCount;

        private Builder(){}

        public ChatMessage.Builder id(String id){
            this.id = id;
            return this;
        }

        public ChatMessage.Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public ChatMessage.Builder chatSessionId(String chatSessionId){
            this.chatSessionId = chatSessionId;
            return this;
        }

        public ChatMessage.Builder messageType(MessageType messageType){
            this.messageType = messageType;
            return this;
        }

        public ChatMessage.Builder message(String message){
            this.message = message;
            return this;
        }

        public ChatMessage.Builder timestamp(Instant timestamp){
            this.timestamp = timestamp;
            return this;
        }

        public ChatMessage.Builder tokenCount(Long tokenCount){
            this.tokenCount = tokenCount;
            return this;
        }

        public ChatMessage build(){
            return new ChatMessage(this.id, this.userId, this.chatSessionId,
                    this.messageType, this.message, this.timestamp, this.tokenCount);
        }
    }
}

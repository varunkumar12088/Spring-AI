package com.learning.ai.service;

public interface ChatMessageService {

    default String chat(String query, String userId, String conversionId){
        return "";
    }
}

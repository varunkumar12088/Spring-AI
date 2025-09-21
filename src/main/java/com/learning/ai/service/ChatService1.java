package com.learning.ai.service;

import reactor.core.publisher.Flux;

public interface ChatService1 {

    default String chat(String query){
        return "";
    }

    default String chatCoding(String query){
        return "";
    }

    default String chatJavaCoding(String query){
        return "";
    }

    default Flux<String> chatStream(String query){
        return Flux.just("");
    }
}

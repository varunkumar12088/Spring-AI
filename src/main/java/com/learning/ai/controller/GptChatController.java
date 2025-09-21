package com.learning.ai.controller;

import com.learning.ai.service.ChatService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping(value = "/gpt")
public class GptChatController {

    @Autowired
    @Qualifier("gptChatService")
    private ChatService1 chatService;

    @RequestMapping(value = "/general", method = RequestMethod.GET)
    public ResponseEntity<String> chat(@RequestParam(name = "q") String query){
        String response = chatService.chat(query);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/coding", method = RequestMethod.GET)
    public ResponseEntity<String> chatCoding(@RequestParam(name = "q") String query){
        String response = chatService.chatCoding(query);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(value = "/java", method = RequestMethod.GET)
    public ResponseEntity<String> chatJavaCoding(@RequestParam(name = "q") String query){
        String response = chatService.chatJavaCoding(query);
        return ResponseEntity.ok(response);
    }
    @RequestMapping(value = "/stream", method = RequestMethod.GET)
    public ResponseEntity<Flux<String>> chatStream(@RequestParam(name = "q") String query){
        Flux<String> response = chatService.chatStream(query);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(response);
    }

    @RequestMapping(value = "/stream1", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream1(@RequestParam(name = "q") String query){
        return chatService.chatStream(query)
                .buffer(Duration.ofMillis(50)) // Buffer for 50ms
                .map(chunks -> String.join("", chunks)) // Join buffered chunks
                .filter(text -> !text.trim().isEmpty()); // Filter empty strings;
    }

    @RequestMapping(value = "/stream2", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream2(@RequestParam(name = "q") String query){
        return chatService.chatStream(query)
                .buffer(Duration.ofMillis(100)) // Buffer characters
                .map(chunks -> String.join("", chunks))
                .filter(text -> !text.trim().isEmpty())
                .map(text -> ServerSentEvent.<String>builder()
                        .data(text)
                        .build());
    }

    @RequestMapping(value = "/stream3", method = RequestMethod.GET)
    public ResponseEntity<Flux<String>> chatStream3(@RequestParam(name = "q") String query){
        Flux<String> response = chatService.chatStream(query);
        return ResponseEntity.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(response);
    }
}

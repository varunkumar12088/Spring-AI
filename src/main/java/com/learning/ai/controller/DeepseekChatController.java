package com.learning.ai.controller;

import com.learning.ai.service.ChatService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/deepseek")
public class DeepseekChatController {

    @Autowired
    @Qualifier("deepseekChatService")
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
        return ResponseEntity.ok(response);
    }

}

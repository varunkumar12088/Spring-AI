package com.learning.ai.controller;

import com.learning.ai.dto.Response;
import com.learning.ai.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    @Qualifier("deepseek-general")
    private ChatClient chatClient;

    @Autowired
    private ChatService chatService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> chat(@RequestBody String message){
        System.out.println("Message :: " + message);
        String response = this.chatClient.prompt(message).call().content();
        System.out.println("Response from chat client :: " + response);
        return ResponseEntity.ok(response);
    }


    @RequestMapping(value ="/java", method = RequestMethod.POST)
    public ResponseEntity<?> chatJava(@RequestBody String query){
        String response = chatService.chatJavaCode(query);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/response", method = RequestMethod.POST)
    public ResponseEntity<?> chatResponse(@RequestBody String query){
        Response response = chatService.chatWithResponseMapping(query);
        return ResponseEntity.ok(response);
    }

}

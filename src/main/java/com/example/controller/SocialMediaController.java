package com.example.controller;

import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<String> register(@RequestBody Account newAccount){
        if(newAccount.getUsername() == "" || newAccount.getPassword().length()<5){
            return ResponseEntity.status(400).body("Registrastion unsuccess");
        }
        else if(accountService.isAccountExist(newAccount.getUsername())){
            return ResponseEntity.status(409).body("Account exist");
        }
        accountService.register(newAccount);
        return ResponseEntity.status(200).body("Successfully registerd");
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Object> login(@RequestBody Account account) throws AuthenticationException{
        Account matchedAccount = accountService.login(account.getUsername(), account.getPassword());;
        if(matchedAccount == null){
            return ResponseEntity.status(401).body("No match");
        }
        return ResponseEntity.status(200).body(matchedAccount);
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Object> newMessage (@RequestBody Message newMessage){
        if(newMessage.getMessageText().length() > 255 || 
        !accountService.isAccountExist(newMessage.getPostedBy())||
        newMessage.getMessageText()==""){
            return ResponseEntity.status(400).body("Message unseccessful");
        }
        Message m = messageService.createMessage(newMessage);
        return ResponseEntity.status(200).body(m);
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<Object> findAllMessage(){
        return ResponseEntity.status(200).body(messageService.findAllMessage());
    }

    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Object> findMessageById(@PathVariable int messageId){
        return ResponseEntity.status(200).body(messageService.findMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Object> deleteMessageById(@PathVariable int messageId){
        if(messageService.deleteMessageById(messageId) == null){
            return ResponseEntity.status(200).body(messageService.deleteMessageById(messageId));
        }
        else{
            return ResponseEntity.status(200).body(1);
        }
        
    }

    @PatchMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<Object> patchMessage(@PathVariable int messageId, @RequestBody Message message){
        System.out.println("----------------------" + message.getMessageText());
        if(message.getMessageText().length() > 255 ||  messageService.findMessageById(messageId)==null){
            System.out.println("The text body null");
            return ResponseEntity.status(400).body(null);
        }
        else if(message.getMessageText() == ""){
            System.out.println("The text body null");
            return ResponseEntity.status(400).body(null);
        }
        else{
            System.out.println("The text body not null");
            messageService.patchMessage(messageId, message.getMessageText());
            return ResponseEntity.status(200).body(1);
        }   
                                          
    }

    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<Object> getAllMessageByPostBy(@PathVariable int accountId){
        List<Message> messages = messageService.getAllMessagesByPostBy(accountId);
        return ResponseEntity.status(200).body(messages);
    }
    
    
    

}

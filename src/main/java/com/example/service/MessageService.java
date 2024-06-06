package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;



@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message newMessage){

        return messageRepository.save(newMessage);
    }

    public List<Message> findAllMessage(){
        return messageRepository.findAll();
    }

    public Message findMessageById(int id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(messageOptional.isPresent()){
            return messageOptional.get();
        }
        return null;
    }

    public Message deleteMessageById(int id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(messageOptional.isPresent()){
            Message m = messageOptional.get();
            messageRepository.deleteById(id);
            return m;
        }
        return null;
        
    }

    public Message patchMessage(int id, String textBody){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(messageOptional.isPresent()){
            Message message = messageOptional.get();
            message.setMessageText(textBody);
            messageRepository.save(message);
            return message;
        }
        return null;
    }

    public List<Message> getAllMessagesByPostBy(int accountId){
        List<Message> messages= messageRepository.findByPostedBy(accountId);
        return messages;
    }

}

package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    public Message findById(int id) {
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message) {
        message.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return messageRepository.save(message);
    }

    public void deleteById(int id) {
        messageRepository.deleteById(id);
    }
}

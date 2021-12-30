package ru.job4j.chat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.MessageService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Iterable<Message>> findAll() {
        return new ResponseEntity<>(messageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Message message = messageService.findById(id);
        return new ResponseEntity<>(
                message, message != null ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return new ResponseEntity<>(
                messageService.saveMessage(message), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody Message message) {
        message.setId(id);
        Message newMessage = messageService.saveMessage(message);
        newMessage.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(newMessage,
                message.getId() == newMessage.getId() ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        messageService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
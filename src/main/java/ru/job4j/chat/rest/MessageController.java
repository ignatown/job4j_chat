package ru.job4j.chat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.Patcher;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
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
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The message with this id was not found"
            );
        }
        return new ResponseEntity<>(
                message, HttpStatus.OK
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Message> saveMessage(@RequestBody @Valid Message message) {
        if (message.getText() == null) {
            throw new NullPointerException("The message must contain the text");
        }
        return new ResponseEntity<>(
                messageService.saveMessage(message), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable int id, @RequestBody @Valid Message message) {
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

    @PatchMapping("/")
    public ResponseEntity<Message> patch(@RequestBody @Valid Message message) throws InvocationTargetException, IllegalAccessException {
        Message patchableMessage = messageService.findById(message.getId());
        if (patchableMessage == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Patcher.patch(patchableMessage, message);
        return new ResponseEntity<>(
                messageService.saveMessage(patchableMessage),
                HttpStatus.OK
        );
    }
}

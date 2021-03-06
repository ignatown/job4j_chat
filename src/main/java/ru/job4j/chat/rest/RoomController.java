package ru.job4j.chat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.Patcher;
import ru.job4j.chat.service.RoomService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Iterable<Room>> findAll() {
        return new ResponseEntity<>(roomService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        Room room = roomService.findById(id);
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no room with this id");
        }
        return new ResponseEntity<>(
                room, HttpStatus.OK
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Room> saveRoom(@RequestBody @Valid Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("The room must have a name");
        }
        return new ResponseEntity<>(
                roomService.saveRoom(room), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable int id, @RequestBody @Valid Room room) {
        room.setId(id);
        Room newRoom = roomService.saveRoom(room);
        return new ResponseEntity<>(newRoom,
                room.getId() == newRoom.getId() ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id) {
        roomService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Room> patch(@RequestBody @Valid Room room) throws InvocationTargetException, IllegalAccessException {
        Room patchableRoom = roomService.findById(room.getId());
        if (patchableRoom == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Patcher.patch(patchableRoom, room);
        return new ResponseEntity<>(
                roomService.saveRoom(patchableRoom),
                HttpStatus.OK
        );
    }
}

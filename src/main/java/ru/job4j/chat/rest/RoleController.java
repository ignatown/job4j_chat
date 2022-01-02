package ru.job4j.chat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Iterable<Role>> findAll() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
         Role role = roleService.findById(id);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The role with this id was not found"
            );
        }
        return new ResponseEntity<>(
                    role,  HttpStatus.OK
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("The role must have a name");
        }
        return new ResponseEntity<>(
                roleService.saveRole(role), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable int id, @RequestBody Role role) {
        role.setId(id);
        Role newRole = roleService.saveRole(role);
        return new ResponseEntity<>(newRole,
                role.getId() == newRole.getId() ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        roleService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}


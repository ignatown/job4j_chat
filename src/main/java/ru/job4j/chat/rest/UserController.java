package ru.job4j.chat.rest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

@RestController
@RequestMapping("users")
public class UserController {

    private final PersonService personService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    public UserController(PersonService personService,
                          RoleService roleService,
                          BCryptPasswordEncoder encoder) {
        this.personService = personService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping("/all")
    public Iterable<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(roleService.findRoleByName("ROLE_USER"));
        personService.savePerson(person);
    }
}
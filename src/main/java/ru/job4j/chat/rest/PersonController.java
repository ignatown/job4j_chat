package ru.job4j.chat.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.Patcher;
import ru.job4j.chat.service.PersonService;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Iterable<Person>> findAll() {
        return new ResponseEntity<>(personService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Person person = personService.findById(id);
        if (person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The user with this id does not exist"
            );
        }
        return new ResponseEntity<>(
                person, HttpStatus.OK
        );
    }

    @PostMapping({"/", ""})
    public ResponseEntity<Person> savePerson(@RequestBody Person person) {
        if (person.getPassword() == null || person.getUsername() == null) {
            throw new NullPointerException("All fields must be filled in");
        }
        return new ResponseEntity<>(
                personService.savePerson(person), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person person) {
        person.setId(id);
        Person newPerson = personService.savePerson(person);
        return new ResponseEntity<>(newPerson,
                person.getId() == newPerson.getId() ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        personService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public ResponseEntity<Person> patch(@RequestBody Person person) throws InvocationTargetException, IllegalAccessException {
        Person patchablePerson = personService.findById(person.getId());
        if (patchablePerson == null) {
            throw new IllegalArgumentException("Person with this id is not found");
        }
        Patcher.patch(patchablePerson, person);
        return new ResponseEntity<>(
                personService.savePerson(patchablePerson),
                HttpStatus.OK
        );
    }
}

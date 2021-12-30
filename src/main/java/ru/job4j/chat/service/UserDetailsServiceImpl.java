package ru.job4j.chat.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonService personService;

    public UserDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personService.findByUsername(username).orElse(null);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.getUsername(), person.getPassword(), Collections.emptyList());
    }
}
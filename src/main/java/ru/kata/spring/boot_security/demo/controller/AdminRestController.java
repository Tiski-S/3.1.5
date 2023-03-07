package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminRestController {
    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping()
    public ResponseEntity<List<User>> getUserList() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        Role role = userService.getRole(user.getRoles().stream().findFirst().get().getId());
        user.setRoles(Arrays.asList(role));
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Role role = userService.getRole(user.getRoles().stream().findFirst().get().getId());
        user.setRoles(Arrays.asList(role));
        if (user.getPassword().equals("")) {
            user.setPassword(userService.getUser(user.getId()).getPassword());
            userService.updateUserWithoutEncode(user);
        }
        else
        {
            userService.updateUser(user);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}

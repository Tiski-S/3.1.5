package ru.kata.spring.boot_security.demo.service;





import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();
    User getUser(int id);
    void saveUser(User user);
    void deleteUser(int id);
    void updateUser(User user);
    void updateUserWithoutEncode(User user);
    User getUserByName(String username);

    Role getRole(int id);
}

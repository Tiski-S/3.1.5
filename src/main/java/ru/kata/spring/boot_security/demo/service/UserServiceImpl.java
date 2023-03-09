package ru.kata.spring.boot_security.demo.service;



import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repo.RoleRepository;
import ru.kata.spring.boot_security.demo.repo.UserRepository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository,  RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setA(user.getPassword());
        List<Role> roles = new ArrayList<>();
        for (Role role: user.getRoles()) {
            roles.add(getRole(role.getId()));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getA()));
        userRepository.save(user);
        
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
//        userRepository.deleteById(id);
        Optional<User> user = userRepository.findById(id);
        User user1 = user.get();
        user1.setRoles(null);
        userRepository.save(user1);
//        userRepository.delete(user.get());
        userRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void updateUser(User user) {
        user.setA(user.getPassword());
        if (user.getPassword().equals("")) {
            user.setPassword(getUser(user.getId()).getPassword());
        }
        else
        {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public User getUserByName(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user.orElse(null);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        Hibernate.initialize(user.getRoles());
        return user;
    }

    @Override
    public Role getRole(int id) {
        return roleRepository.getById(id);
    }


}

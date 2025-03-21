package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;

import java.util.List;

@Service
public class MyUserServiceImpl {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserServiceImpl(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<MyUser> showAll() {
        return myUserRepository.findAll();
    }

    public MyUser showById(int id) {
        return myUserRepository.findById(id).orElse(null);
    }

    public void save(MyUser user) {
        myUserRepository.save(user);
    }

    public void update(int id, MyUser myUser) {

        MyUser updateUser = showById(id);

        if (myUser.getUsername() != null) {
            updateUser.setUsername(myUser.getUsername());
        }
        if (myUser.getAge() != 0) {
            updateUser.setAge(myUser.getAge());
        }
        if (myUser.getEmail() != null) {
            updateUser.setEmail(myUser.getEmail());
        }
        if (!myUser.getPassword().equals(updateUser.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        }
        if (myUser.getRoles() != null) {
            updateUser.setRoles(myUser.getRoles());
        }

        myUserRepository.save(myUser);
    }

    public void deleteById(int id) {
        myUserRepository.deleteById(id);
    }
}

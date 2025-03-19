package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;

import java.util.List;

@Service
public class MyUserServiceImpl {

    private final MyUserRepository myUserRepository;

    @Autowired
    public MyUserServiceImpl(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
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

    public void deleteById(int id) {
        myUserRepository.deleteById(id);
    }
}

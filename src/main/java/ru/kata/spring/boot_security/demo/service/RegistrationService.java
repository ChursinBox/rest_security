package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.Collections;

@Service
public class RegistrationService {

    private final MyUserRepository myUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(MyUserRepository myUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(MyUser myUser) {
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setRoles(Collections.singleton(roleRepository.findByRoleName("ROLE_USER")));
        myUserRepository.save(myUser);
    }
}

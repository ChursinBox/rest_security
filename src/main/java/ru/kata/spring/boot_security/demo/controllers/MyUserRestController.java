package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.MyUserDTO;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.MyUserServiceImpl;
import ru.kata.spring.boot_security.demo.service.RegistrationService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class MyUserRestController {

    private final MyUserServiceImpl myUserService;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;
    private final RoleRepository roleRepository;


    @Autowired
    public MyUserRestController(MyUserServiceImpl myUserService, PasswordEncoder passwordEncoder, RegistrationService registrationService, RoleRepository roleRepository) {
        this.myUserService = myUserService;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<MyUserDTO> getUser() {
        return myUserService.showAll().stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MyUserDTO showByIdJson(@PathVariable int id) {
        return toDTO(myUserService.showById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName("ROLE_USER")));
        registrationService.register(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable int id, @RequestBody MyUser user) {

        myUserService.update(id, user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        myUserService.deleteById(id);
    }

    private MyUserDTO toDTO(MyUser user) {
        MyUserDTO myUserDTO = new MyUserDTO();
        myUserDTO.setId(user.getId());
        myUserDTO.setUsername(user.getUsername());
        myUserDTO.setAge(user.getAge());
        myUserDTO.setEmail(user.getEmail());
        myUserDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        myUserDTO.setRoles(user.getRoles());

        return myUserDTO;
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}

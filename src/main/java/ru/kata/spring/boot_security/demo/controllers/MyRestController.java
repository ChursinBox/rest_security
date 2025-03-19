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
@RequestMapping("/api")
public class MyRestController {

    private final MyUserServiceImpl myUserService;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;
    private final RoleRepository roleRepository;


    @Autowired
    public MyRestController(MyUserServiceImpl myUserService, PasswordEncoder passwordEncoder, RegistrationService registrationService, RoleRepository roleRepository) {
        this.myUserService = myUserService;
        this.passwordEncoder = passwordEncoder;
        this.registrationService = registrationService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<MyUserDTO> getPeopleJson() {
        return myUserService.showAll().stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/all/{id}")
    @ResponseBody
    public MyUserDTO showByIdJson(@PathVariable int id) {
        return toDTO(myUserService.showById(id));
    }

    @PostMapping("/all")
    @ResponseBody
    public ResponseEntity<HttpStatus> createUserJson(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName("ROLE_USER")));
        registrationService.register(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/all/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable int id, @RequestBody MyUser user) {

        MyUser updateUser = myUserService.showById(id);

        if (user.getUsername() != null) {
            updateUser.setUsername(user.getUsername());
        }
        if (user.getAge() != 0) {
            updateUser.setAge(user.getAge());
        }
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (!user.getPassword().equals(updateUser.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRoles() != null) {
            updateUser.setRoles(user.getRoles());
        }

        myUserService.save(updateUser);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/all/{id}")
    @ResponseBody
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
    @ResponseBody
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}

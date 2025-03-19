package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
     MyUser findByUsername(String username);
}

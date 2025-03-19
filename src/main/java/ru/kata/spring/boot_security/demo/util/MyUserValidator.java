package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.service.MyUserDetailService;

@Component
public class MyUserValidator implements Validator {

    private final MyUserDetailService myUserDetailService;

    @Autowired
    public MyUserValidator(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MyUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MyUser myUser = (MyUser) target;

        try {
            myUserDetailService.loadUserByUsername(myUser.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username", "", "такой уже есть");
    }
}

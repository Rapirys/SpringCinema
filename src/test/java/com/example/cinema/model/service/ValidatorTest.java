package com.example.cinema.model.service;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    @Autowired
    Validator valid;
    @Test
    void nullEmail() {
        assertFalse (valid.email(null));
    }
    @Test
    void validEmail() {
        assertTrue (valid.email("gregor@gmail.com"));
        assertTrue (valid.email("gregordsadasfasfasfa@gmail.com"));
    }
    @Test
    void invalidEmail() {
        assertFalse (valid.email("gregor@gmail.commm"));
        assertFalse (valid.email("gregor@gmail..com"));
        assertFalse (valid.email("gregor@гмаил.com"));
    }



    @Test
    void invalidUsername() {
        assertFalse (valid.email("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName"));
        assertFalse (valid.email("Д"));
    }

}
package com.example.cinema.model.service;

import com.example.cinema.entities.User;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
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
    void  validPassword(){
        assertFalse (valid.password("111Q2"));
        assertFalse (valid.password("abcd"));
        assertTrue (valid.password("1233456q"));
    }


    @Test
    void invalidUsername() {
        assertFalse (valid.email("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName"));
        assertFalse (valid.email("Д"));
    }

    @Test
    void cvv() {
        assertFalse(valid.cvv("aaa"));
        assertFalse(valid.cvv("11111"));
        assertTrue(valid.cvv("123"));
        assertTrue(valid.cvv("1234"));
    }

    @Test
    void cardNumber() {
        assertFalse(valid.cardNumber("1111 1111 1111"));
        assertFalse(valid.cardNumber("1234567812345678"));
        assertFalse(valid.cardNumber("1234 5a78 1234 5678"));
        assertTrue(valid.cardNumber("1234 5678 1234 5678"));
    }

    @Test
    void year() {
        assertFalse(valid.year(1000));
        assertFalse(valid.year(10000));
        assertTrue(valid.year(2025));
        assertTrue(valid.year(2030));
    }

    @Test
    void month() {
        assertFalse(valid.month(-10));
        assertFalse(valid.month(13));
        assertTrue(valid.month(1));
        assertTrue(valid.month(5));
    }

    @Test
    void validCard() {
        String cardN="1111 1111 1111 1111";
        String name="Asd Jkc";
        String cvv="1111";
        Integer y = 2024;
        Integer m =12;
        assertTrue(valid.validCard(cardN, cvv , m, y, name));

        assertFalse(valid.validCard(cardN, cvv , m, 1000, name));
        assertFalse(valid.validCard(cardN, cvv , -10, y, name));
        assertFalse(valid.validCard(cardN, "aaa" , m, y, name));
        assertFalse(valid.validCard("1234567812345678", cvv , m, y, name));
    }

    @Test
    void toValidDate() {
        assertThat(valid.toValidDate(LocalDate.now().minusDays(7),7)).isEqualTo(LocalDate.now().plusDays(7));
    }


    @Test
    void validUserFields() {
        User user= new User();
        user.setPassword("12345678q").setEmail("gregor@gmail.com").setUsername("Name");
        assertEquals(0, valid.validUserFields(user).size());
        user.setPassword("1111");
        assertTrue(valid.validUserFields(user).size()!=0);
        user.setPassword("12345678q");
        user.setUsername("NameTooLongggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        assertTrue(valid.validUserFields(user).size()!=0);
    }
}
package com.spring.cinema.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MvcConfigTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Test
    void init() {
        boolean f = jdbcTemplate.queryForObject("SELECT EXISTS (select * from pg_proc where proname = 'findhall')", Boolean.class);
        assertTrue(f);
    }
}
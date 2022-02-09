package com.example.cinema;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import java.time.Duration;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CinemaApplicationTests {
    @Autowired
    ConversionService conversionService;
    @Test
    void stringToDuration() {
        Duration duration = conversionService
                .convert("08:30", Duration.class);
        Duration actualDuration = Duration.between(LocalTime.MIN, LocalTime.parse("08:30"));

        assertThat(duration).isEqualByComparingTo(actualDuration);
    }

}

package com.spring.cinema;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ConversionTests {
    @Autowired
    ConversionService conversionService;

    @Test
    void stringToLocalDate() {
        LocalDate localDate = conversionService.convert("2004-01-12", LocalDate.class);
        LocalDate localDate1 = LocalDate.parse("2004-01-12");
        assertTrue(localDate.isEqual(localDate1));
    }

    @Test
    void stringTo() {
        Duration duration = conversionService.convert("08:30", Duration.class);
        Duration actualDuration = Duration.between(LocalTime.MIN, LocalTime.parse("08:30"));
        assertThat(duration).isEqualByComparingTo(actualDuration);
    }

    @Test
    void stringToLocalTime() {
        LocalTime localTime = conversionService.convert("08:30", LocalTime.class);
        LocalTime localTime1 = LocalTime.parse("08:30");
        assertEquals(localTime, localTime1);
    }

}

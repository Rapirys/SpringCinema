package com.spring.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/src/main/upload/");
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver r = new CookieLocaleResolver();
        r.setDefaultLocale(new Locale("en"));
        r.setCookieName("lang");
        return r;
    }

    public class DurationToStringConverter implements Converter<String, Duration> {
        @Override
        public Duration convert(String source) {
            return Duration.between(LocalTime.MIN, LocalTime.parse(source));
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Duration>() {
            @Override
            public Duration convert(String source) {
                return Duration.between(LocalTime.MIN, LocalTime.parse(source));
            }
        });
        registry.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source);
            }
        });
        registry.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source);
            }
        });
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    void init() {
        jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION public.findhall(numeric)
                       RETURNS TABLE(ticket_id numeric, place numeric, "row" numeric, order_id numeric, session_id numeric, salt numeric)
                       LANGUAGE plpgsql
                      AS $function$
                          DECLARE
                           rec RECORD;
                           var ALIAS FOR $1;
                           cur CURSOR FOR
                           SELECT o.session_id AS session_id,
                                  t.salt AS salt,
                                  t.ticket_id AS ticket_id,
                                  o.order_id AS order_id,
                                  t.place AS place,                                      
                                  t."row" AS "row",
                                  o.active AS active,
                                  o."time" AS start_time\s
                           FROM tickets AS t\s
                                INNER JOIN orders AS o ON\s
                                t.order_id =o.order_id\s
                                INNER JOIN "session" AS s ON
                                o.session_id =s.session_id\s
                           WHERE s.session_id =var
                           FOR UPDATE;
                           BEGIN
                              FOR rec IN cur
                                      LOOP\s
                                       IF (rec.active=TRUE OR rec.start_time>=now()::timestamp - INTERVAL '1 min') THEN
                                         ticket_id=rec.ticket_id;
                                         place = rec.place;
                                        "row"=rec."row";
                                         order_id=rec.order_id;
                                         session_id=rec.session_id;
                                         salt=rec.salt;
                                         RETURN next;
                                       ELSE
                                         DELETE FROM orders WHERE CURRENT OF cur;
                                       END IF;
                                      END LOOP;
                             END;
                         $function$
                ;"""
        );
    }
}


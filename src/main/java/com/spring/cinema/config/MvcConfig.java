package com.spring.cinema.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.convert.converter.Converter;

import org.springframework.format.FormatterRegistry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;

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
    public LocaleResolver localeResolver () {
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
        registry.addConverter(new Converter<String, LocalDate>() {@Override
            public LocalDate convert(String source) {return LocalDate.parse(source);}
        });
        registry.addConverter(new Converter<String, LocalTime>() {@Override
        public LocalTime convert(String source) {return LocalTime.parse(source);}
        });
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    void init() {
     jdbcTemplate.execute(
             "CREATE OR REPLACE FUNCTION public.findhall(numeric)\n" +
                     " RETURNS TABLE(ticket_id numeric, place numeric, \"row\" numeric, order_id numeric, session_id numeric, salt numeric)\n" +
                     " LANGUAGE plpgsql\n" +
                     "AS $function$ \n" +
                     "    DECLARE\n" +
                     "     rec RECORD;\n" +
                     "     var ALIAS FOR $1; \n" +
                     "     cur CURSOR FOR\n" +
                     "     SELECT o.session_id AS session_id,t.salt AS salt ,t.ticket_id AS ticket_id, o.order_id AS order_id,\n" +
                     "     t.place AS place, t.\"row\" AS \"row\", o.active AS active, o.\"time\" AS start_time FROM \n" +
                     "     tickets AS t INNER JOIN orders AS o ON t.order_id =o.order_id INNER JOIN \"session\" AS s ON o.session_id =s.session_id \n" +
                     "     WHERE s.session_id =var\n" +
                     "     FOR UPDATE;\n" +
                     "     BEGIN\n" +
                     "        FOR rec IN cur\n" +
                     "                LOOP \n" +
                     "                 IF (rec.active=TRUE OR rec.start_time>=now()::timestamp - INTERVAL '15 min') THEN\n" +
                     "                   ticket_id=rec.ticket_id;\n" +
                     "                   place = rec.place;\n" +
                     "                  \"row\"=rec.\"row\";\n" +
                     "                   order_id=rec.order_id;\n" +
                     "                   session_id=rec.session_id;\n" +
                     "                   salt=rec.salt;\n" +
                     "                   RETURN next;\n" +
                     "                 ELSE\n" +
                     "                   DELETE FROM orders WHERE CURRENT OF cur;\n" +
                     "                 END IF;\n" +
                     "                END LOOP;\n" +
                     "       END;\n" +
                     "   $function$;"
     );
    }
}
package com.example.cinema.config;

import org.springframework.boot.convert.DurationFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
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


}
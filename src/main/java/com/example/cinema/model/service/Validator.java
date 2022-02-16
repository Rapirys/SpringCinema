package com.example.cinema.model.service;

import com.example.cinema.entities.User;
import com.example.cinema.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class Validator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    public static final Pattern PASSWORD_REGEX = Pattern.compile("^[A-Za-z0-9]{6,40}");//"^[a-zA-Z][a-zA-Z0-9-_\\.]{6,20}$");


    public boolean email(String s){
        return s!=null && s.matches(VALID_EMAIL_ADDRESS_REGEX.pattern());
    }
    public boolean password(String s){
        return s!=null && s.matches(PASSWORD_REGEX.pattern());
    }
    public boolean username(String s){
        return s!=null && s.length()>2 && s.length()<=20;
    }
    public boolean cvv(String cvv){
       return cvv!=null && cvv.matches("^[0-9]{3,4}");
    }
    public boolean cardNumber(String cardNumber){
         return cardNumber!=null && cardNumber.matches("^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}");
    }
    public boolean year(Integer year){
       return year!=null && year>=2022 && year<=3000;
    }
    public boolean month(Integer month){
        return month!=null && month>=0 && month<=12;
    }
    public List<String> validUserFields(User user){
        List<String> s=new ArrayList<>();
        if (!email(user.getEmail()))
            s.add("Incorrect_email");
        if (!username(user.getUsername()))
            s.add("Username_too_short_or_too_long");
        if (!password(user.getPassword()))
            s.add("Password_problem");
        return s;
    }

    public LocalDate toValidDate(LocalDate date,int shift) {
        if (date==null || date.isBefore(LocalDate.now()))
            date=LocalDate.now().plusDays(shift);
        return date;
    }

    public boolean validCard(String cardNumber, String cvv, Integer dateM, Integer dateY, String holder) {
        return cardNumber(cardNumber) && cvv(cvv) && year(dateY) && month(dateM) && (holder!=null);
    }
}

package com.example.cinema.model.service;

import com.example.cinema.config.entities.User;
import com.example.cinema.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class Validator {

    @Autowired
    private UserRepository userRepository;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    public static final Pattern PASSWORD_REGEX = Pattern.compile("^[A-Za-z0-9]{6,40}");//"^[a-zA-Z][a-zA-Z0-9-_\\.]{6,20}$");


    public boolean email(String s){
        return s.matches(VALID_EMAIL_ADDRESS_REGEX.pattern());
    };
    public boolean password(String s){ return s.matches(PASSWORD_REGEX.pattern());};
    public boolean username(String s){ return s.length()>2 && s.length()<=20;}


    public List<String> validUserFields(User user){
        List<String> s=new ArrayList<>();
        User userFromDb = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (!email(user.getEmail()))
            s.add("Incorrect_email");
        if (!username(user.getUsername()))
            s.add("Username_too_short_or_too_long");
        if (!password(user.getPassword()))
            s.add("Password_problem");
        if (userFromDb != null) {
            s.add("User_already_exist");
        }

        System.out.println(s.toString()+" "+1324);
        return s;
    }
}

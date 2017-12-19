package com.example.jpademo.controller;

import com.example.jpademo.entity.UserDetails;
import com.example.jpademo.entity.UserDetailsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDetailsController {

    @Autowired
    UserDetailsQuery userDetailsQuery;

    @GetMapping(path = "/userdetails")
    @ResponseBody
    public List<UserDetails> index(@Param("username") String username) {

        return userDetailsQuery.findUserDetailsByUsername(username);
    }
}

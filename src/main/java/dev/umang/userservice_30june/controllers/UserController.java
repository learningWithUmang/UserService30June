package dev.umang.userservice_30june.controllers;

import dev.umang.userservice_30june.dtos.SignupRequestDTO;
import dev.umang.userservice_30june.models.User;
import dev.umang.userservice_30june.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.random.RandomGenerator;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    /*
    I want to implement user service that should have 4 functionalities
    1. signup
    2. login
    3. logout
    4. validate token
     */

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        String name = signupRequestDTO.getName();
        String email = signupRequestDTO.getEmail();
        String password = signupRequestDTO.getPassword();

        userService.signup(name,
                email,
                password);


        return ResponseEntity.ok("User signed up successfully with email: " + email);
    }


}


/*
I want all of you to come with the following things done by next class:-

1. Put a check if email is already registered or not.
2. implement login (input - email, password; output - token)
3. implement logout (input - token; output - success message)
4. implement validate token (input - token; output - user details)

Try these before next class, if you are not able to do it, we will do it together in the next class.
 */
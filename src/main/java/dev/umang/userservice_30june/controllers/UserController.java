package dev.umang.userservice_30june.controllers;

import dev.umang.userservice_30june.dtos.*;
import dev.umang.userservice_30june.dtos.ResponseStatus;
import dev.umang.userservice_30june.models.Token;
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
    1. signup ---done
    2. login ?
    3. logout
    4. validate token
     */

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        String name = signupRequestDTO.getName();
        String email = signupRequestDTO.getEmail();
        String password = signupRequestDTO.getPassword();

        SignupResponseDTO signupResponseDTO = new SignupResponseDTO();

        try {
            User user = userService.signup(name,
                    email,
                    password);

            signupResponseDTO.setMessage("User signed up successfully");
            signupResponseDTO.setEmail(user.getEmail());
            signupResponseDTO.setStatus(ResponseStatus.SUCCESS);

            //sign up is done
            //publish an event into kafka queue

            return ResponseEntity.ok(signupResponseDTO);

        }catch (Exception e) {
            signupResponseDTO.setMessage("Error signing up user: " + e.getMessage());
            signupResponseDTO.setEmail(email);
            signupResponseDTO.setStatus(ResponseStatus.FAILURE);
            return ResponseEntity.status(400).body(signupResponseDTO);
        }
    }

    /*
    I want to design the login api

    Request DTO => email , password
    Response DTO
     */

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

        try{
            Token token = userService.login(email, password);
            loginResponseDTO.setToken(token.getToken());
            loginResponseDTO.setExpiryAt(token.getExpiresAt());
            loginResponseDTO.setEmail(token.getUser().getEmail());
            loginResponseDTO.setMessage("User logged in successfully");
            loginResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            return ResponseEntity.ok(loginResponseDTO);

        }catch (Exception e) {
            loginResponseDTO.setMessage("Error logging in user: " + e.getMessage());
            loginResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
            return ResponseEntity.status(400).body(loginResponseDTO);
        }
    }

    @PatchMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try {
            userService.logout(logoutRequestDTO.getToken());
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error logging out user: " + e.getMessage());
        }
    }


    @GetMapping("/validate-token/{token}") //validate-token/12345
    public TokenValidatedResponseDTO validateToken(@PathVariable("token") String token){
        TokenValidatedResponseDTO tokenValidatedResponseDTO = new TokenValidatedResponseDTO();
        try {
            User user = userService.validateToken(token);
            tokenValidatedResponseDTO.setEmail(user.getEmail());
            tokenValidatedResponseDTO.setNameOfUser(user.getName());
            tokenValidatedResponseDTO.setMessage("Token is valid");
            tokenValidatedResponseDTO.setToken(token);
            tokenValidatedResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            tokenValidatedResponseDTO.setMessage("Invalid token: " + e.getMessage());
            tokenValidatedResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return tokenValidatedResponseDTO;
    }

    /*
    Soft delete??
     */

}
/*
Whast
 */

/*
I want all of you to come with the following things done by next class:-

1. Put a check if email is already registered or not.
2. implement login (input - email, password; output - token)
3. implement logout (input - token; output - success message)
4. implement validate token (input - token; output - user details)

Try these before next class, if you are not able to do it, we will do it together in the next class.
 */
package dev.umang.userservice_30june.services;


import dev.umang.userservice_30june.models.Token;
import dev.umang.userservice_30june.models.User;
import dev.umang.userservice_30june.repositories.TokenRepository;
import dev.umang.userservice_30june.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                        TokenRepository tokenRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User signup(String name,
                         String email,
                         String password) throws Exception {
        // Check if the user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent()){
            throw new Exception("User with this email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // In a real application, you should encrypt the password before saving it
        // Save the user to the database (this part is not implemented here)

        userRepository.save(user);
        return user;
    }

    public Token login(String email,
                       String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        // Check if the user exists
        if(optionalUser.isEmpty()){
            throw new Exception("User with this email does not exist");
        }

        User user = optionalUser.get();

        //validate the password
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid email or password");
        }


        // Generate a token for the user (this part is not implemented here)
        Token token = new Token();
        token.setUser(user);
        token.setToken(RandomStringUtils.randomAlphanumeric(120));
        //expiry of the token is the business logic of application
        //set the token expiry to 3 days from now
        token.setExpiresAt(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000)); // 3 days in milliseconds
        // Save the token to the database (this part is not implemented here)
        tokenRepository.save(token);
        return token;
    }

    public void logout(String token){
        Optional<Token> optionalToken = tokenRepository.findByToken(token);

        if(optionalToken.isEmpty()){
            throw new RuntimeException("Token not found");
        }

        Token existingToken = optionalToken.get();
        // Delete the token from the database
        existingToken.setIsDeleted(true);
        tokenRepository.save(existingToken);
        return;
    }

    public User validateToken(String token){

        Optional<Token> optionalToken = tokenRepository.findByTokenAndIsDeletedEqualsAndExpiresAtAfter(token, false, System.currentTimeMillis());

        if(optionalToken.isEmpty()){
            throw new RuntimeException("Token is invalid or expired");
        }
        Token existingToken = optionalToken.get();
        // Return the user associated with the token
        return existingToken.getUser();

//        Optional<Token> optionalToken = tokenRepository.findByToken(token);
//
//        if(optionalToken.isEmpty()){
//            throw new RuntimeException("Token not found");
//        }
//        Token existingToken = optionalToken.get();
//
//        // Check if the token is expired
//        if(existingToken.getExpiresAt() < System.currentTimeMillis()){
//            throw new RuntimeException("Token is expired");
//        }
//
//        // Check if the token is deleted
//        if(existingToken.getIsDeleted()){
//            throw new RuntimeException("Token is deleted");
//        }
//
//        // Return the user associated with the token
//        User user = existingToken.getUser();
//        if(user == null){
//            throw new RuntimeException("User not found for the token");
//        }
//        return user;
    }

}

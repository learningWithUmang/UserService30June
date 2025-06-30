package dev.umang.userservice_30june.services;


import dev.umang.userservice_30june.models.Token;
import dev.umang.userservice_30june.models.User;
import dev.umang.userservice_30june.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String signup(String name,
                         String email,
                         String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // In a real application, you should encrypt the password before saving it
        // Save the user to the database (this part is not implemented here)

        userRepository.save(user);
        return "User signed up successfully with email: " + email;
    }

    public Token login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }


        if (bCryptPasswordEncoder.matches(password, user.getPassword()) == false) {
            throw new RuntimeException("Invalid email or password");
        }
        //validate the password

        // Generate a token for the user (this part is not implemented here)
        Token token = new Token();
        token.setUser(user);
        token.setToken(RandomStringUtils.randomAlphanumeric(32));
        // Save the token to the database (this part is not implemented here)

        return token;
    }

}

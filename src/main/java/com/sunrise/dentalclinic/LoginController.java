package com.sunrise.dentalclinic;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public String login(@RequestBody User loginUser) {

        Optional<User> user =
                userRepository.findByUsername(loginUser.getUsername());

        if (user.isPresent()
                && user.get().getPassword()
                .equals(loginUser.getPassword())) {

            return "success";
        }

        return "failed";
    }
}
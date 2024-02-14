package com.woolog.service;

import com.woolog.crypto.PasswordEncoder;
import com.woolog.domain.User;
import com.woolog.exception.AlreadyExistsEmailException;
import com.woolog.exception.InvalidSigninInformation;
import com.woolog.repository.UserRepository;
import com.woolog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(Signup signup) {

        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());

        if(userOptional.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());


        User user = User.builder()
                .name(signup.getName())
                .email(signup.getEmail())
                .password(encryptedPassword)
                .build();

        userRepository.save(user);
    }
}

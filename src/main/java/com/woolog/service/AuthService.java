package com.woolog.service;

import com.woolog.crypto.PasswordEncoder;
import com.woolog.domain.Session;
import com.woolog.domain.User;
import com.woolog.exception.AlreadyExistsEmailException;
import com.woolog.exception.InvalidRequest;
import com.woolog.exception.InvalidSigninInformation;
import com.woolog.repository.UserRepository;
import com.woolog.request.Login;
import com.woolog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signin(Login login) {
//        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword()).orElseThrow(InvalidSigninInformation::new);

//        Session session = user.addSession();

        User user = userRepository.findByEmail(login.getEmail()).orElseThrow(InvalidSigninInformation::new);

        boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());

        if(!matches){
            throw new InvalidSigninInformation();
        }

        return user.getId();

    }

    public void signup(Signup signup) {

        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());

        if(userOptional.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrpyt(signup.getPassword());


        User user = User.builder()
                .name(signup.getName())
                .email(signup.getEmail())
                .password(encryptedPassword)
                .build();

        userRepository.save(user);
    }
}

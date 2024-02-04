package com.woolog.service;

import com.woolog.domain.Session;
import com.woolog.domain.User;
import com.woolog.exception.InvalidSigninInformation;
import com.woolog.repository.UserRepository;
import com.woolog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long signin(Login login){
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword()).orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();

        return user.getId();

    }
}

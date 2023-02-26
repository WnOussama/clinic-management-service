package com.nexym.clinic.domain.user;

import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.FormatUtil;
import com.nexym.clinic.utils.exception.AccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtProvider jwtUtils;


    @Override
    public User getUserById(Long userId) {
        return userPersistence.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' does not exist", userId)));
    }

    @Override
    public Long registerUser(User user) {
        var errorList = user.applyValidations();
        if (FormatUtil.isFilled(errorList)) {
            throw new UserValidationException("Failed to validate user request", errorList);
        } else {
            var userEmail = user.getEmail();
            if (userPersistence.existsByEmail(userEmail)) {
                throw new UserValidationException(String.format("User with email '%s' already exists", userEmail));
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userPersistence.registerUser(user);
        }
    }

    @Override
    public List<User> getUserList() {
        return userPersistence.getUserList();
    }

    @Override
    public com.nexym.clinic.domain.user.model.auth.Authentication authenticate(LoginCredential loginCredential,
                                                                               AuthenticationManager authenticationManager) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCredential.getEmail(),
                loginCredential.getPassword(),
                new ArrayList<>()));
        var user = loadUserByUsername(loginCredential.getEmail());
        if (user != null) {
            var token = jwtUtils.generateToken(user);
            var expirationDate = jwtUtils.getExpirationDateFromToken(token);
            var now = new Date();
            return Authentication.builder()
                    .token(token)
                    .expiresIn((expirationDate.getTime() - now.getTime()) / 1000)
                    .build();
        }
        throw new AccessDeniedException("The access to this resource is denied");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.nexym.clinic.domain.user.model.User user = userPersistence.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email '%s' not found", email)));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

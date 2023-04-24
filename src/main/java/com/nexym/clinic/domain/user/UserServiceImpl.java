package com.nexym.clinic.domain.user;

import com.nexym.clinic.config.security.JwtProvider;
import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.model.auth.Authentication;
import com.nexym.clinic.domain.user.model.auth.LoginCredential;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.exception.AccessDeniedException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPersistence userPersistence;

    @Autowired
    private JwtProvider jwtUtils;


    @Override
    public User getUserById(Long userId) {
        return userPersistence.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' does not exist", userId)));
    }

    @Override
    public List<User> getUserList() {
        return userPersistence.getUserList();
    }

    @Override
    public com.nexym.clinic.domain.user.model.auth.Authentication authenticate(LoginCredential loginCredential,
                                                                               AuthenticationManager authenticationManager) {
        var email = loginCredential.getEmail();
        var password = loginCredential.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                password,
                new ArrayList<>()));
        return generateUserAuthentication(email);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userPersistence.getUserByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Access to this resource is denied"));
    }

    @Override
    public Optional<User> findUserByResetToken(String resetToken) {
        return Optional.empty();
    }

    private Authentication generateUserAuthentication(String email) {
        var user = loadUserByUsername(email);
        var token = jwtUtils.generateToken(user);
        var expirationDate = jwtUtils.getExpirationDateFromToken(token);
        var now = new Date();
        return Authentication.builder()
                .id(user.getUserId())
                .token(token)
                .expiresIn((expirationDate.getTime() - now.getTime()) / 1000)
                .build();
    }
}

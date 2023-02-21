package com.nexym.clinic.domain.user;

import com.nexym.clinic.domain.user.exception.UserNotFoundException;
import com.nexym.clinic.domain.user.exception.UserValidationException;
import com.nexym.clinic.domain.user.model.User;
import com.nexym.clinic.domain.user.port.UserPersistence;
import com.nexym.clinic.utils.FormatUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserPersistence userPersistence;


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
            return userPersistence.registerUser(user);
        }
    }
}

package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.UserSignupRequest;
import com.ibank.user.http.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    @Transactional
    void signupShouldFailWhenUsingExistingUsername() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );
        UserSignupRequest signupRequest = new UserSignupRequest(user);

        // when
        userRepository.save(user);

        // then
        assertThrows(UsernameTakenException.class, () -> {
            underTest.registerUser(signupRequest);
        });
    }

    @Test
    @Transactional
    void signupShouldFailWhenUsingExistingEmail() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );
        UserSignupRequest signupRequest = new UserSignupRequest(user);

        // when
        userRepository.save(user);
        signupRequest.username = "demoabc";

        // then
        assertThrows(EmailTakenException.class, () -> {
            underTest.registerUser(signupRequest);
        });
    }

    @Test
    @Transactional
    void updateShouldWork() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        underTest.registerUser(new UserSignupRequest(user));

        // then
        assertDoesNotThrow(() -> {
            User updatedUser = underTest.updateUser(
                "demo",
                "demo2",
                "demo77@gmail.com",
                "1234",
                "123456"
            );
            assertEquals(updatedUser.getEmail(), "demo77@gmail.com");
        });

    }

    @Test
    @Transactional
    void updateShouldFailWhenPasswordsDoesNotMatch() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        underTest.registerUser(new UserSignupRequest(user));

        // then
        // Debe arrojar IllegalState ya que el password actual es 123456 y recibe dummypassword
        assertThrows(IllegalStateException.class, () -> {
            underTest.updateUser(
                "demo",
                "demo",
                "demo@gmail.com",
                "dummypassword",
                "1234"
            );
        });
    }

    @Test
    @Transactional
    void updateShouldFailWhenFormUserIsDifferentThanLogged() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demo",
            "demo7777@gmail.com",
            "1234",
            "123456",
            "1234"
        );

        // when
        underTest.registerUser(new UserSignupRequest(user));

        // then
        // Debe arrojar IllegalState ya que el password actual es 123456 y recibe dummypassword
        assertThrows(IllegalStateException.class, () -> {
            User updatedUser = underTest.updateUser(updateRequest);
            assertEquals(updatedUser.getEmail(), updateRequest.email);
        });

        //log.info(" ...");
    }
}
package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.UserSignupRequest;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    @Transactional
    void signupShouldWork() {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        // then
        assertDoesNotThrow(() -> {
            underTest.createUser(new UserSignupRequest(user));
        });
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

        // when
        userRepository.save(user);

        // then
        assertThrows(UsernameTakenException.class, () -> {
            underTest.createUser(new UserSignupRequest(user));
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

        // when
        userRepository.save(user);

        // then
        assertThrows(EmailTakenException.class, () -> {
            UserSignupRequest signupRequest = new UserSignupRequest(user);
            signupRequest.username = "demoabc";
            underTest.createUser(signupRequest);
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
        // Es importante crear el usuario de esta forma para que el password sea cifrado
        underTest.createUser(new UserSignupRequest(user));

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
        underTest.createUser(new UserSignupRequest(user));

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
}
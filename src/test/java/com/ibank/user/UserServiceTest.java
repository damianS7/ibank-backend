package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.UserSignupRequest;
import com.ibank.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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

        // Usuario por defecto
        userRepository.save(
            new User(
                null,
                "demo",
                "demo@gmail.com",
                PasswordEncoder.encode("1234")
            )
        );
    }

    @Test
    @Transactional
    void signupShouldWork() {
        // given
        User givenUser = new User(
            null,
            "demo1",
            "demo1@gmail.com",
            "1234"
        );

        // Peticion de registro
        UserSignupRequest signupRequest = new UserSignupRequest(givenUser);

        // when
        // Usuario creado
        User userCreated = underTest.createUser(signupRequest);

        // then
        assertThat(userCreated.getId()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @Transactional
    void signupShouldFailWhenUsingExistingUsername() {
        // given
        User givenUser = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // Peticion de registro
        UserSignupRequest signupRequest = new UserSignupRequest(givenUser);

        // then
        assertThrows(UsernameTakenException.class, () -> {
            // when
            underTest.createUser(signupRequest);
        });
    }

    @Test
    @Transactional
    void signupShouldFailWhenUsingExistingEmail() {
        // given
        User givenUser = new User(
            null,
            "demo1",
            "demo@gmail.com",
            "1234"
        );

        // Peticion de registro
        UserSignupRequest signupRequest = new UserSignupRequest(givenUser);

        // then
        assertThrows(EmailTakenException.class, () -> {
            // when
            underTest.createUser(signupRequest);
        });
    }

    @Test
    @Transactional
    void updateShouldWork() {
        // given
        // Ver user de setUp()

        // then
        assertDoesNotThrow(() -> {
            // when
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
        // Ver user de setUp()

        // then
        // Debe arrojar IllegalState ya que el password actual es 1234 y recibe dummypassword
        assertThrows(IllegalStateException.class, () -> {
            // when
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
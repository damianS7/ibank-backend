package com.ibank.account;

import com.github.javafaker.Faker;
import com.ibank.auth.exception.AuthorizationException;
import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Slf4j
class BankingAccountServiceTest {

    @Autowired
    private BankingAccountRepository bankingAccountRepository;

    @Autowired
    private UserRepository userRepository;

    private BankingAccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new BankingAccountService(bankingAccountRepository);
    }

    @Test
    @Transactional
    void shouldCreateBankAccount() {
        // given
        User customer = userRepository.save(new User(null, "demo", "demo@gmail.com", "1234"));

        //when
        BankingAccount bankingAccount = underTest.createAccount(
            customer,
            BankingAccountType.SAVINGS,
            BankingAccountCurrency.EUR
        );

        // then
        assertThat(bankingAccount).isNotNull();
        log.info(bankingAccount.toString());
    }

    @Test
    @Transactional
    void shouldLockBankAccount() {
        // given
        User customer = userRepository.save(new User(null, "demo", "demo@gmail.com", "1234"));
        User admin = userRepository.save(new User(null, "admin", "admin@gmail.com", "1234"));

        // Cambiamos el rol de la cuenta por defecto a ADMIN
        admin.setRole(UserRole.ADMIN);

        BankingAccount bankingAccount = underTest.createAccount(
            customer,
            BankingAccountType.SAVINGS,
            BankingAccountCurrency.EUR
        );

        // when
        underTest.setLockAccount(admin, bankingAccount.getId(), true);

        // then
        assertThat(bankingAccount.isLocked()).isTrue();
        log.info(bankingAccount.toString());
    }

    @Test
    @Transactional
    void shouldFailToLockBankAccount() {
        // given
        User customer = userRepository.save(new User(null, "demo", "demo@gmail.com", "1234"));
        User admin = userRepository.save(new User(null, "admin", "admin@gmail.com", "1234"));
        // Rol de usuario para admin
        admin.setRole(UserRole.USER);

        BankingAccount bankingAccount = underTest.createAccount(
            customer,
            BankingAccountType.SAVINGS,
            BankingAccountCurrency.EUR
        );

        // then
        assertThrows(AuthorizationException.class, () -> {
            // when
            underTest.setLockAccount(admin, bankingAccount.getId(), true);
        });

        log.info(bankingAccount.toString());
    }
}
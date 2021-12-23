package com.ibank.account;

import com.ibank.user.User;
import com.ibank.user.UserRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class BankingAccountRepositoryTest {

    @Autowired
    private BankingAccountRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void customerCanHaveSeveralBankAccounts() {
        // given
        User customer = userRepository.save(new User(null, "demo", "demo@gmail.com", "1234"));

        BankingAccount usdAccount = new BankingAccount(customer, "ES55555555555", BankingAccountType.SAVINGS, BankingAccountCurrency.USD, new Date());
        BankingAccount eurAccount = new BankingAccount(customer, "ES12312312323", BankingAccountType.SAVINGS, BankingAccountCurrency.EUR, new Date());

        //when
        eurAccount = underTest.save(eurAccount);
        usdAccount = underTest.save(usdAccount);

        // then
        assertThat(eurAccount).isNotNull();
        assertThat(usdAccount).isNotNull();
        log.info(eurAccount.toString());
        log.info(usdAccount.toString());
    }

    @Test
    @Transactional
    void ibanFieldShouldBeUnique() {
        // given
        User customer = userRepository.save(new User(null, "demo", "demo@gmail.com", "1234"));

        BankingAccount usdAccount = new BankingAccount(customer, "ES55555555555", BankingAccountType.SAVINGS, BankingAccountCurrency.USD, new Date());
        BankingAccount eurAccount = new BankingAccount(customer, "ES55555555555", BankingAccountType.SAVINGS, BankingAccountCurrency.EUR, new Date());

        //when
        usdAccount = underTest.save(usdAccount);

        // then
        // Debe fallar al intentar guardar un iban duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            underTest.save(eurAccount);
        });

    }
}
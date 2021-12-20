package com.ibank.account;

import com.ibank.account.http.BankingAccountRequest;
import com.ibank.account.http.BankingAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankingAccountService {

    private final BankingAccountRepository accountRepository;

    @Autowired
    public BankingAccountService(BankingAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public BankingAccountResponse create(BankingAccountRequest request) {
        // leer User logged de SecurityContext y validar antes de crear ...
        return new BankingAccountResponse(
            this.createAccount(request.customer_id, request.accountType)
        );
    }

    // private?
    public BankingAccount createAccount(Long customerId, BankingAccountType accountType) {
        //BankingAccount bankingAccount = new BankingAccount(
        //    null, customerId, BankingAccountIBAN.generate(),
        //    0.0, true, accountType
        //);
        //return accountRepository.save(bankingAccount);
        return null;
    }

    public void enableAccount() {

    }

    public void disableAccount() {

    }
}

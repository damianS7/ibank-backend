package com.ibank.account.http;

import com.ibank.account.BankingAccount;
import com.ibank.account.BankingAccountCurrency;
import com.ibank.account.BankingAccountType;
import com.ibank.transaction.BankingAccountTransaction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class BankingAccountResponse {
    // Id de la cuenta
    public Long id;

    // Numero de cuenta
    public String iban;

    // Flag para cuentas bloquedas o no
    public boolean locked;

    // Tipo de cuenta bancaria
    @Enumerated(EnumType.STRING)
    public BankingAccountType type;

    // Moneda de la cuenta
    @Enumerated(EnumType.STRING)
    public BankingAccountCurrency currency;

    // Fecha de creacion de la cuenta
    public Date createdAt;

    public List<BankingAccountTransaction> accountTransactions;

    public BankingAccountResponse(BankingAccount bankingAccount) {
        this.id = bankingAccount.getId();
        this.iban = bankingAccount.getIban();
        this.locked = bankingAccount.isLocked();
        this.type = bankingAccount.getType();
        this.currency = bankingAccount.getCurrency();
        this.createdAt = bankingAccount.getCreatedAt();
        this.accountTransactions = bankingAccount.getAccountTransactions();
    }
}

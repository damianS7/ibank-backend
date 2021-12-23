package com.ibank.account.http;

import com.ibank.account.BankingAccountCurrency;
import com.ibank.account.BankingAccountType;
import lombok.AllArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
public class BankingAccountContractRequest {
    //@NotBlank
    @Enumerated(EnumType.STRING)
    public BankingAccountType accountType;

    //@NotBlank
    @Enumerated(EnumType.STRING)
    public BankingAccountCurrency accountCurrency;
}

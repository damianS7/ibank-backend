package com.ibank.account.http;

import javax.validation.constraints.NotBlank;

public class BankingAccountLockRequest {
    @NotBlank
    public Long accountId;

    @NotBlank
    public boolean lock;
}

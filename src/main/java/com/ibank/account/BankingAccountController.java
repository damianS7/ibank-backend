package com.ibank.account;

import com.ibank.account.http.BankingAccountContractRequest;
import com.ibank.account.http.BankingAccountLockRequest;
import com.ibank.account.http.BankingAccountResponse;
import com.ibank.common.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BankingAccountController {

    private BankingAccountService accountService;

    @Autowired
    public BankingAccountController(BankingAccountService bankingAccountService) {
        this.accountService = bankingAccountService;
    }

    /**
     * Endpoint para la creacion de cuentas bancarias del usuario logeado.
     *
     * @param bankingAccountContractRequest Los datos necesarios para la creacion de la cuenta
     * @return La cuenta creada
     */
    @PostMapping(path = "/api/v1/customer/account/create", consumes = "application/json")
    public BankingAccountResponse createAccount(@Valid @RequestBody BankingAccountContractRequest bankingAccountContractRequest) {
        return new BankingAccountResponse(accountService.createAccount(bankingAccountContractRequest));
    }

    /**
     * Endpoint para el bloqueo/desbloqueo de cuentas bancarias.
     *
     * @param bankingAccountLockRequest La peticion con los datos de la cuenta que se quiere bloquear/desbloquear
     */
    @PostMapping(path = "/api/v1/admin/customer/account/lock", consumes = "application/json")
    public void lockAccount(@Valid @RequestBody BankingAccountLockRequest bankingAccountLockRequest) {
        accountService.setLockAccount(bankingAccountLockRequest);
    }

    //@ExceptionHandler(Throwable.class)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ErrorResponse("Error parsing json from request."));
    }
}

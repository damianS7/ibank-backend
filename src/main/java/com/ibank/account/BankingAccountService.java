package com.ibank.account;

import com.github.javafaker.Faker;
import com.ibank.account.http.BankingAccountContractRequest;
import com.ibank.account.http.BankingAccountLockRequest;
import com.ibank.auth.exception.AuthorizationException;
import com.ibank.user.User;
import com.ibank.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BankingAccountService {

    private final BankingAccountRepository accountRepository;

    @Autowired
    public BankingAccountService(BankingAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * @param request Peticion que contiene los datos de la cuenta bancaria a crear
     * @return BankingAccount la cuenta bancaria creada
     */
    public BankingAccount createAccount(BankingAccountContractRequest request) {
        // Leemos el nombre de usuario desde el token
        User customer = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Si no existe usuario logeado ...
        if (customer == null) {
            throw new AuthorizationException("You must log in.");
        }

        return this.createAccount(customer, request);
    }

    /**
     * @param customer El owner de la cuenta
     * @param request  La peticion que contiene los datos de la cuenta a crear
     * @return La cuenta creada
     */
    BankingAccount createAccount(User customer, BankingAccountContractRequest request) {
        return this.createAccount(customer, request.accountType, request.accountCurrency);
    }

    /**
     * Crea una cuenta bancaria asociada al cliente
     *
     * @param customer        El owner de la cuenta
     * @param accountType     Tipo de cuenta
     * @param accountCurrency Moneda de la cuenta
     * @return Cuenta creada
     */
    BankingAccount createAccount(User customer, BankingAccountType accountType, BankingAccountCurrency accountCurrency) {
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String formatedDate = dateFormat.format(new Date());
        BankingAccount bankingAccount = new BankingAccount(
            customer,
            generateIBAN(),
            accountType,
            accountCurrency,
            new Date()
        );

        return accountRepository.save(bankingAccount);
    }

    /**
     * Este metodo es usado por la administracion para bloquear cuentas.
     *
     * @param request La peticion con el flag para lock/unlock de la cuenta
     */
    public void setLockAccount(BankingAccountLockRequest request) {
        // Obtenemos el usuario logeado que deberia ser un admin
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Si no existe usuario logeado ...
        if (admin == null) {
            throw new AuthorizationException("You must log in.");
        }

        this.setLockAccount(admin, request);
    }

    /**
     * Bloquea/Desbloquea una cuenta de un cliente. El usuario que bloquea
     * debe ser ADMIN.
     *
     * @param admin   El usuario que va a modificar la cuenta
     * @param request La peticion con el flag para lock/unlock de la cuenta
     */
    public void setLockAccount(User admin, BankingAccountLockRequest request) {
        this.setLockAccount(admin, request.accountId, request.lock);
    }

    /**
     * Bloquea/Desbloquea una cuenta de un cliente. El usuario que bloquea
     * debe ser ADMIN.
     *
     * @param admin El usuario que va a modificar la cuenta
     */
    public void setLockAccount(User admin, Long accountId, boolean lock) {
        // Comprobamos que el usuario que intenta modificar la cuenta es ADMIN
        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new AuthorizationException("You must be admin to this.");
        }

        BankingAccount bankingAccount = accountRepository.findById(accountId).orElseThrow();
        bankingAccount.setLocked(lock);
    }

    /**
     * Genera un numero de cuenta
     *
     * @return String con el IBAN de la cuenta
     */
    public String generateIBAN() {
        Faker faker = new Faker();
        return faker.finance().iban("ES");
    }
}

package com.ibank.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibank.transaction.BankingAccountTransaction;
import com.ibank.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "banking_accounts")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankingAccount {
    // Id de la cuenta
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Propietario de la cuenta
    @OneToOne(targetEntity = User.class,
        cascade = CascadeType.MERGE,
        fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private User customer;

    // Numero de cuenta
    @Column(unique = true)
    private String iban;

    // Flag para cuentas bloquedas o no
    private boolean locked = false;

    // Tipo de cuenta bancaria
    @Enumerated(EnumType.STRING)
    private BankingAccountType type;

    // Moneda de la cuenta
    @Enumerated(EnumType.STRING)
    private BankingAccountCurrency currency;

    // getBalance

    // Fecha de creacion de la cuenta
    private Date createdAt;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "ownerAccount",
        orphanRemoval = true
    )
    private List<BankingAccountTransaction> accountTransactions;

    //private List<BankingAccountTransaction> cardTransactions;


    // Constructor para cuentas por defecto
    public BankingAccount(User customer, String IBAN, BankingAccountType type, BankingAccountCurrency currency, Date createAt) {
        this(null,
            customer,
            IBAN,
            false,
            type,
            currency,
            createAt,
            null
        );
    }
}

package com.ibank.account;

import com.ibank.transaction.BankingAccountTransaction;
import com.ibank.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "banking_accounts")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class,
        cascade = CascadeType.MERGE,
        fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    private String iban;

    // Este campo es calculado ! No existe en la BD?
    @Column(precision = 2, scale = 2)
    private BigDecimal balance;

    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private BankingAccountType type;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "bankingAccount",
        orphanRemoval = true
    )
    private List<BankingAccountTransaction> accountTransactions;

    public BankingAccount(Long id, User customer, String iban, double balance, BankingAccountType type) {
        this.id = id;
        this.customer = customer;
        this.iban = iban;
        this.balance = new BigDecimal(balance);
        this.type = type;
        this.enabled = true;
    }
}

package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RiJAS
 */
@Entity
@Table(name="transaction_mode")
@Data
public class TransactionMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_mode_id")
    private Long transactionModeId;

    private String mode;

    private boolean enable;

    @OneToMany(mappedBy = "transactionMode", fetch=FetchType.LAZY)
    private List<CashBook> cashBooks;


}
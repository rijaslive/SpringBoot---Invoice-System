package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RiJAS
 */
@Entity
@Table(name="transaction_type")
@Data
public class TransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_type_id")
    private Long transactionTypeId;

    private String type;

    private boolean enable;

    @OneToMany(mappedBy = "transactionType", fetch=FetchType.LAZY)
    private List<CashBook> cashBooks;


}
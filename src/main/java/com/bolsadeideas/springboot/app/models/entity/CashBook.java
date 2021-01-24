package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author RiJAS
 */
@Entity
@Table(name="cash_book")
@Data
public class CashBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cashBookId;

    private String particular;

    private Double amount;

    private Double discount;

    @Temporal(TemporalType.DATE)
    private Date createdTime;

    @Temporal(TemporalType.DATE)
    private Date updatedDate;

    @Column(unique = true)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_mode_id", nullable = false)
    private TransactionMode transactionMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


/*    @PrePersist
    public void prePersist() {
        createdTime = new Date();
    }*/
}
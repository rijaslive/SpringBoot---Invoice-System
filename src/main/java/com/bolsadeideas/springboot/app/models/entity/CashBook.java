package com.bolsadeideas.springboot.app.models.entity;

import lombok.Data;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * @author RiJAS
 */
@Entity
@Table(name="cash_book")
@Data
public class CashBook {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cashBookId;

    private String particular;

    private Double amount;

    private Double discount;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdTime;

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdTime;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date updatedDate;

    @Column(name = "updatedDate", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updatedDate;

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


    @PrePersist
    public void setCreatedOn() {
        this.setCreatedTime(OffsetDateTime.now());
        this.setUpdatedDate(OffsetDateTime.now());
    }

    @PreUpdate
    public void setUpdatedOn() {
        this.setUpdatedDate(OffsetDateTime.now());
    }

    public String getCreatedDateString(){
        return this.createdTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }
}
package com.bolsadeideas.springboot.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashBookDto {

    private Long cashBookId;
    private Integer index;
    private String item;
    private Double amount;
    private String type;
    private String mode;
    private String user;
    private String date;
}

package com.bolsadeideas.springboot.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashBookItemDto {

    private Long cashBookId;
    private String item;
    private Double amount;
    private Integer type;
    private String typeString;
    private Integer mode;
    private String modeString;
}

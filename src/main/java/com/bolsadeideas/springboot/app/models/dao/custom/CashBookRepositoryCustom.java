package com.bolsadeideas.springboot.app.models.dao.custom;

import com.bolsadeideas.springboot.app.models.entity.CashBook;

import java.time.OffsetDateTime;
import java.util.List;

public interface  CashBookRepositoryCustom {
//    List<CashBook> getCashBookByDateAndTransactionType(OffsetDateTime start, OffsetDateTime end, long type);
//    List<CashBook> getCashBookByDateAndTransactionMode(OffsetDateTime start, OffsetDateTime end, long mode);
    List<CashBook> getCashBookByDateAndTransactionTypeAndTransactionMode(OffsetDateTime start, OffsetDateTime end,long type, long mode);
}

package com.bolsadeideas.springboot.app.service;

import com.bolsadeideas.springboot.app.dto.CashBookDto;
import com.bolsadeideas.springboot.app.dto.CashBookItemDto;
import com.bolsadeideas.springboot.app.models.dao.CashBookRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionModeRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionTypeRepository;
import com.bolsadeideas.springboot.app.models.entity.*;
import com.bolsadeideas.springboot.app.util.DateUtil;
import com.bolsadeideas.springboot.app.util.UserUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.awt.print.Book;
import java.math.BigInteger;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CashBookService {

    @Autowired
    CashBookRepository cashBookRepository;

    @Autowired
    TransactionModeRepository transactionModeRepository;

    @Autowired
    TransactionTypeRepository transactionTypeRepository;

    @Autowired
    UserUtil userUtil;

    @Autowired
    EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<TransactionMode> findAllTransactionModes(){
        return transactionModeRepository.findByEnable(true);
    }

    @Transactional(readOnly = true)
    public List<TransactionType> findAllTransactionTypes(){
      return   transactionTypeRepository.findByEnable(true);
    }

    @Transactional
    public CashBook createCashBookEntry(CashBook cashBook){
        Optional<User> currentUser = userUtil.getCurrentUser();
        cashBook.setUser(currentUser.get());
        if(cashBook.getCashBookId()!=null && cashBook.getCashBookId()!=0){
            CashBook one = cashBookRepository.getOne(cashBook.getCashBookId());
            cashBook.setCreatedTime(one.getCreatedTime());
        }
        return cashBookRepository.save(cashBook);
    }


    @Transactional
    public CashBookDto  createCashBookEntry(CashBookItemDto bookItemDto){
        Optional<User> currentUser = userUtil.getCurrentUser();
        OffsetDateTime now = OffsetDateTime.now();
        Query query = null;
        if(bookItemDto.getCashBookId()==null){
            query =  entityManager.createNativeQuery("INSERT INTO cash_book (particular, amount, created_time, " +
                    "updated_date, transaction_mode_id, transaction_type_id, user_id, deleted) " +
                    "VALUES (?,?,?,?,?,?,?,?) RETURNING cash_book_id");
        }
        query
                .setParameter(1, bookItemDto.getItem())
                .setParameter(2, bookItemDto.getAmount())
                .setParameter(3, now)
                .setParameter(4, now)
                .setParameter(5, bookItemDto.getMode())
                .setParameter(6, bookItemDto.getType())
                .setParameter(7, currentUser.get().getId())
                .setParameter(8, false);

        BigInteger singleResult = (BigInteger) query.getSingleResult();

        CashBookDto cashBookDto = CashBookDto.builder()
                .cashBookId(singleResult.longValue())
                .item(bookItemDto.getItem())
                .amount(bookItemDto.getAmount())
                .mode(bookItemDto.getModeString())
                .type(bookItemDto.getTypeString())
                .date(DateUtil.getDateToString(now))
                .build();

        return cashBookDto;

    }


    @Transactional(readOnly = true)
    public Model fetchAllCashBooks(int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10,  Sort.by("cashBookId").descending());	//Spring Boot
        Page<CashBook> cashBooks = cashBookRepository.findAll(pageRequest);

        model.addAttribute("clients", cashBooks.get().collect(Collectors.toList()));
        model.addAttribute("page", page+1);
        return model;
    }

/*    @Transactional(readOnly = true)
    public List<CashBookDto> fetchAllCashBooksByDate(){
        OffsetDateTime startTime = DateUtil.getOffsetDateTime(LocalDate.now(), LocalTime.of(00, 00));
        OffsetDateTime endTime = DateUtil.getOffsetDateTime(LocalDate.now(), LocalTime.of(23, 53));
        List<CashBook> cashBooks = cashBookRepository.findByUpdatedDateBetweenOrderByUpdatedDateDesc(startTime,endTime);
        if(CollectionUtils.isNotEmpty(cashBooks)){
              return cashBooks.stream().map(cb-> CashBookDto.builder()
                     .cashBookId(cb.getCashBookId())
                     .item(cb.getParticular())
                     .type(cb.getTransactionType().getType())
                     .mode(cb.getTransactionMode().getMode())
                     .amount(cb.getAmount())
                     .user(cb.getUser().getUsername())
                     .date(cb.getCreatedDateString())
                     .build()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }*/

    @Transactional(readOnly = true)
    public Map<String,Object> fetchAllCashBooksByFilter(Map<String,Object> requestObject){

        OffsetDateTime start = DateUtil.getStringToDate(MapUtils.getString(requestObject,"start"));
        OffsetDateTime end = DateUtil.getStringToEndDate(MapUtils.getString(requestObject,"end"));
        Long mode = MapUtils.getLong(requestObject,"transactionMode",0L);
        Long type = MapUtils.getLong(requestObject,"transactionType",0L);
        List<CashBook> cashBooks = cashBookRepository.getCashBookByDateAndTransactionTypeAndTransactionMode(start,end,type,mode );
        if(CollectionUtils.isNotEmpty(cashBooks)){
            Map<String,Object> result = new HashMap<>();
            List<CashBookDto> collect = cashBooks.stream().map(cb -> CashBookDto.builder()
                    .cashBookId(cb.getCashBookId())
                    .item(cb.getParticular())
                    .type(cb.getTransactionType().getType())
                    .mode(cb.getTransactionMode().getMode())
                    .amount(cb.getAmount())
                    .user(cb.getUser().getUsername())
                    .date(cb.getCreatedDateString())
                    .build()).collect(Collectors.toList());

            Double totalIncome = cashBooks.stream().filter(cashbook -> cashbook.getTransactionType().getTransactionTypeId() == 1).collect(Collectors.summingDouble(cashbook -> cashbook.getAmount()));
            Double totalExpense = cashBooks.stream().filter(cashbook -> cashbook.getTransactionType().getTransactionTypeId() == 2).collect(Collectors.summingDouble(cashbook -> cashbook.getAmount()));
            result.put("cashbooks",collect);
            result.put("totalIncome",totalIncome);
            result.put("totalExpense",totalExpense);
            return result;
        }
        return new HashMap<>();
    }

    public Page<CashBookDto> findPaginatedCashBooksByDate(Pageable pageable, LocalDate localDate) {
        OffsetDateTime startTime = DateUtil.getOffsetDateTime(localDate, LocalTime.of(00, 00));
        OffsetDateTime endTime = DateUtil.getOffsetDateTime(localDate, LocalTime.of(23, 53));
        Page<CashBook> currentPage = cashBookRepository.findByCreatedTimeBetweenAndDeleted(startTime, endTime,false, pageable);
        List<CashBookDto> collect = currentPage.stream().map(cb -> CashBookDto.builder()
               .cashBookId(cb.getCashBookId())
               .item(cb.getParticular())
               .type(cb.getTransactionType().getType())
               .mode(cb.getTransactionMode().getMode())
               .amount(cb.getAmount())
               .user(cb.getUser().getUsername())
               .date(cb.getCreatedDateString())
               .build()).collect(Collectors.toList());
        Page<CashBookDto> bookPage
                = new PageImpl(collect, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), currentPage.getTotalElements());

        return bookPage;
    }

    public CashBook getCashBookById(long cashbookId) {
        CashBook one = cashBookRepository.getOne(cashbookId);
        if (one==null) one = new CashBook();
        return one;
    }

    public void deleteCashBookEntry(long cashbookId) {
        CashBook one = cashBookRepository.getOne(cashbookId);
        if (one!=null) {
             one.setDeleted(true);
            cashBookRepository.save(one);
        }
    }
}

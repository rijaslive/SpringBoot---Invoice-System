package com.bolsadeideas.springboot.app.service;

import com.bolsadeideas.springboot.app.dto.CashBookDto;
import com.bolsadeideas.springboot.app.models.dao.CashBookRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionModeRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionTypeRepository;
import com.bolsadeideas.springboot.app.models.entity.*;
import com.bolsadeideas.springboot.app.security.CurrentUser;
import com.bolsadeideas.springboot.app.util.DateUtil;
import com.bolsadeideas.springboot.app.util.UserUtil;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public Model fetchAllCashBooks(int page, Model model){
        Pageable pageRequest = PageRequest.of(page, 10,  Sort.by("cashBookId").descending());	//Spring Boot
        Page<CashBook> cashBooks = cashBookRepository.findAll(pageRequest);

        model.addAttribute("clients", cashBooks.get().collect(Collectors.toList()));
        model.addAttribute("page", page+1);
        return model;
    }

    @Transactional(readOnly = true)
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
}

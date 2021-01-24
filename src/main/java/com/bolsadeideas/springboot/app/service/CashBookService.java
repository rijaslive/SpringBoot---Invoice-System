package com.bolsadeideas.springboot.app.service;

import com.bolsadeideas.springboot.app.models.dao.CashBookRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionModeRepository;
import com.bolsadeideas.springboot.app.models.dao.TransactionTypeRepository;
import com.bolsadeideas.springboot.app.models.entity.CashBook;
import com.bolsadeideas.springboot.app.models.entity.TransactionMode;
import com.bolsadeideas.springboot.app.models.entity.TransactionType;
import com.bolsadeideas.springboot.app.models.entity.User;
import com.bolsadeideas.springboot.app.security.CurrentUser;
import com.bolsadeideas.springboot.app.util.paginator.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return cashBookRepository.save(cashBook);
    }

}

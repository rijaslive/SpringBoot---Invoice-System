package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.CashBook;
import com.bolsadeideas.springboot.app.models.entity.TransactionMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface TransactionModeRepository extends JpaRepository<TransactionMode, Long> {
    List<TransactionMode> findByEnable(boolean isEnabled);
}

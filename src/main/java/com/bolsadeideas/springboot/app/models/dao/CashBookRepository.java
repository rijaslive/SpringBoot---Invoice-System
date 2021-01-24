package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.CashBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashBookRepository extends JpaRepository<CashBook, Long> {
}

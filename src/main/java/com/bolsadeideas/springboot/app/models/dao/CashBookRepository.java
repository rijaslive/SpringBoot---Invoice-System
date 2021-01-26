package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.CashBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface CashBookRepository extends JpaRepository<CashBook, Long> {

   List<CashBook> findByUpdatedDateBetweenOrderByUpdatedDateDesc(OffsetDateTime start,OffsetDateTime end);
   Page<CashBook> findByCreatedTimeBetweenAndDeleted(OffsetDateTime start, OffsetDateTime end,boolean isDeleted, Pageable pageable);
}

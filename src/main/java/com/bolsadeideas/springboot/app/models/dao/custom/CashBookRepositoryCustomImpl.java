package com.bolsadeideas.springboot.app.models.dao.custom;

import com.bolsadeideas.springboot.app.models.entity.CashBook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.print.Book;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CashBookRepositoryCustomImpl implements CashBookRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<CashBook> getCashBookByDateAndTransactionTypeAndTransactionMode(OffsetDateTime start, OffsetDateTime end, long type, long mode) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CashBook> cq = cb.createQuery(CashBook.class);

        Root<CashBook> book = cq.from(CashBook.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.greaterThanOrEqualTo(book.get("createdTime"), start));
        predicates.add(cb.lessThanOrEqualTo(book.get("createdTime"), end));
        predicates.add(cb.equal(book.get("deleted"), false));
        if (type != 0) {
            predicates.add(cb.equal(book.get("transactionType"), type));
        }
        if (mode != 0) {
            predicates.add(cb.equal(book.get("transactionMode"), mode));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(book.get("cashBookId")));
        return entityManager.createQuery(cq).getResultList();
    }

}

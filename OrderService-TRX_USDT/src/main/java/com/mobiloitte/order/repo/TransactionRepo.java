package com.mobiloitte.order.repo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.model.Transaction;

public interface TransactionRepo extends PagingAndSortingRepository<Transaction, Long> {
	List<Transaction> findAll();

	List<Transaction> findByExecutionTimeGreaterThan(Date date);

	Response<List<Order>> findByUserId(Long userId);

	@Query("Select sum(t.quantity) from Transaction t")
	Optional<BigDecimal> sumByQuantity();

	Optional<Transaction> findTopByOrderByTransactionIdDesc();
}

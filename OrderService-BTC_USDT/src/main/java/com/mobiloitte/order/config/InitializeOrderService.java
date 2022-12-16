package com.mobiloitte.order.config;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mobiloitte.order.model.MarketData;
import com.mobiloitte.order.model.Order;
import com.mobiloitte.order.model.Response;
import com.mobiloitte.order.model.Transaction;
import com.mobiloitte.order.service.OrderService;
import com.mobiloitte.order.service.TradeService;
import com.mobiloitte.order.service.TransactionService;

@Configuration
@EnableScheduling
public class InitializeOrderService {

	@Autowired
	private OrderService orderService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private MarketData marketData;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public InitializingBean initializeOrderBook() {
		return () -> {
			marketData.setTotalVolume(transactionService.getTotalVolume());
			Response<List<Order>> orderBookResponse = orderService.getOrderBook(null);
			tradeService.initializeOrderBook(orderBookResponse.getData());
			List<Transaction> transactions = transactionService.getLast24HourTransactions();
			transactions.forEach(t -> {
				marketData.init24HourVolume(t.getQuantity(), t.getExecutionTime().getTime());
				marketData.init24HourPrice(t.getPrice(), t.getExecutionTime().getTime());
			});
			if (marketData.getLastPrice() == null) {
				Optional<Transaction> transaction = transactionService.getLastTransaction();
				if (transaction.isPresent()) {
					marketData.setLastPrice(transaction.get().getPrice());
				}
			}
		};
	}
}

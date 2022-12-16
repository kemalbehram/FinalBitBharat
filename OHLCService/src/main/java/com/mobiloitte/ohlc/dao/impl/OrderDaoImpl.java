package com.mobiloitte.ohlc.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mobiloitte.ohlc.dao.OrderDao;
import com.mobiloitte.ohlc.dto.OrderDetailDto;
import com.mobiloitte.ohlc.dto.SearchAndFilterDto;
import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.enums.OrderStatus;
import com.mobiloitte.ohlc.enums.OrderType;

/**
 * @author Kumar Arjun
 *
 */
@Repository

public class OrderDaoImpl implements OrderDao {

	private static final String YYYY_MM_DD_HH_MM_SS_SSSSSS = "yyyy-MM-dd HH:mm:ss.SSSSSS";

	private static final String USER_ID = "user_id";

	private static final String STOP_PRICE = "stop_price";

	private static final String QUANTITY = "quantity";

	private static final String ORDER_TYPE = "order_type";

	private static final String ORDER_STATUS = "order_status";

	private static final String ORDER_SIDE = "order_side";

	private static final String ORDER_ID = "order_id";

	private static final String LIMIT_PRICE = "limit_price";

	private static final String LAST_EXECUTION_TIME = "last_execution_time";

	private static final String INSTRUMENT = "instrument";

	private static final String EXE_COIN = "exe_coin";

	private static final String CURRENT_QUANTITY = "current_quantity";

	private static final String CREATION_TIME = "creation_time";

	private static final String BLOCKED_BALANCE = "blocked_balance";

	private static final String BASE_COIN = "base_coin";

	private static final String AVG_EXECUTION_PRICE = "avg_execution_price";

	private static final String ACTIVE = "active";

	private static final String TRIGGER_CONDITION = "trigger_condition";

	private static final String AND_ORDER_SIDE = " and order_side=";

	private static final String AND = " and '";

	private static final String AND_EXE_COIN = " and exe_coin='";

	private static final String AND_CREATION_TIME = " and creation_time between '";

	private static final String AND_BASE_COIN = " and base_coin='";

	private static final String DECRYPTED_TEXT = "decryptedText";

	private static final String SELECT_QUERY = "select  user_id , order_id, quantity, current_quantity, blocked_balance, limit_price, avg_execution_price, stop_price, active, order_status, order_side, instrument, order_type, creation_time, last_execution_time, trigger_condition, base_coin, exe_coin from ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> coinPairOrderHistory(Long userId, String table) {
		String query = "select order_id, active, avg_execution_price, blocked_balance, creation_time, current_quantity, instrument, last_execution_time, limit_price, order_side, order_status, order_type, quantity, stop_price, user_id from "
				+ table + " where user_id=" + userId;
		return jdbcTemplate.queryForList(query);
	}

	@Override

	public List<OrderDetailDto> getAlltrade(Long userId, String transaction, String order,
			SearchAndFilterDto searchAndFilterDto) {

		try {
			List<OrderDetailDto> respList = new LinkedList<>();

			StringBuilder query = new StringBuilder(
					"select t.transaction_id,t.executed_order_id, t.executed_user_id,t.execution_time,t.order_id,t.price,t.quantity,t.side,t.user_id FROM ");

			query.append(transaction);
			query.append(" t where (t.user_id=");
			query.append(userId);
			query.append(" or t.executed_user_id=");
			query.append(userId);
			query.append(")");
			if (searchAndFilterDto.getFromDate() != null) {
				query.append(" and t.execution_time between '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
						.format(new Date(searchAndFilterDto.getFromDate())).substring(0, 19) + "'");
			}
			if (searchAndFilterDto.getToDate() != null) {
				query.append(" and '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
						.format(new Date(searchAndFilterDto.getToDate())).substring(0, 19) + "'");
			}
			List<Map<String, Object>> createQuery = jdbcTemplate.queryForList(query.toString());
			createQuery.parallelStream().forEachOrdered(a -> {

				OrderDetailDto dto = new OrderDetailDto();
				dto.setQuantity((BigDecimal) a.get("quantity"));
				dto.setLimitPrice((BigDecimal) a.get("price"));
				dto.setOrderSide(orderSideFromInt((Integer) a.get("side")));
				dto.setLastExecutionTime((Date) a.get("execution_time"));
				dto.setTransactionId((Long) a.get("transaction_id"));
				Object userIds = a.get("user_id");
				Object exeUserId = a.get("executed_user_id");
				Object orderIds = a.get("order_id");
				Object exeOrderIds = a.get("executed_order_id");

				StringBuilder Ordersquery;
				List<Map<String, Object>> createQuerys;
				if (userIds.equals(userId)) {

					dto.setUserId((Long) userIds);
					Ordersquery = new StringBuilder(
							"select t.transaction_id,o.order_status, o.avg_execution_price,o.stop_price, o.order_side, o.instrument, o.order_type, o.current_quantity FROM ");
					Ordersquery.append(transaction);
					Ordersquery.append(" t ");
					Ordersquery.append(" join ");
					Ordersquery.append(order);
					Ordersquery.append(" o on t.order_id=o.order_id ");
					Ordersquery.append("  where o.order_id=");
					Ordersquery.append(orderIds);
					dto.setOrderId((Long) orderIds);
					if (searchAndFilterDto.getBaseCoin() != null) {
						Ordersquery.append(" and o.base_coin='" + searchAndFilterDto.getBaseCoin() + "'");
					}

					if (searchAndFilterDto.getExeCoin() != null) {
						Ordersquery.append(" and o.exe_coin='" + searchAndFilterDto.getExeCoin() + "'");
					}
					if (searchAndFilterDto.getSide() != null) {
						Ordersquery.append(" and o.order_side=" + searchAndFilterDto.getSide().ordinal());
					}
					createQuerys = jdbcTemplate.queryForList(Ordersquery.toString());

					createQuerys.parallelStream().forEachOrdered(d -> {
						dto.setAvgExecutionPrice((BigDecimal) d.get("avg_execution_price"));
						dto.setCurrentQuantity((BigDecimal) d.get("current_quantity"));
						dto.setOrderStatus(orderStatusFromInt((Integer) d.get("order_status")));
						dto.setOrderSide(orderSideFromInt((Integer) d.get("order_side")));
						dto.setInstrument((String) d.get("instrument"));
						dto.setStopPrice((BigDecimal) d.get("stop_price"));
						dto.setOrderType(orderTypeFromInt((Integer) d.get("order_type")));
						dto.setTransactionId((Long) d.get("transaction_id"));

					});
				} else {
					dto.setUserId((Long) exeUserId);

					Ordersquery = new StringBuilder(
							"select t.transaction_id, o.order_status, o.avg_execution_price, o.order_side, o.instrument,o.stop_price, o.order_type, o.current_quantity FROM ");
					// Ordersquery.append(order);
					Ordersquery.append(transaction);
					Ordersquery.append(" t ");
					Ordersquery.append(" join ");
					Ordersquery.append(order);
					Ordersquery.append(" o on t.order_id=o.order_id ");
					Ordersquery.append(" where o.order_id= ");
					Ordersquery.append(exeOrderIds);
					dto.setOrderId((Long) exeOrderIds);
					if (searchAndFilterDto.getBaseCoin() != null) {
						Ordersquery.append(" and base_coin='" + searchAndFilterDto.getBaseCoin() + "'");
					}

					if (searchAndFilterDto.getExeCoin() != null) {
						Ordersquery.append(" and exe_coin='" + searchAndFilterDto.getExeCoin() + "'");
					}
					if (searchAndFilterDto.getSide() != null) {
						Ordersquery.append(" and order_side=" + searchAndFilterDto.getSide().ordinal());
					}
					createQuerys = jdbcTemplate.queryForList(Ordersquery.toString());

					createQuerys.parallelStream().forEachOrdered(d -> {
						dto.setAvgExecutionPrice((BigDecimal) d.get("avg_execution_price"));
						dto.setCurrentQuantity((BigDecimal) d.get("current_quantity"));
						dto.setOrderStatus(orderStatusFromInt((Integer) d.get("order_status")));
						dto.setOrderSide(orderSideFromInt((Integer) d.get("order_side")));
						dto.setInstrument((String) d.get("instrument"));
						dto.setStopPrice((BigDecimal) d.get("stop_price"));
						dto.setOrderType(orderTypeFromInt((Integer) d.get("order_type")));
						dto.setTransactionId((Long) a.get("transaction_id"));

					});
				}
				if (!createQuerys.isEmpty()) {
					respList.add(dto);
				}
			});

			return respList;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	private static final Map<Integer, OrderStatus> intToTypeOrderStatus = new HashMap<>();
	static {
		for (OrderStatus type : OrderStatus.values()) {
			intToTypeOrderStatus.put(type.ordinal(), type);
		}
	}

	public static OrderStatus orderStatusFromInt(Integer i) {
		return intToTypeOrderStatus.get(i);
	}

	private static final Map<Integer, OrderSide> intToTypeOrderSide = new HashMap<>();
	static {
		for (OrderSide type : OrderSide.values()) {
			intToTypeOrderSide.put(type.ordinal(), type);
		}
	}

	public static OrderSide orderSideFromInt(Integer i) {
		return intToTypeOrderSide.get(i);
	}

	private static final Map<Integer, OrderType> intToTypeOrderType = new HashMap<>();
	static {
		for (OrderType type : OrderType.values()) {
			intToTypeOrderType.put(type.ordinal(), type);
		}
	}

	public static OrderType orderTypeFromInt(Integer i) {
		return intToTypeOrderType.get(i);
	}

	@Override
	public List<OrderDetailDto> activeOrderHistory(Long userId, String table, SearchAndFilterDto searchAndFiltersDto) {
		try {

			StringBuilder query = new StringBuilder(SELECT_QUERY);

			query.append(table);
			query.append(" where order_status  in (0,1,2,8) and user_id=");
			query.append(userId);

			if (searchAndFiltersDto.getBaseCoin() != null) {
				query.append(" and base_coin ='" + searchAndFiltersDto.getBaseCoin() + "'");
			}

			if (searchAndFiltersDto.getExeCoin() != null) {
				query.append(" and exe_coin ='" + searchAndFiltersDto.getExeCoin() + "'");
			}

			if (searchAndFiltersDto.getSide() != null) {
				query.append(" and order_side =" + searchAndFiltersDto.getSide().ordinal());
			}

			if (searchAndFiltersDto.getFromDate() != null) {
				query.append("  and creation_time between '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getFromDate())).substring(0, 19) + "'");
			}
			if (searchAndFiltersDto.getToDate() != null) {
				query.append(" and  '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getToDate())).substring(0, 19) + "'");
			}

			List<Map<String, Object>> createQuery = jdbcTemplate.queryForList(query.toString());
			List<OrderDetailDto> respList = new LinkedList<>();
			createQuery.parallelStream().forEachOrdered(d -> {
				OrderDetailDto dto = new OrderDetailDto();
				dto.setUserId((Long) d.get(USER_ID));
				dto.setOrderId((Long) d.get(ORDER_ID));
				dto.setQuantity((BigDecimal) d.get(QUANTITY));
				dto.setCurrentQuantity((BigDecimal) d.get(CURRENT_QUANTITY));
				dto.setBlockedBalance((BigDecimal) d.get(BLOCKED_BALANCE));
				dto.setAvgExecutionPrice((BigDecimal) d.get(AVG_EXECUTION_PRICE));
				dto.setLimitPrice((BigDecimal) d.get(LIMIT_PRICE));
				dto.setStopPrice((BigDecimal) d.get(STOP_PRICE));
				dto.setActive((Boolean) d.get(ACTIVE));
				dto.setOrderStatus(orderStatusFromInt((Integer) d.get(ORDER_STATUS)));
				dto.setOrderSide(orderSideFromInt((Integer) d.get(ORDER_SIDE)));
				dto.setInstrument((String) d.get(INSTRUMENT));
				dto.setOrderType(orderTypeFromInt((Integer) d.get(ORDER_TYPE)));
				dto.setCreationTime((Date) d.get(CREATION_TIME));
				dto.setLastExecutionTime((Date) d.get(LAST_EXECUTION_TIME));
				dto.setBaseCoin((String) d.get(BASE_COIN));
				dto.setExeCoin((String) d.get(EXE_COIN));
				dto.setTriggerCondition((Boolean) d.get(TRIGGER_CONDITION));
				respList.add(dto);
			});
			return respList;

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Object OrderHistory(Long userId, String table, SearchAndFilterDto searchAndFiltersDto) {
		try {

			StringBuilder query = new StringBuilder(SELECT_QUERY);

			query.append(table);
			query.append(" where order_status  in (2,3,4,8,5) and user_id=");
			query.append(userId);

			if (searchAndFiltersDto.getBaseCoin() != null) {
				query.append(" and base_coin ='" + searchAndFiltersDto.getBaseCoin() + "'");
			}

			if (searchAndFiltersDto.getExeCoin() != null) {
				query.append(" and exe_coin ='" + searchAndFiltersDto.getExeCoin() + "'");
			}

			if (searchAndFiltersDto.getSide() != null) {
				query.append(" and order_side =" + searchAndFiltersDto.getSide().ordinal());
			}

			if (searchAndFiltersDto.getFromDate() != null) {
				query.append("  and creation_time between '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getFromDate())).substring(0, 19) + "'");
			}
			if (searchAndFiltersDto.getToDate() != null) {
				query.append(" and  '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getToDate())).substring(0, 19) + "'");
			}

			List<Map<String, Object>> createQuery = jdbcTemplate.queryForList(query.toString());
			List<OrderDetailDto> respList = new LinkedList<>();
			createQuery.parallelStream().forEachOrdered(d -> {
				OrderDetailDto dto = new OrderDetailDto();
				dto.setUserId((Long) d.get(USER_ID));
				dto.setOrderId((Long) d.get(ORDER_ID));
				dto.setQuantity((BigDecimal) d.get(QUANTITY));
				dto.setCurrentQuantity((BigDecimal) d.get(CURRENT_QUANTITY));
				dto.setBlockedBalance((BigDecimal) d.get(BLOCKED_BALANCE));
				dto.setAvgExecutionPrice((BigDecimal) d.get(AVG_EXECUTION_PRICE));
				dto.setLimitPrice((BigDecimal) d.get(LIMIT_PRICE));
				dto.setStopPrice((BigDecimal) d.get(STOP_PRICE));
				dto.setActive((Boolean) d.get(ACTIVE));
				dto.setOrderStatus(orderStatusFromInt((Integer) d.get(ORDER_STATUS)));
				dto.setOrderSide(orderSideFromInt((Integer) d.get(ORDER_SIDE)));
				dto.setInstrument((String) d.get(INSTRUMENT));
				dto.setOrderType(orderTypeFromInt((Integer) d.get(ORDER_TYPE)));
				dto.setCreationTime((Date) d.get(CREATION_TIME));
				dto.setLastExecutionTime((Date) d.get(LAST_EXECUTION_TIME));
				dto.setBaseCoin((String) d.get(BASE_COIN));
				dto.setExeCoin((String) d.get(EXE_COIN));
				dto.setTriggerCondition((Boolean) d.get(TRIGGER_CONDITION));
				respList.add(dto);
			});
			return respList;

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public Object getAlltradeForAllUser(String transaction, String order, SearchAndFilterDto searchAndFiltersDto) {
		try {
			List<OrderDetailDto> respList = new LinkedList<>();

			StringBuilder query = new StringBuilder(
					"select t.transaction_id,t.executed_order_id, t.executed_user_id,t.execution_time,t.order_id,t.price,t.quantity,t.side,t.user_id ,o.base_coin ,o.exe_coin, o.instrument FROM ");
			query.append(transaction);
			query.append(" t ");
			query.append(" join ");
			query.append(order);
			query.append(" o on t.order_id=o.order_id ");

			if (searchAndFiltersDto.getBaseCoin() != null) {
				query.append(" and base_coin ='" + searchAndFiltersDto.getBaseCoin() + "'");
			}

			if (searchAndFiltersDto.getExeCoin() != null) {
				query.append(" and exe_coin ='" + searchAndFiltersDto.getExeCoin() + "'");
			}

			if (searchAndFiltersDto.getSide() != null) {
				query.append(" and order_side =" + searchAndFiltersDto.getSide().ordinal());
			}

			if (searchAndFiltersDto.getFromDate() != null) {
				query.append("  and execution_time between '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getFromDate())).substring(0, 19) + "'");
			}
			if (searchAndFiltersDto.getToDate() != null) {
				query.append(" and  '" + new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSSSSS)
						.format(new Date(searchAndFiltersDto.getToDate())).substring(0, 19) + "'");
			}

			List<Map<String, Object>> createQuery = jdbcTemplate.queryForList(query.toString());
			createQuery.parallelStream().forEachOrdered(a -> {
				OrderDetailDto dto = new OrderDetailDto();
				dto.setInstrument((String) a.get("instrument"));
				dto.setTransactionId((Long) a.get("transaction_id"));
				dto.setOrderId((Long) a.get("order_id"));
				dto.setOrderSide(orderSideFromInt((Integer) a.get("side")));
				dto.setOrderType(orderTypeFromInt((Integer) a.get("order_type")));
				dto.setBaseCoin((String) a.get("exe_coin"));
				dto.setExeCoin((String) a.get("base_coin"));

				respList.add(dto);
			});
			return respList;
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

}

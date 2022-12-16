/*
 * package com.mobiloitte.p2p.content.utils;
 * 
 * import java.math.BigDecimal; import java.util.Arrays; import java.util.List;
 * 
 * import org.joda.time.DateTime; import org.joda.time.Minutes; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.scheduling.annotation.EnableScheduling; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Component;
 * 
 * import com.mobiloitte.p2p.content.dao.CreateAdvertisementDao; import
 * com.mobiloitte.p2p.content.dao.EscrowDao; import
 * com.mobiloitte.p2p.content.dao.TradingDao; import
 * com.mobiloitte.p2p.content.dto.GetBalanceResponseDto; import
 * com.mobiloitte.p2p.content.enums.TradeStatus; import
 * com.mobiloitte.p2p.content.feign.WalletClient; import
 * com.mobiloitte.p2p.content.model.Escrow; import
 * com.mobiloitte.p2p.content.model.Response; import
 * com.mobiloitte.p2p.content.model.Trading;
 * 
 * @Component
 * 
 * @Configuration
 * 
 * @EnableScheduling public class SchedulingConfigurer {
 * 
 * @Autowired CreateAdvertisementDao createAdvertisementDao;
 * 
 * @Autowired WalletClient walletClient;
 * 
 * @Autowired TradingDao tradingDao;
 * 
 * @Autowired EscrowDao escrowDao;
 * 
 * @Scheduled(fixedRate = 500000) public void scheduleTaskWithFixedRate() {
 * 
 * List<Trading> list =
 * tradingDao.findByTradeStatusIn(Arrays.asList(TradeStatus.PENDING,
 * TradeStatus.PAID)); if (!list.isEmpty()) { for (Trading p2pAdvertisement :
 * list) { DateTime createdTime = new
 * DateTime(p2pAdvertisement.getCreationTime()); DateTime currentTime = new
 * DateTime();
 * 
 * int paymentWindow = p2pAdvertisement.getPaymentWindow();
 * //System.err.println(createdTime);
 * 
 * //System.err.println(currentTime); int timeDiff =
 * Minutes.minutesBetween(createdTime, currentTime).getMinutes();
 * 
 * //System.err.println(timeDiff);
 * 
 * if (paymentWindow < timeDiff) { List<Escrow> list1 =
 * escrowDao.findByTradeStatus(TradeStatus.PENDING); if (!list1.isEmpty()) { for
 * (Escrow escrow : list1) { Response<GetBalanceResponseDto> value3 =
 * walletClient .getBalance(p2pAdvertisement.getCryptoCoin(),
 * p2pAdvertisement.getFkUserId());
 * 
 * BigDecimal walletBallance = value3.getData().getWalletBalance();
 * 
 * BigDecimal blockedBallance = value3.getData().getBlockedBalance(); BigDecimal
 * returnBallance = walletBallance.add(blockedBallance);
 * escrow.setBlockedBalance(BigDecimal.ZERO);
 * escrow.setWalletBalance(returnBallance);
 * walletClient.updateWallet(returnBallance, p2pAdvertisement.getFkUserId(),
 * p2pAdvertisement.getCryptoCoin());
 * p2pAdvertisement.setTradeStatus(TradeStatus.CANCEL);
 * tradingDao.save(p2pAdvertisement);
 * //System.out.println("Ballance Return SuccessFully"); escrowDao.save(escrow);
 * 
 * } }
 * 
 * List<Trading> list2 = tradingDao.findByTradeStatus(TradeStatus.PAID); if
 * (!list2.isEmpty()) { for (Trading trade : list2) {
 * trade.setTradeStatus(TradeStatus.DISPUTE); trade.setUpdationTime(new Date());
 * tradingDao.save(trade); } }
 * 
 * 
 * } }
 * 
 * } } }
 */
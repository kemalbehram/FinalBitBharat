/*
 * package com.mobiloitte.microservice.wallet.utils;
 * 
 * import java.sql.Timestamp; import java.text.ParseException; import
 * java.text.SimpleDateFormat; import java.time.Instant; import
 * java.util.Calendar; import java.util.Date; import java.util.List; import
 * java.util.Optional; import java.util.TimeZone;
 * 
 * import javax.transaction.Transactional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Component;
 * 
 * import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao; import
 * com.mobiloitte.microservice.wallet.dao.UserToAdminTransferDao; import
 * com.mobiloitte.microservice.wallet.dao.WalletDao; import
 * com.mobiloitte.microservice.wallet.entities.StorageDetail; import
 * com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer; import
 * com.mobiloitte.microservice.wallet.entities.Wallet; import
 * com.mobiloitte.microservice.wallet.model.Response;
 * 
 * import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao; import
 * com.mobiloitte.microservice.wallet.dao.UserToAdminTransferDao; import
 * com.mobiloitte.microservice.wallet.dao.WalletDao; import
 * com.mobiloitte.microservice.wallet.entities.StorageDetail; import
 * com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer; import
 * com.mobiloitte.microservice.wallet.entities.Wallet; import
 * com.mobiloitte.microservice.wallet.model.Response;
 * 
 * @Transactional
 * 
 * @Component public class AdminToUserTransfer {
 * 
 * @Autowired private StorageDetailsDao storageDetailsDao;
 * 
 * @Autowired private UserToAdminTransferDao userToAdminTransferDao;
 * 
 * @Autowired private WalletDao walletDao;
 * 
 * @SuppressWarnings("deprecation")
 * 
 * @Scheduled(fixedRate = 1000) public void transfer() {
 * List<UserToAdminTransfer> pendingRequest =
 * userToAdminTransferDao.findByStatus("PENDING"); final Date date1 = new
 * Date(); final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz"; final
 * SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT); final TimeZone utc =
 * TimeZone.getTimeZone("UTC"); sdf.setTimeZone(utc);
 * 
 * // Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")); if
 * (!pendingRequest.isEmpty()) {
 * 
 * pendingRequest.parallelStream().forEachOrdered(a -> {
 * 
 * try { SimpleDateFormat sdf123 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 * Timestamp ts = new Timestamp(a.getCreationTime().getTime()); Date date = new
 * Date(ts.getTime());
 * 
 * Date sdf12 = new
 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf123.format(date)); Calendar
 * cal12 = Calendar.getInstance(); cal12.setTime(sdf12); String newDate123 =
 * sdf123.format(cal12.getTime()); Date day =
 * converter(Integer.parseInt(a.getValidityDay().toString()), newDate123);
 * 
 * 
 * 
 * // if (new Date().getMinutes() - day.getMinutes() == 0) { if (new
 * Date().getTime() == day.getTime() || new Date().getTime() > day.getTime()) {
 * adminToUser1ReturnTransfer(a.getUserToAdminTransferId());
 * 
 * System.err.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
 * 
 * } else {
 * System.err.println("noooooooooooooooooooooooooooooooooooooooooooooo"); }
 * 
 * } catch (ParseException e) { e.printStackTrace(); }
 * 
 * }); } }
 * 
 * @Scheduled(fixedRate = 3000) public List<UserToAdminTransfer> ss() {
 * List<UserToAdminTransfer> pendingRequest =
 * userToAdminTransferDao.findByStatus("PENDING"); return pendingRequest;
 * 
 * }
 * 
 * public static Date converter(int validity, String date) throws ParseException
 * {
 * 
 * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Date sdf
 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date); Calendar cal =
 * Calendar.getInstance(); cal.setTime(sdf); cal.add(Calendar.DAY_OF_MONTH,
 * validity); String newDate = sdf1.format(cal.getTime()); Date date1 = new
 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newDate);
 * System.err.println(date); return date1;
 * 
 * }
 * 
 * public Response<Object> adminToUser1ReturnTransfer(Long referenceId) {
 * 
 * try {
 * 
 * Optional<UserToAdminTransfer> findAmountToSend = userToAdminTransferDao
 * .findByUserToAdminTransferId(referenceId);
 * 
 * if (findAmountToSend.isPresent() &&
 * findAmountToSend.get().getStatus().equals("PENDING")) {
 * Optional<StorageDetail> adminStorageDetails = storageDetailsDao
 * .findByCoinTypeAndStorageType(findAmountToSend.get().getBaseCoin(), "HOT");
 * if (adminStorageDetails.isPresent()) {
 * 
 * Optional<Wallet> UserUserWalletCheck = walletDao.findByCoinNameAndFkUserId(
 * findAmountToSend.get().getBaseCoin(), findAmountToSend.get().getUser1Id());
 * if (UserUserWalletCheck.isPresent()) {
 * 
 * UserUserWalletCheck.get().setWalletBalance(
 * UserUserWalletCheck.get().getWalletBalance().add(findAmountToSend.get().
 * getAmount())); walletDao.save(UserUserWalletCheck.get());
 * adminStorageDetails.get().setHotWalletBalance(adminStorageDetails.get().
 * getHotWalletBalance() .subtract(findAmountToSend.get().getAmount()));
 * storageDetailsDao.save(adminStorageDetails.get());
 * findAmountToSend.get().setStatus("CANCEL");
 * userToAdminTransferDao.save(findAmountToSend.get()); return new
 * Response<>(200, "Amount sent to successfully"); } else { return new
 * Response<>(201, "TO_USER_ID_WALLET_NOT_FOUND"); } } else { return new
 * Response<>(201, "NO_STORAGE_DETAILS_FOUND"); } } else { return new
 * Response<>(201, "REFERENCEID_DETAILS_FOUND");
 * 
 * }
 * 
 * } catch (Exception e) { System.err.println(e); return null; } } }
 */
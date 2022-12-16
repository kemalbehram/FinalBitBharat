package com.mobiloitte.microservice.wallet.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.dto.AdminBankDto;
import com.mobiloitte.microservice.wallet.dto.AdminUpiDto;
import com.mobiloitte.microservice.wallet.dto.CoinStatusDto;
import com.mobiloitte.microservice.wallet.dto.DepositInrDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.CoinManagementService;
import com.mobiloitte.microservice.wallet.service.WalletManagementService;

@RestController
@RequestMapping(value = "/admin")
public class AdminWalletController {

	@Autowired
	CoinManagementService coinManagementService;

	@Autowired
	private WalletManagementService walletManagementService;

	@GetMapping("/dashboard/get-deposit-and-coin-count")
	Response<Map<String, Object>> getDepositAndCoinCount() {
		return coinManagementService.getDepositeAndCoinCoin();

	}

	@PostMapping("/approvedBy-user1")
	Response<Object> adminToUser2Transfer(@RequestHeader Long userId, @RequestParam Long referenceId) {
		return walletManagementService.adminToUser2TransferByAdmin(referenceId);
	}

	@GetMapping("/request-cancel-ESCROW")
	public Response<Object> requestCancel(@RequestHeader Long userId, @RequestParam Long userToAdminTransferId) {

		return walletManagementService.requestCancel(userId, userToAdminTransferId);
	}

	@GetMapping("/get-coin-status")
	Response<Object> getCoinStatus(@RequestParam(required = false) String coin) {

		return coinManagementService.getCoinStatus(coin);

	}

	@GetMapping("get-escrow-list")
	public Response<Object> getEscrowList(@RequestHeader Long userId, @RequestParam Integer page,
			@RequestParam Integer pageSize) {
		return walletManagementService.getEscrowList(page, pageSize);
	}

	@PostMapping("/set-coin-status")
	Response<Object> setCoinStatus(@RequestParam String coin, @RequestBody CoinStatusDto coinStatusDto) {
		return coinManagementService.setCoinStatus(coin, coinStatusDto);
	}

	@GetMapping("/get-user-to-admin-send-amount-details")
	Response<List<UserToAdminTransfer>> getUserToAdminSendAmountDetails(@RequestParam(required = false) Long page,
			@RequestParam(required = false) Long pageSize) {
		return walletManagementService.getUserToAdminSendAmountDetails(page, pageSize);
	}

	@GetMapping("/get-last-transaction-date")
	public Response<Date> getUserLastTransactionDate(@RequestParam Long userId) {
		return walletManagementService.getLastTransactionOfUser(userId);
	}

	@GetMapping("/get-internal-transfer-history-admin")
	public Response<Object> getInternaltransferHistory(@RequestHeader Long adminId) {
		return walletManagementService.getInternaltransferHistory();

	}

	@PostMapping("/admin/set-XINDIA-price-inUsd")
	Response<Coin> setAVTPriceInUsd(@RequestParam String coin, @RequestParam BigDecimal priseInUsd) {
		return coinManagementService.setAVTPriceInUsd(coin, priseInUsd);
	}

	@GetMapping("/admin/get-XINDIA-price-inUsd")
	Response<Map<String, BigDecimal>> getAVTPriceInUsd(@RequestParam String coin) {
		return coinManagementService.getAVTPriceInUsd(coin);
	}

	@GetMapping("/Deposi-Inr-List-admin")
	public Response<Object> getListDepositAdmin(@RequestParam(required = false) String utrNo,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) FiatStatus fiatStatus) {
		return walletManagementService.getListDepositAdmin(utrNo, page, pageSize, fiatStatus);
	}

	@GetMapping("/withdraw-Inr-List-admin")
	public Response<Object> getListwithdrawAdmin(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) FiatStatus fiatStatus) {
		return walletManagementService.getListwithdrawAdmin(page, pageSize, fiatStatus);
	}

	@PostMapping("/add-Amount")
	public Response<Object> addDeposit(@RequestBody DepositInrDto depositInrDto, @RequestHeader Long userId) {
		return walletManagementService.addDeposit(depositInrDto, userId);
	}

	@PostMapping("/update-deposit-withdraw-fee")
	public Response<Object> addDeposit(@RequestParam(required = false) BigDecimal deposite,
			@RequestParam(required = false) BigDecimal withdraw, @RequestHeader Long userId) {
		return walletManagementService.addDepositFee(deposite, withdraw, userId);
	}

	@PostMapping("/update-minimum-deposit-withdraw-fee")
	public Response<Object> addMinimumDeposit(@RequestParam(required = false) BigDecimal minimumdeposite,
			@RequestParam(required = false) BigDecimal minimumwithdraw, @RequestHeader Long userId) {
		return walletManagementService.addMinimumDeposit(minimumdeposite, minimumwithdraw, userId);
	}

	@PostMapping("/add-Bank-details")
	public Response<Object> addbank(@RequestBody AdminBankDto adminBankDto) {
		return walletManagementService.addbank(adminBankDto);
	}

	@PostMapping("/bank-status")
	public Response<Object> addBankStatus(@RequestParam Boolean isVisible, @RequestParam Long bankId) {
		return walletManagementService.addBankStatus(isVisible, bankId);
	}

	@PostMapping("/upi-status")
	public Response<Object> addUpiStatus(@RequestParam Boolean isVisible, @RequestParam String upiId) {
		return walletManagementService.addUpiStatus(isVisible, upiId);
	}

	@PostMapping("/add-Upi-details")
	public Response<Object> addUpi(@RequestBody AdminUpiDto adminUpiDto) {
		return walletManagementService.addUpi(adminUpiDto);
	}

	@PostMapping("/Deposit-Inr-Status")
	public Response<Object> changestatus(@RequestParam(required = false) FiatStatus fiatStatus,
			@RequestParam(required = false) String utrNo, @RequestHeader Long userId) {
		return walletManagementService.changestatus(fiatStatus, utrNo, userId);
	}

	@PostMapping("/withdraw-Inr-Status")
	public Response<Object> changestatuswithdraw(@RequestParam(required = false) FiatStatus fiatStatus,
			@RequestHeader Long userId, @RequestParam(required = false) Long userId1, @RequestParam BigDecimal amount) {
		return walletManagementService.changestatuswithdraw(fiatStatus, userId, userId1, amount);
	}

	@GetMapping("/upi-List-user")
	public Response<Object> listUpiuser() {
		return walletManagementService.listUpiuser();
	}

	@GetMapping("/bank-List-user")
	public Response<Object> bankListuser() {
		return walletManagementService.bankListuser();
	}

	@GetMapping("/upi-List-admin")
	public Response<Object> listUpi() {
		return walletManagementService.listUpi();
	}

	@GetMapping("/bank-List-admin")
	public Response<Object> bankList() {
		return walletManagementService.bankList();
	}

	@GetMapping("/admin-Fund-Amount")
	public Response<Object> getAmount() {
		return walletManagementService.getAmount();
	}

	@GetMapping("/upi-list-view")
	public Response<Object> getView(@RequestParam Long userId, @RequestParam String upiId) {
		return walletManagementService.getView(userId, upiId);
	}

	@GetMapping("/User-Upi-List")
	public Response<Object> getFullList(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String name,
			@RequestParam(required = false) String upiId) {
		return walletManagementService.getFullList(page, pageSize, name, upiId);
	}

	@GetMapping("/fiat-deposit-view")
	public Response<Object> getdepositview(@RequestParam Long fiatid) {
		return walletManagementService.getdepositview(fiatid);
	}

	@GetMapping("/fiat-withdarw-view")
	public Response<Object> getwithdarwList(@RequestParam Long fiatid) {
		return walletManagementService.getwithdarwList(fiatid);
	}
	
}

package com.mobiloitte.microservice.wallet.dto;

import java.math.BigDecimal;

public class NomineeComisionFeeDto {
	
	private BigDecimal nomineeComission;

	public BigDecimal getNomineeComission() {
		return nomineeComission;
	}

	public void setNomineeComission(BigDecimal nomineeComission) {
		this.nomineeComission = nomineeComission;
	}
	
	
}

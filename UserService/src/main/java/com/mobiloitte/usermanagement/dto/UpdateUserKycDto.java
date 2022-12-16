package com.mobiloitte.usermanagement.dto;

public class UpdateUserKycDto {

    private Long userId;

    private Double usdAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(Double usdAmount) {
        this.usdAmount = usdAmount;
    }

}
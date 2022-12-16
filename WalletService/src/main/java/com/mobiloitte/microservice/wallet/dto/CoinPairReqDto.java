package com.mobiloitte.microservice.wallet.dto;

public class CoinPairReqDto {
private String baseCoin;

private String executableCoin;

private Boolean visible;

public String getBaseCoin() {
return baseCoin;
}

public void setBaseCoin(String baseCoin) {
this.baseCoin = baseCoin;
}

public String getExecutableCoin() {
return executableCoin;
}

public void setExecutableCoin(String executableCoin) {
this.executableCoin = executableCoin;
}

public Boolean getVisible() {
return visible;
}

public void setVisible(Boolean visible) {
this.visible = visible;
}




}
package com.mobiloitte.microservice.wallet.dto;

public class Keystore {
private int version;
private String id;
private String address;
private Crypto crypto;

public int getVersion() {
return version;
}

public void setVersion(int version) {
this.version = version;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public Crypto getCrypto() {
return crypto;
}

public void setCrypto(Crypto crypto) {
this.crypto = crypto;
}

}
package com.mobiloitte.microservice.wallet.dto;

public class Crypto {
private String ciphertext;
private Cipherparams cipherparams;
private String cipher;
private String kdf;
private Kdfparams kdfparams;
private String mac;


public String getCiphertext() {
return ciphertext;
}
public void setCiphertext(String ciphertext) {
this.ciphertext = ciphertext;
}
public Cipherparams getCipherparams() {
return cipherparams;
}
public void setCipherparams(Cipherparams cipherparams) {
this.cipherparams = cipherparams;
}
public String getCipher() {
return cipher;
}
public void setCipher(String cipher) {
this.cipher = cipher;
}
public String getKdf() {
return kdf;
}
public void setKdf(String kdf) {
this.kdf = kdf;
}
public Kdfparams getKdfparams() {
return kdfparams;
}
public void setKdfparams(Kdfparams kdfparams) {
this.kdfparams = kdfparams;
}
public String getMac() {
return mac;
}
public void setMac(String mac) {
this.mac = mac;
}


}
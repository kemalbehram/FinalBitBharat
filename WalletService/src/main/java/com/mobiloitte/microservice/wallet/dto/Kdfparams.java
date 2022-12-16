package com.mobiloitte.microservice.wallet.dto;

public class Kdfparams {

private int dklen;
private String salt;
private int n;
private int r;
private int p;

public int getDklen() {
return dklen;
}

public void setDklen(int dklen) {
this.dklen = dklen;
}

public String getSalt() {
return salt;
}

public void setSalt(String salt) {
this.salt = salt;
}

public int getN() {
return n;
}

public void setN(int n) {
this.n = n;
}

public int getR() {
return r;
}

public void setR(int r) {
this.r = r;
}

public int getP() {
return p;
}

public void setP(int p) {
this.p = p;
}

}
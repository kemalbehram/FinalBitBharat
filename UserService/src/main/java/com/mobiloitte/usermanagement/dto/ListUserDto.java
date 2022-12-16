package com.mobiloitte.usermanagement.dto;

public class ListUserDto {
private Long userId;
private String email;
private String userName;

public Long getUserId() {
return userId;
}

public void setUserId(Long userId) {
this.userId = userId;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

}
package com.SecurityAuthConfig.Dto;

public class LoginMobileOTPDto {
    private String mobile;
	private Integer otp;
    public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getOtp() {
		return otp;
	}
	public void setOtp(Integer otp) {
		this.otp = otp;
	}

}
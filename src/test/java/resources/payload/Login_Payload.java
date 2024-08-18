package resources.payload;

import pojo.RequestPojo.Login_Pojo;
import pojo.RequestPojo.MobileLoginOTP_pojo;
import utilities.GetProperty;

public class Login_Payload {

    public Login_Pojo loginPayload() {
        Login_Pojo loginpojo = new Login_Pojo();
        loginpojo.setUsername(GetProperty.value("login_username"));
        loginpojo.setPassword(GetProperty.value("login_password"));
        return loginpojo;
    }

    public MobileLoginOTP_pojo loginMobilegetOTPPayload() {
        MobileLoginOTP_pojo mobileOTPpojo = new MobileLoginOTP_pojo();
        mobileOTPpojo.setMobileNumber(GetProperty.value("appLoginOTP_mobileNumber"));
        mobileOTPpojo.setAttributeId(GetProperty.value("appLoginOTP_mobile_attributeId"));
        return  mobileOTPpojo;
    }

    public MobileLoginOTP_pojo.MobileLoginFirebase_pojo loginMobileCreateFirebaseyload() {
        MobileLoginOTP_pojo.MobileLoginFirebase_pojo mobileFirebasepojo = new MobileLoginOTP_pojo.MobileLoginFirebase_pojo();
        mobileFirebasepojo.setUserId(GetProperty.value("appLoginFirebase_userId"));
        mobileFirebasepojo.setDeviceId(GetProperty.value("appLoginFirebase_deviceId"));
        mobileFirebasepojo.setDeviceName(GetProperty.value("appLoginFirebase_deviceName"));
        mobileFirebasepojo.setDeviceType(GetProperty.value("appLoginFirebase_deviceType"));
        mobileFirebasepojo.setFirebaseToken(GetProperty.value("appLoginFirebase_firebaseToken"));
        mobileFirebasepojo.setActive("1");
        return mobileFirebasepojo;
    }
}
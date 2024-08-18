package pojo.RequestPojo;

public class MobileLoginOTP_pojo {

    //for generate OTP
    String mobileNumber, attributeId;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }



    //for create Firebase token
    public static class MobileLoginFirebase_pojo {
       private String userId, deviceId, deviceName, deviceType, firebaseToken, active;

        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
        public String getDeviceId() {
            return deviceId;
        }
        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }
        public String getDeviceName() {
            return deviceName;
        }
        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
        public String getDeviceType() {
            return deviceType;
        }
        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }
        public String getFirebaseToken() {
            return firebaseToken;
        }
        public void setFirebaseToken(String firebaseToken) {
            this.firebaseToken = firebaseToken;
        }
        public String getActive() {
            return active;
        }
        public void setActive(String active) {
            this.active = active;
        }
    }

}

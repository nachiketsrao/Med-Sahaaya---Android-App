package studio.knowhere.bloodbank.Activity;

// this is a class that always contains all the API links.
// these APIs can be changed from other classes and we can fetch the API links by calling the
// getter methods of this class.

public class APIs {

    // this is the public IPV4 address of my current EC2 instance: 3.144.108.202 ; port number is 3002


    public String LOGINAPI = "http://3.144.108.202:3002/user/login";
    public String REGISTERAPI = "http://3.144.108.202:3002/user";
    public String USERLIST = "http://3.144.108.202:3002/user";
    public String ADDDONAR = "http://3.144.108.202:3002/donor";
    public String PROFILE = "http://3.144.108.202:3002/user?user_id=";
    public String DonarComm = "http://3.144.108.202:3002/donor/byuser/";
    public String AvailabilityStatus = "http://3.144.108.202:3002/user/availablity/";
    public String PlasmaAvailabilityStatus = "http://3.144.108.202:3002/user/plasma/";



    public String getLOGINAPI() {
        return LOGINAPI;
    }

    public void setLOGINAPI(String LOGINAPI) {
        this.LOGINAPI = LOGINAPI;
    }

    public String getREGISTERAPI() {
        return REGISTERAPI;
    }

    public void setREGISTERAPI(String REGISTERAPI) {
        this.REGISTERAPI = REGISTERAPI;
    }

    public String getUSERLIST() {
        return USERLIST;
    }

    public void setUSERLIST(String USERLIST) {
        this.USERLIST = USERLIST;
    }

    public String getADDDONAR() {
        return ADDDONAR;
    }

    public void setADDDONAR(String ADDDONAR) {
        this.ADDDONAR = ADDDONAR;
    }

    public String getPROFILE() {
        return PROFILE;
    }

    public void setPROFILE(String PROFILE) {
        this.PROFILE = PROFILE;
    }

    public String getDonarComm() {
        return DonarComm;
    }

    public void setDonarComm(String donarComm) {
        DonarComm = donarComm;
    }

    public String getAvailabilityStatus() {
        return AvailabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        AvailabilityStatus = availabilityStatus;
    }

    public String getPlasmaAvailabilityStatus() {
        return PlasmaAvailabilityStatus;
    }

    public void setPlasmaAvailabilityStatus(String plasmaAvailabilityStatus) {
        PlasmaAvailabilityStatus = plasmaAvailabilityStatus;
    }
}

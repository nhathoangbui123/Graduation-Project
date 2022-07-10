package com.example.testrestapi;

public class InfoCardview {
    protected String name;
    protected int type;
    private String flagName;
    private String param;
    private String date;
    private String Lat,Long;
    private String E;
    public InfoCardview(String name, int type) {
        this.name =name;
        this.type =type;
    }
//    public InfoCardview(String date, String Lat, String Long, int type) {
//        this.date =date;
//        this.type =type;
//        this.Lat=Lat;
//        this.Long=Long;
//    }
    public InfoCardview(String name, String flagName, int type) {
        this.name =name;
        this.type =type;
        this.flagName=flagName;
    }
    public InfoCardview(String name, String flagName, int type, String param) {
        this.name =name;
        this.type =type;
        this.flagName=flagName;
        this.param=param;
    }
    public String getDate() {
        return date;
    }
    public String getLat(){return Lat;}
    public String getLong(){return Long;}
    public String getParameter() {
        return param;
    }
    public int getType() {
        return type;
    }
    public String getFlagName() {
        return flagName;
    }
}

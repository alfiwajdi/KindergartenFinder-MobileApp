package com.example.pencaritadika;

public class model
{
  String name,address,phone,latlng;

    public model() {
    }

    public model(String name, String address, String phone, String latlng) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}

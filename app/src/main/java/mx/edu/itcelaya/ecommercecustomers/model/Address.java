package mx.edu.itcelaya.ecommercecustomers.model;

/**
 * Created by niluxer on 5/25/16.
 */
public class Address {
    String first_name;
    String last_name;
    String address_1;
    String city;
    String state;
    String postcode;
    String country;

    public Address(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Address(String first_name, String last_name, String address_1, String city, String state, String postcode, String country) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address_1 = address_1;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.country = country;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

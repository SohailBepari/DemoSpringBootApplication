package com.example.demo.model;

import lombok.*;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @NotNull
    private String houseNumber;

    @NotNull
    private String streetName;

    @NotNull
    private String locality;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    @Pattern(regexp ="^[0-9]{6}$")
    private String zipCode;

    public Address(Address address){
        this.houseNumber = address.getHouseNumber();
        this.streetName = address.getStreetName();
        this.locality = address.getLocality();
        this.city = address.getCity();
        this.state = address.getState();
        this.zipCode = address.getZipCode();
    }

    @Override
    public String toString() {
        return "{" +
                "houseNumber='" + houseNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", locality='" + locality + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}

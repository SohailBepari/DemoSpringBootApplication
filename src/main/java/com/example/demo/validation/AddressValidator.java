package com.example.demo.validation;

import com.example.demo.model.Address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Objects;

public class AddressValidator implements ConstraintValidator<AddressConstraint, Address> {
    @Override
    public void initialize(AddressConstraint address) {
        ConstraintValidator.super.initialize(address);
    }

    @Override
    public boolean isValid(Address address, ConstraintValidatorContext cxt) {
        if(!checkValidAddress(address))
            return false;
        return numberOfAddressCharacters(address) <= 30;
    }

    public Integer numberOfAddressCharacters(Address address){
        return
                address.getHouseNumber().length() +
                address.getStreetName().length() +
                address.getLocality().length() +
                address.getCity().length() +
                address.getState().length() +
                address.getZipCode().length();
    }

    public boolean checkValidAddress(Address address){
        return address != null && address.getHouseNumber() != null && address.getStreetName() != null &&
                address.getLocality() != null && address.getCity() != null && address.getState() != null &&
                address.getZipCode() != null;
    }
}

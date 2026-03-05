package org.s1024.hotelbooking.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter

public class Address {
    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
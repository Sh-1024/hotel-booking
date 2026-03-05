package org.s1024.hotelbooking.dto;

public record AddressDto(
        Integer houseNumber,
        String street,
        String city,
        String country,
        String postCode
) {}
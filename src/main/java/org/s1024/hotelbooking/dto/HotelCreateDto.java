package org.s1024.hotelbooking.dto;

public record HotelCreateDto(
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime
) {}
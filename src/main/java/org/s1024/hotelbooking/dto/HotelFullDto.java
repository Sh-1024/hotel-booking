package org.s1024.hotelbooking.dto;

import java.util.Set;

public record HotelFullDto(
        Long id,
        String name,
        String description,
        String brand,
        AddressDto address,
        ContactsDto contacts,
        ArrivalTimeDto arrivalTime,
        Set<String> amenities
) {}
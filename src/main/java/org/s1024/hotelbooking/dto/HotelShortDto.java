package org.s1024.hotelbooking.dto;

public record HotelShortDto(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {}
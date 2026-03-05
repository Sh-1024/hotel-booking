package org.s1024.hotelbooking.mapper;

import org.s1024.hotelbooking.dto.*;
import org.s1024.hotelbooking.entity.Address;
import org.s1024.hotelbooking.entity.ArrivalTime;
import org.s1024.hotelbooking.entity.Contacts;
import org.s1024.hotelbooking.entity.Hotel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class HotelMapper {

    public HotelShortDto toShortDto(Hotel hotel) {
        String addressString = "";
        if (hotel.getAddress() != null) {
            Address a = hotel.getAddress();

            String streetPart = (a.getHouseNumber() != null ? a.getHouseNumber() + " " : "") +
                    (a.getStreet() != null ? a.getStreet() : "");
            addressString = Stream.of(
                    streetPart.trim().isEmpty() ? null : streetPart.trim(),
                    a.getCity(),
                    a.getPostCode(),
                    a.getCountry()
            ).filter(Objects::nonNull).filter(s -> !s.isEmpty()).collect(Collectors.joining(", "));

        }
        String phone = hotel.getContacts() != null ? hotel.getContacts().getPhone() : null;

        return new HotelShortDto(hotel.getId(), hotel.getName(), hotel.getDescription(), addressString, phone);
    }

    public HotelFullDto toFullDto(Hotel hotel) {

        return new HotelFullDto(
                hotel.getId(), hotel.getName(), hotel.getDescription(), hotel.getBrand(),
                mapAddress(hotel.getAddress()), mapContacts(hotel.getContacts()),
                mapArrival(hotel.getArrivalTime()), hotel.getAmenities()
        );
    }

    public Hotel toEntity(HotelCreateDto dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.name());
        hotel.setDescription(dto.description());
        hotel.setBrand(dto.brand());

        if (dto.address() != null) {
            Address a = new Address();
            a.setHouseNumber(dto.address().houseNumber());
            a.setStreet(dto.address().street());
            a.setCity(dto.address().city());
            a.setCountry(dto.address().country());
            a.setPostCode(dto.address().postCode());
            hotel.setAddress(a);
        }

        if (dto.contacts() != null) {
            Contacts c = new Contacts();
            c.setPhone(dto.contacts().phone());
            c.setEmail(dto.contacts().email());
            hotel.setContacts(c);
        }

        if (dto.arrivalTime() != null) {
            ArrivalTime at = new ArrivalTime();
            at.setCheckIn(dto.arrivalTime().checkIn());
            at.setCheckOut(dto.arrivalTime().checkOut());
            hotel.setArrivalTime(at);
        }

        return hotel;
    }

    private AddressDto mapAddress(Address a) {
        return a == null ? null : new AddressDto(a.getHouseNumber(), a.getStreet(), a.getCity(), a.getCountry(), a.getPostCode());
    }

    private ContactsDto mapContacts(Contacts c) {
        return c == null ? null : new ContactsDto(c.getPhone(), c.getEmail());
    }

    private ArrivalTimeDto mapArrival(ArrivalTime a) {
        return a == null ? null : new ArrivalTimeDto(a.getCheckIn(), a.getCheckOut());
    }
}
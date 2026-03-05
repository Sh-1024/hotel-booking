package org.s1024.hotelbooking.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.s1024.hotelbooking.dto.HotelCreateDto;
import org.s1024.hotelbooking.dto.HotelFullDto;
import org.s1024.hotelbooking.dto.HotelShortDto;
import org.s1024.hotelbooking.entity.Hotel;
import org.s1024.hotelbooking.mapper.HotelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.s1024.hotelbooking.repository.HotelRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional(readOnly = true)
    public List<HotelShortDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelFullDto getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(hotelMapper::toFullDto)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<HotelShortDto> searchHotels(String name, String brand, String city, String country, String amenities) {
        Specification<Hotel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));

            if (brand != null) predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));

            if (city != null) predicates.add(cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase()));

            if (country != null) predicates.add(cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase()));

            if (amenities != null) {
                Join<Hotel, String> amenitiesJoin = root.join("amenities");
                predicates.add(cb.equal(cb.lower(amenitiesJoin), amenities.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Transactional
    public HotelShortDto createHotel(HotelCreateDto dto) {
        Hotel saved = hotelRepository.save(hotelMapper.toEntity(dto));

        return hotelMapper.toShortDto(saved);
    }

    @Transactional
    public HotelFullDto addAmenities(Long id, Set<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with id: " + id));

        if (amenities != null) {
            hotel.getAmenities().addAll(amenities);
        }

        return hotelMapper.toFullDto(hotelRepository.save(hotel));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(String param) {
        List<Object[]> results = switch (param.toLowerCase()) {
            case "brand" -> hotelRepository.countByBrand();
            case "city" -> hotelRepository.countByCity();
            case "country" -> hotelRepository.countByCountry();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid histogram parameter. Allowed: brand, city, country, amenities");
        };

        return results.stream()
                .filter(row -> row[0] != null)
                .collect(Collectors.toMap(
                        row -> String.valueOf(row[0]),
                        row -> ((Number) row[1]).longValue()
                ));
    }
}
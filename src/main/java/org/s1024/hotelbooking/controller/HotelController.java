package org.s1024.hotelbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.s1024.hotelbooking.dto.HotelCreateDto;
import org.s1024.hotelbooking.dto.HotelFullDto;
import org.s1024.hotelbooking.dto.HotelShortDto;
import org.s1024.hotelbooking.service.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public List<HotelShortDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public HotelFullDto getHotelById(@PathVariable("id") Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/search")
    public List<HotelShortDto> searchHotels(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "amenities", required = false) String amenities) {
        return hotelService.searchHotels(name, brand, city, country, amenities);
    }

    @PostMapping("/hotels")
    public HotelShortDto createHotel(@RequestBody HotelCreateDto dto) {
        return hotelService.createHotel(dto);
    }

    @PostMapping("/hotels/{id}/amenities")
    public HotelFullDto addAmenities(@PathVariable("id") Long id, @RequestBody Set<String> amenities) {
        return hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable("param") String param) {
        return hotelService.getHistogram(param);
    }
}
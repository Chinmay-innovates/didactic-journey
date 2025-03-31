package com.salon.controller;

import com.salon.domain.BookingStatus;
import com.salon.dto.*;
import com.salon.mapper.BookingMapper;
import com.salon.model.Booking;
import com.salon.model.SalonReport;
import com.salon.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long salonId,
            @RequestBody BookingRequest bookingRequest
    ) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salonId);
        salonDTO.setOpeningTime(LocalTime.of(9, 0));  // 9:00 AM
        salonDTO.setClosingTime(LocalTime.of(21, 0)); // 9:00 PM
        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setPrice(399);
        serviceDTO.setDuration(45);
        serviceDTO.setName("Hair cut for men");
        serviceDTOSet.add(serviceDTO);

        Booking booking = bookingService.createBooking(
                bookingRequest,
                userDTO,
                salonDTO,
                serviceDTOSet
        );
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer() {
        List<Booking> bookings = bookingService.getBookingsByCustomerId(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingsBySalon() {
        List<Booking> bookings = bookingService.getBookingsBySalonId(1L);

        return ResponseEntity.ok(getBookingDTOs(bookings));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long bookingId
    ) throws Exception {
        Booking bookings = bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
    ) throws Exception {
        Booking bookings = bookingService.updateBookingStatus(bookingId, status);

        return ResponseEntity.ok(BookingMapper.toDTO(bookings));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlots(
            @PathVariable Long salonId,
            @RequestParam(required = false) LocalDateTime date
    ) throws Exception {
        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);
        List<BookingSlotDTO> slotDTOS = bookings.stream().map(booking -> {
            BookingSlotDTO slotDTO = new BookingSlotDTO();
            slotDTO.setStartTime(booking.getStartTime());
            slotDTO.setEndTime(booking.getEndTime());
            return slotDTO;
        }).toList();

        return ResponseEntity.ok(slotDTOS);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(

    ) throws Exception {
        SalonReport salonReport = bookingService.getSalonReport(1L);

        return ResponseEntity.ok(salonReport);
    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDTO).collect(Collectors.toSet());
    }
}

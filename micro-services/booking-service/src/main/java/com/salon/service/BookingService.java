package com.salon.service;

import com.salon.domain.BookingStatus;
import com.salon.dto.BookingRequest;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.dto.UserDTO;
import com.salon.model.Booking;
import com.salon.model.SalonReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(
            BookingRequest bookingRequest,
            UserDTO userDTO,
            SalonDTO salonDTO,
            Set<ServiceDTO> serviceDTOSet
    ) throws Exception;

    List<Booking> getBookingsByCustomerId(Long customerId);

    List<Booking> getBookingsBySalonId(Long salonId);

    Booking getBookingById(Long id) throws Exception;

    Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception;

    List<Booking> getBookingsByDate(LocalDateTime date, Long salonId);

    SalonReport getSalonReport(Long salonId);
}

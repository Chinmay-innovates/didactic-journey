package com.salon.mapper;

import com.salon.dto.BookingDTO;
import com.salon.model.Booking;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setServiceIds(booking.getServiceIds());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        return bookingDTO;
    }
}

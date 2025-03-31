package com.salon.service.impl;

import com.salon.domain.BookingStatus;
import com.salon.dto.BookingRequest;
import com.salon.dto.SalonDTO;
import com.salon.dto.ServiceDTO;
import com.salon.dto.UserDTO;
import com.salon.model.Booking;
import com.salon.model.SalonReport;
import com.salon.repository.BookingRepository;
import com.salon.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking, UserDTO userDTO, SalonDTO salonDTO, Set<ServiceDTO> serviceDTOSet) throws Exception {

        int totalDuration = serviceDTOSet.stream().mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = booking.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);
        isTimeSlotAvailable(salonDTO, bookingStartTime, bookingEndTime);

        int totalPrice = serviceDTOSet.stream().mapToInt(ServiceDTO::getPrice).sum();

        Set<Long> serviceDTOs = serviceDTOSet.stream().map(ServiceDTO::getId).collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(salonDTO.getId());
        newBooking.setServiceIds(serviceDTOs);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    private void isTimeSlotAvailable(SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings = bookingRepository.findBySalonId(salonDTO.getId());
        LocalDateTime salonOpenTime = salonDTO.getOpeningTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getClosingTime().atDate(bookingEndTime.toLocalDate());

        if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time must be within salon's working hours");
        }

        for (Booking booking : existingBookings) {
            LocalDateTime existingBookingStartTime = booking.getStartTime();
            LocalDateTime existingBookingEndTime = booking.getEndTime();

            if (bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)) {
                throw new Exception("Slot not available, please choose different time");
            }

            if (bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)) {
                throw new Exception("Slot not available, please choose different time");
            }
        }
    }

    @Override
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalonId(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        return booking;
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDateTime date, Long salonId) {
        List<Booking> allBookings = getBookingsBySalonId(salonId);
        if (date == null) return allBookings;

        return allBookings.stream().filter(
                booking -> isSameDate(booking.getStartTime(), date) ||
                        isSameDate(booking.getEndTime(), date)
        ).toList();
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDateTime date) {
        return dateTime.toLocalDate().equals(date.toLocalDate());
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = getBookingsBySalonId(salonId);
        int totalEarnings = bookings.stream().mapToInt(Booking::getTotalPrice).sum();
        Integer totalBookings = bookings.size();
        List<Booking> cancelledBookings = bookings.stream().filter(
                booking -> booking.getStatus().equals(BookingStatus.CANCELLED)
        ).toList();
        Double totalRefund = cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setSalonId(salonId);
//        salonReport.setSalonName(salonReport.getSalonName());
        salonReport.setCancelledBookings(cancelledBookings.size());
        salonReport.setTotalBookings(totalBookings);
        salonReport.setTotalEarnings(totalEarnings);
        salonReport.setTotalRefund(totalRefund);
        return salonReport;
    }
}

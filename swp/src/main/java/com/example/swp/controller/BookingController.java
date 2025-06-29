package com.example.swp.controller;

import com.example.swp.entity.Booking;
import com.example.swp.entity.User;
import com.example.swp.service.BookingService;
import com.example.swp.service.EmailService;
import com.example.swp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @GetMapping("/manage-booking")
    public String manageBooking(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date bookingDateTo,
            Model model,
            HttpSession session
    ) {
        String notification = (String) session.getAttribute("notification");
        if (notification != null && !notification.isEmpty()) {
            model.addAttribute("notification", notification);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (bookingDateFrom == null) bookingDateFrom = new Date();
        if (bookingDateTo == null) bookingDateTo = new Date();
        List<Booking> listOfPage = bookingService.searchBookings(status, bookingDateFrom, bookingDateTo);
        model.addAttribute("listOfPage", listOfPage);
        model.addAttribute("status", status);
        String historyUrl = "/manage-booking";
        historyUrl += "?bookingDateFrom=" + dateFormat.format(bookingDateFrom);
        model.addAttribute("bookingDateFrom", dateFormat.format(bookingDateFrom));
        historyUrl += "&bookingDateTo=" + dateFormat.format(bookingDateTo);
        model.addAttribute("bookingDateTo", dateFormat.format(bookingDateTo));
        historyUrl += "&status=" + (status != null ? status : "");
        session.setAttribute("historyUrl", historyUrl);
        session.setAttribute("p", 3);
        return "receptionist/manage-booking";
    }

    @PostMapping("/update-booking")
    public String updateBookingInfo(
            @RequestParam(value = "bookingId", required = false) Integer bookingId,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String typeSubmit,
            HttpSession session
    ) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        Integer userId = (Integer) session.getAttribute("userId");
        User updatedUser = userService.findByUserId(userId);
        if (booking != null) {
            if (status.equals("Canceled")) {
                emailService.sendSimpleMail(booking.getEmail(), "Đăng ký lịch thất bại", "Xin chào!\n Chúng tôi chưa thể xác thực thông tin lịch bạn đăng ký. Hãy đăng ký lại!");
                bookingService.delete(booking);
                session.setAttribute("notification", "Từ chối lịch hẹn thành công.");
            } else if (status.equals("Approved")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (typeSubmit != null && !typeSubmit.equals("btn")) {
                    booking.setStatus(status);
                    booking.setLastUpdatedTime(new Date());
                    booking.setPatientName(patientName != null ? patientName : booking.getPatientName());
                    booking.setDob(dob != null ? dob : booking.getDob());
                    booking.setPhoneNumber(phoneNumber != null ? phoneNumber : booking.getPhoneNumber());
                    booking.setEmail(email != null ? email : booking.getEmail());
                    booking.setAddress(address != null ? address : booking.getAddress());
                    booking.setNote(note);
                }

                booking.setStatus(status);
                booking.setLastUpdatedTime(new Date());
                booking.setUpdatedUser(updatedUser);
                // Format ngày hẹn

                String time = booking.getSlot().getStartTime().toString();
                // Gửi email xác nhận
                emailService.sendSimpleMail(
                        booking.getEmail(),
                        "Đăng ký lịch thành công",
                        "Xin chào!\nBạn đã đăng ký dịch vụ " + booking.getService().getServiceName() +
                                " thành công.\nHẹn gặp bạn tại DentalCare vào " + booking.getAppointmentDate() + ", lúc " + time + "."
                );

                // Lưu lại thông tin booking
                bookingService.save(booking);
            } else { // completed
                booking.setStatus(status);
                booking.setLastUpdatedTime(new Date());
                bookingService.save(booking);
                booking.setUpdatedUser(updatedUser);
                emailService.sendSimpleMail(
                        booking.getEmail(),
                        "Hoàn tất dịch vụ",
                        "Xin chào!\n Cảm ơn bạn đã sử dụng dịch vụ " + booking.getService().getServiceName() +
                                " của chúng tôi.\nNếu cần hỗ trợ khác, hãy liên hệ với chung tôi"
                );
            }
            session.setAttribute("notification", "Cập nhật thành công. Kết quả xác nhận đã gửi tới email: " + booking.getEmail());
        }

        return "redirect:" + session.getAttribute("historyUrl");
    }

    @GetMapping("/booking-history")
    public String getBookingHistory(
            @RequestParam(required = false) String status,
            Model model,
            HttpSession session
    ) {
        Integer userId = (Integer) session.getAttribute("userId");
        User u = userService.findByUserId(userId);
        List<Booking> bookings = bookingService.searchByPhoneNumber(u.getPhoneNumber());
        // Nếu có truyền status và không phải "All", thì lọc theo status
        if (status != null && !status.equalsIgnoreCase("All")) {
            String finalStatus = status;
            bookings = bookings.stream()
                    .filter(b -> finalStatus.equalsIgnoreCase(b.getStatus()))
                    .collect(Collectors.toList());
        }
        if (status == null) status = "All";
        model.addAttribute("bookings", bookings);
        model.addAttribute("status", status); // nếu bạn dùng để set lại filter trên giao diện
        session.setAttribute("historyUrl", "/booking-history?status=" + status);
        return "booking-history";
    }

    @PostMapping("/delete-booking")
    public String deleteBookingInfo(@RequestParam(value = "bookingId", required = false) Integer bookingId, HttpSession session) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        if (booking != null) {
            bookingService.delete(booking);
            session.setAttribute("notification", "Hủy lịch hẹn thành công.");
        }
        return "redirect:" + session.getAttribute("historyUrl");
    }

}

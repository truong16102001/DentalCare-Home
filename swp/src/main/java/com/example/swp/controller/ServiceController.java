package com.example.swp.controller;

import com.example.swp.DTO.ServiceBookingInfoDTO;
import com.example.swp.entity.Booking;
import com.example.swp.entity.Service;
import com.example.swp.entity.Slot;
import com.example.swp.entity.User;
import com.example.swp.service.BookingService;
import com.example.swp.service.DentalCareService;
import com.example.swp.service.SlotService;
import com.example.swp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class ServiceController {
    @Autowired
    DentalCareService dentalCareService;
    @Autowired
    SlotService slotService;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    @GetMapping("/service")
    public String getServices(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int index,
            Model model,
            HttpSession session
    ) {
        // Lấy danh sách dịch vụ từ service layer, có thể thêm lọc, tìm kiếm, sắp xếp
        List<Service> list = dentalCareService.getServices(key, value, type, index);

        int total = dentalCareService.getTotalPages(key); // Tính số trang tổng

        model.addAttribute("list", list);
        model.addAttribute("pageIndex", index);
        model.addAttribute("endPage", total);

        // Truyền lại các thông số tìm kiếm/sắp xếp để sử dụng trong template
        model.addAttribute("key", key);
        model.addAttribute("value", value);
        model.addAttribute("type", type);
        model.addAttribute("historyKey", key != null ? "&key=" + key : "");
        model.addAttribute("historyValue", value != null ? "&value=" + value : "");
        model.addAttribute("historyType", type != null ? "&type=" + type : "");
        model.addAttribute("active", 2);
        return "service"; // Tên file thymeleaf ở src/main/resources/templates/service/list.html
    }

    @GetMapping("/service-details")
    public String getServiceDetails(
            @RequestParam("id") int id,
            Model model
    ) {
        Service service = dentalCareService.findById(id);
        List<Service> relatedServices = dentalCareService.getRelatedServices(id);

        // Gửi dữ liệu đến view
        model.addAttribute("service", service);
        model.addAttribute("relatedServices", relatedServices);

        return "service-details";
    }

    @GetMapping("/service-booking")
    public String getBookingService(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "step", required = false) Integer step,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String note,
            HttpSession session,
            Model model
    ) {
        ServiceBookingInfoDTO dto = new ServiceBookingInfoDTO();

        if(step != 2){
            Service service = dentalCareService.findById(id);
            ServiceBookingInfoDTO bookingInfoDTO = (ServiceBookingInfoDTO) session.getAttribute("bookingInfo");
            if(bookingInfoDTO != null){
                session.setAttribute("bookingInfo", bookingInfoDTO);
            }else{
                dto.setFullName((fullName != null && !fullName.trim().isEmpty()) ? fullName : "");
                dto.setEmail((email != null && !email.trim().isEmpty()) ? email : "");
                dto.setPhone((phone != null && !phone.trim().isEmpty()) ? phone : "");
                dto.setAddress((address != null && !address.trim().isEmpty()) ? address : "");
                dto.setNote((note != null && !note.trim().isEmpty()) ? note : "");
                session.setAttribute("bookingInfo", dto);
            }
            // Gửi dữ liệu đến view
            session.setAttribute("service", service);
            return "service-booking-step1";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        List<Booking> bookings;
        User user = null;
        if(userId != null){
            user = userService.findByUserId(userId);
        }

        if(user != null){
            bookings = dentalCareService.getBookingServicesByUserId(user.getUserId());
        }else{
            bookings = dentalCareService.getBookingServicesByPhoneNumber(phone);
        }
        List<Slot> slots = slotService.findAll();
        model.addAttribute("slots", slots);
        model.addAttribute("bookings", bookings);
        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPhone(phone);
        dto.setAddress(address);
        dto.setNote(note);
        session.setAttribute("bookingInfo", dto);
        return "service-booking-step2";
    }

    @PostMapping("/service-booking")
    public String saveBookingService(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "step", required = false) Integer step,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String note,
            @RequestParam(value = "bookingDate") LocalDate appointmentDate,
            @RequestParam(value = "timeSlot") Integer timeSlotId,
            HttpSession session,
            Model model
    ) {
        // Lấy service từ session
        Service service = (Service) session.getAttribute("service");

        // Lấy thông tin người dùng từ session (giả định đã login)
        Integer userId = (Integer) session.getAttribute("userId");
        User patient = null;
        if(userId != null){
            patient = userService.findByUserId(userId);
        }

        // Lấy Slot từ DB
        Slot slot = slotService.findById(timeSlotId).orElse(null);

        Booking booking = Booking.builder()
                .service(service)
                .patient(patient)
                .patientName(fullName)
                .phoneNumber(phone)
                .email(email)
                .address(address)
                .slot(slot)
                .appointmentDate(appointmentDate)
                .note(note)
                .status("Pending")
                .lastUpdatedTime(new Date())
                .registeredTime(new Date())
                .build();

        bookingService.save(booking);
        session.setAttribute("booking", booking);
        session.removeAttribute("service");
        session.removeAttribute("bookingInfo");
        return "redirect:/booking-success";
    }

    @GetMapping("/booking-success")
    public String getBookingSuccess(
            Model model,
            HttpSession session
    ) {
        model.addAttribute("booking", (Booking) session.getAttribute("booking"));
        session.removeAttribute("booking");
        return "booking-success";
    }
}

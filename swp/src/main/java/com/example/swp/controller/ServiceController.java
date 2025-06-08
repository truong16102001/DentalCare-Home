package com.example.swp.controller;

import com.example.swp.DTO.ServiceBookingInfoDTO;
import com.example.swp.entity.Booking;
import com.example.swp.entity.Service;
import com.example.swp.entity.Slot;
import com.example.swp.entity.User;
import com.example.swp.service.DentalCareService;
import com.example.swp.service.SlotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ServiceController {
    @Autowired
    DentalCareService dentalCareService;
    @Autowired
    SlotService slotService;

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
            // Gửi dữ liệu đến view
            session.setAttribute("service", service);
            dto.setFullName((fullName != null && !fullName.trim().isEmpty()) ? fullName : "");
            dto.setEmail((email != null && !email.trim().isEmpty()) ? email : "");
            dto.setPhone((phone != null && !phone.trim().isEmpty()) ? phone : "");
            dto.setAddress((address != null && !address.trim().isEmpty()) ? address : "");
            dto.setNote((note != null && !note.trim().isEmpty()) ? note : "");
            model.addAttribute("bookingInfo", dto);
            return "service-booking-step1";
        }
        User user = (User) session.getAttribute("us");
        List<Booking> bookings;
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
        model.addAttribute("bookingInfo", dto);
        return "service-booking-step2";
    }
}

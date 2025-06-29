package com.example.swp.controller;

import com.example.swp.entity.*;
import com.example.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SessionController {
    @Autowired
    BookingService bookingService;
    @Autowired
    WorkScheduleService workScheduleService;
    @Autowired
    SessionService sessionService;
    @Autowired
    PatientReportService patientReportService;
    @Autowired
    ReportMedicineService reportMedicineService;
    @Autowired
    InvoiceService invoiceService;
    @GetMapping("/create-session")
    public String createSessionPage(
            @RequestParam(required = false) Integer bookingId,
            Model model,
            HttpSession session
    ) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        if(booking != null){
            List<WorkingSchedule> workingScheduleList = workScheduleService.getByWorkingDate(booking.getAppointmentDate());

            List<Session> sessions = sessionService.findBySessionDateAndStatusNot(booking.getAppointmentDate(), "Ended");

            System.out.println("sessionsize:" + sessions);
            System.out.println("kkk: " + workingScheduleList);
            // Lọc theo slotId
            workingScheduleList = workingScheduleList.stream()
                    .filter(ws -> ws.getShift().getSlots().stream()
                            .anyMatch(slot -> slot.getSlotId().equals(booking.getSlot().getSlotId())))
                    .collect(Collectors.toList());
            System.out.println("huhu: " + workingScheduleList.get(0).getShift().getSlots());
            System.out.println("wssize: " + workingScheduleList);
//            // Tạo Set các workingScheduleId đã có trong sessions
//            Set<Integer> sessionScheduleIds = sessions.stream()
//                    .map(s -> s.getSchedule().getScheduleId())
//                    .collect(Collectors.toSet());

            // Lọc ra các workingSchedule chưa có trong session
//            workingScheduleList = workingScheduleList.stream()
//                    .filter(ws -> !sessionScheduleIds.contains(ws.getScheduleId()))
//                    .collect(Collectors.toList());

            System.out.println("wsL: " + workingScheduleList);
            model.addAttribute("workingScheduleList", workingScheduleList);
            model.addAttribute("booking", booking);
            return "manager/create-session";
        }
        session.setAttribute("notification", "Lịch hẹn không tồn tại, hãy thử lại");
        return "receptionist/manage-booking";
    }

    @PostMapping("/create-session")
    public String createSession(
            @RequestParam Integer bookingId,
            @RequestParam Integer scheduleId,
            HttpSession session
    ) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        WorkingSchedule schedule = workScheduleService.findById(scheduleId).orElse(null);

        if (booking != null && schedule != null) {
            Session newSession = new Session();
            newSession.setBooking(booking);
            newSession.setSchedule(schedule);
            newSession.setSessionDate(LocalDate.now());
            newSession.setStatus("Processing");
            sessionService.save(newSession);
            booking.setStatus("Processing");
            bookingService.save(booking);
            session.setAttribute("notification", "Tạo phiên khám thành công!");
        } else {
            session.setAttribute("notification", "Không thể tạo phiên khám. Dữ liệu không hợp lệ.");
        }

        return "redirect:" + session.getAttribute("historyUrl");
    }

    @GetMapping("/patient-report")
    public String showPatientRepodt(
            @RequestParam(required = true) Integer bookingId,
            Model model,
            HttpSession session
    ) {
        Booking booking = bookingService.findById(bookingId).orElse(null);
        Session ses = sessionService.findByBooking(booking);
        PatientReport patientReport = patientReportService.findBySession(ses).orElse(null);
        List<ReportMedicine> reportMedicines = reportMedicineService.findByPatientReport(patientReport);
        Invoice invoice = invoiceService.findBySession(ses).orElse(null);
        model.addAttribute("reportMedicines", reportMedicines);
        model.addAttribute("patientReport", patientReport);
        model.addAttribute("ses", ses);
        session.setAttribute("historyUrl", "/booking-history");
        return "patient-report";
    }
}

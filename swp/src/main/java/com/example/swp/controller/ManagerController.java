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
import java.util.*;

@Controller
public class ManagerController {
    @Autowired
    private WorkScheduleService workScheduleService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SlotService slotService;
    @Autowired
    private  SessionService sessionService;
    @Autowired
    private  PatientReportService patientReportService;
    @Autowired
    private ReportMedicineService reportMedicineService;
    @Autowired
    private  MedicineService medicineService;
    @Autowired
    private  InvoiceService invoiceService;

    @GetMapping("/work-assignment")
    public String getWorkSchedule(Model model, HttpSession session,
                                  @RequestParam(name = "assignDate", required = false) LocalDate date
                                  ) {
        if(date == null) date = LocalDate.now();
        try {
            // Lấy danh sách phân công làm việc cho hôm nay
            List<WorkingSchedule> schedules = workScheduleService.getByWorkingDate(date);
            List<Shift> shifts = shiftService.findAll();
            List<Room> rooms = roomService.findAll();
            List<User> doctors = userService.findByRoleId(3);
            // Tạo map [roomId + shiftId] -> WorkingSchedule
            Map<String, WorkingSchedule> scheduleMap = new HashMap<>();
            for (WorkingSchedule s : schedules) {
                String key = s.getRoom().getRoomId() + "_" + s.getShift().getShiftId();
                scheduleMap.put(key, s);
            }
            // Gửi sang giao diện
            model.addAttribute("scheduleMap", scheduleMap);
            model.addAttribute("assignDate", date);
            model.addAttribute("schedules", schedules);
            model.addAttribute("shifts", shifts != null ? shifts : new ArrayList<>());
            model.addAttribute("rooms", rooms != null ? rooms : new ArrayList<>());
            model.addAttribute("doctors", doctors);
            return "manager/work-assignment";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải dữ liệu phân công làm việc.");
            return "error";
        }
    }

    @PostMapping("/work-assignment")
    public String saveWorkSchedule(Model model, HttpSession session,
                                  @RequestParam(name = "roomId", required = false) Integer roomId,
                                   @RequestParam(name = "shiftId", required = false) Integer shiftId,
                                   @RequestParam(name = "assignDate", required = false) LocalDate assignDate,
                                   @RequestParam(name = "doctorId", required = false) Integer doctorId
    ) {
        WorkingSchedule w = new WorkingSchedule();
        Room r = roomService.findById(roomId).orElse(null);
        Shift shift = shiftService.findById(shiftId).orElse(null);
        User u = userService.findByUserId(doctorId);
        w.setIsWorking(true);
        w.setDateWorking(assignDate);
        w.setRoom(r);
        w.setShift(shift);
        w.setEmployee(u);
        w.setLastUpdatedTime(new Date());
        workScheduleService.save(w);
        session.setAttribute("notification", "Phân công lịch thành công");

        return "redirect:/work-assignment?p=7&assignDate="+assignDate;
    }

    @GetMapping("/doctor-schedule")
    public String getDoctorSchedule(Model model, HttpSession session,
                                  @RequestParam(name = "fromDate", required = false) LocalDate fromDate,
                                    @RequestParam(name = "toDate", required = false) LocalDate toDate,
                                    @RequestParam(name = "doctorId", required = false) Integer doctorId
    ) {
        if(fromDate == null) fromDate = LocalDate.now();
        if(toDate == null) toDate = LocalDate.now();
        try {

            if(doctorId == null){
                doctorId = (Integer) session.getAttribute("userId");
            }

            // Lấy danh sách phân công làm việc cho hôm nay
            List<Session> sessions = sessionService.getBySessionDate(fromDate, toDate);
            if (doctorId != null) {
                Integer finalDoctorId = doctorId;
                sessions = sessions.stream()
                        .filter(s -> s.getSchedule() != null && s.getSchedule().getEmployee() != null && s.getSchedule().getEmployee().getUserId().equals(finalDoctorId))
                        .toList();
            }

            List<Shift> shifts = shiftService.findAll();
            List<Room> rooms = roomService.findAll();

            List<Slot> slots = slotService.findAll();
            // Tạo map [slotId + date] -> Session
            Map<String, Session> sessionMap = new HashMap<>();

            for (Session s : sessions) {
                String key = "";
                for(Slot slot : slots){
                    if(s.getSchedule().getShift().getSlots().contains(slot)){
                         key =  slot.getSlotId()
                                + "_" + s.getSessionDate();
                        sessionMap.put(key, s);
                    }
                }
                if(!key.isEmpty()){
                    sessionMap.put(key, s);
                }
            }
            List<LocalDate> dates = fromDate.datesUntil(toDate.plusDays(1)).toList();
            model.addAttribute("dates", dates);
            // Gửi sang giao diện
            model.addAttribute("sessionMap", sessionMap);
            model.addAttribute("fromDate", fromDate);
            model.addAttribute("toDate", toDate);
            model.addAttribute("sessions", sessions);
            model.addAttribute("shifts", shifts != null ? shifts : new ArrayList<>());
            model.addAttribute("slots", slots != null ? slots : new ArrayList<>());
            model.addAttribute("rooms", rooms != null ? rooms : new ArrayList<>());
            session.setAttribute("p", 7);
            String historyUrl = "/doctor-schedule?fromDate="+fromDate+"&toDate="+toDate;
            session.setAttribute("historyUrl", historyUrl);
            return "doctor/doctor-schedule";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải dữ liệu phân công làm việc.");
            return "error";
        }
    }

    @GetMapping("/schedule-details")
    public String getScheduleDetails(Model model, HttpSession session,
                                    @RequestParam(name = "sessionId", required = false) Integer sessionId
    ) {
        Session s = sessionService.findById(sessionId).orElse(null);
        model.addAttribute("ses", s);
        return "doctor/schedule-details";
    }

    @GetMapping("/create-report")
    public String getCreateReportPage(Model model, HttpSession session,
                                     @RequestParam(name = "sessionId", required = false) Integer sessionId
    ) {
        Session s = sessionService.findById(sessionId).orElse(null);
        model.addAttribute("ses", s);
        return "doctor/create-report";
    }

    @GetMapping("/update-report")
    public String getUpdateReportPage(Model model, HttpSession session,
                                      @RequestParam(name = "sessionId", required = false) Integer sessionId
    ) {
        Session s = sessionService.findById(sessionId).orElse(null);
        PatientReport patientReport = patientReportService.findBySession(s).orElse(null);
        List<ReportMedicine> reportMedicines = reportMedicineService.findByPatientReport(patientReport);
        Invoice invoice = invoiceService.findBySession(s).orElse(null);
        model.addAttribute("reportMedicines", reportMedicines);
        model.addAttribute("patientReport", patientReport);
        model.addAttribute("ses", s);
        return "doctor/update-report";
    }

    @PostMapping("/create-report")
    public String saveReport(@RequestParam Integer sessionId,
                             @RequestParam(required = false) String diagnosis,
                             @RequestParam(required = false) String treatmentMethod,
                             @RequestParam(required = false) String doctorNote,
                             @RequestParam(required = false) List<String> medicineData) {

        Session session = sessionService.findById(sessionId).orElse(null);

        PatientReport patientReport = new PatientReport();
        patientReport.setSession(session);
        patientReport.setDiagnosis(diagnosis);
        patientReport.setTreatmentMethod(treatmentMethod);
        patientReport.setDoctorNote(doctorNote);
        patientReport.setLastUpdatedTime(new Date());

        PatientReport savedReport = patientReportService.save(patientReport);
        if (medicineData != null) {
            for (String data : medicineData) {
                String[] parts = data.split("_", 3); // Split thành 3 phần: id_qty_note
                if (parts.length < 3) continue;

                String medId = parts[0];
                int qty;
                try {
                    qty = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    qty = 1;
                }
                String note = parts[2];

                Medicine medicine = medicineService.findById(medId).orElse(null);
                if (medicine != null) {
                    ReportMedicine rm = ReportMedicine.builder()
                            .medicine(medicine)
                            .patientReport(savedReport)
                            .note(note)
                            .quantity(qty)
                            .build();
                    reportMedicineService.save(rm);
                }
            }
        }
        session.setStatus("Ended");
        sessionService.save(session);
        Invoice invoice = new Invoice();
        invoice.setSession(session);
        invoice.setStatus("No_Paid");
        invoice.setCreatedTime(new Date());
        invoice.setCreatedDate(LocalDate.now());
        invoiceService.save(invoice);
        return "redirect:/schedule-details?sessionId=" + sessionId;
    }

    @PostMapping("/update-report")
    public String updateReport(@RequestParam Integer patientReportId,
                               @RequestParam(required = false) String diagnosis,
                               @RequestParam(required = false) String treatmentMethod,
                               @RequestParam(required = false) String doctorNote,
                               @RequestParam(required = false) List<String> medicineData) {

        PatientReport patientReport = patientReportService.findById(patientReportId).orElse(null);
        patientReport.setDiagnosis(diagnosis);
        patientReport.setTreatmentMethod(treatmentMethod);
        patientReport.setDoctorNote(doctorNote);
        patientReport.setLastUpdatedTime(new Date());

        PatientReport savedReport = patientReportService.save(patientReport);

        reportMedicineService.deleteByPatientReport(savedReport);

        if (medicineData != null) {
            for (String data : medicineData) {
                String[] parts = data.split("_", 3);
                if (parts.length < 3) continue;

                String medId = parts[0];
                int qty;
                try {
                    qty = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    qty = 1;
                }
                String note = parts[2];

                Medicine medicine = medicineService.findById(medId).orElse(null);
                if (medicine != null) {
                    ReportMedicine rm = ReportMedicine.builder()
                            .medicine(medicine)
                            .patientReport(savedReport)
                            .note(note)
                            .quantity(qty)
                            .build();
                    reportMedicineService.save(rm);
                }
            }
        }

        return "redirect:/schedule-details?sessionId=" + patientReport.getSession().getSessionId();
    }


}

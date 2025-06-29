package com.example.swp.controller;

import com.example.swp.entity.*;
import com.example.swp.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    SessionService sessionService;
    @Autowired
    PatientReportService patientReportService;
    @Autowired
    ReportMedicineService reportMedicineService;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    @GetMapping("/manage-invoice")
    public String getUpdateReportPage(Model model, HttpSession session,
                                      @RequestParam(name = "createdDate", required = false) LocalDate createdDate,
                                      @RequestParam(name = "status", required = false) String status
    ) {
        if(createdDate == null) createdDate = LocalDate.now();
        List<Invoice> invoices = invoiceService.getByCreatedDate(createdDate);
        // 2. Nếu có status cụ thể và không phải "All", thì lọc
        if (status != null && !status.equals("All")) {
            String finalStatus = status;
            invoices = invoices.stream()
                    .filter(i -> i.getStatus() != null && i.getStatus().equals(finalStatus))
                    .toList(); // hoặc .collect(Collectors.toList()) nếu dùng Java < 16
        }

        if(status == null) status = "All";

        List<Integer> fees = new ArrayList<>();
        for (Invoice invoice : invoices) {
            int fee1 = 0;
            int fee2 = 0;

            // Step 1: Lấy giá dịch vụ
            Session ses = invoice.getSession();
            if (ses != null && ses.getBooking() != null && ses.getBooking().getService() != null) {
                fee1 = ses.getBooking().getService().getPrice();
            }

            // Step 2: Tính tổng tiền thuốc nếu có report
            PatientReport report = patientReportService.findBySession(ses).orElse(null);
            if (report != null) {
                List<ReportMedicine> reportMedicines = report.getReportMedicines(); // đảm bảo có mappedBy = "patientReport"
                for (ReportMedicine rm : reportMedicines) {
                    Medicine med = rm.getMedicine();
                    if (med != null && med.getPrice() != null) {
                        fee2 += med.getPrice() * rm.getQuantity();
                    }
                }
            }

            fees.add(fee1 + fee2);
        }
        model.addAttribute("fees", fees);
        model.addAttribute("invoices", invoices);
        model.addAttribute("status", status);
        model.addAttribute("createdDate", createdDate);
        session.setAttribute("historyUrl", "/manage-invoice?createdDate="+createdDate+"&status="+status);
        return "receptionist/manage-invoice";
    }

    @GetMapping("/api/invoice-detail/{id}")
    @ResponseBody
    public ResponseEntity<?> getInvoiceDetail(@PathVariable("id") Integer id) {
        Invoice invoice = invoiceService.getById(id).orElse(null);
        if (invoice == null) return ResponseEntity.notFound().build();

        Session ses = invoice.getSession();
        String serviceName = ses.getBooking().getService().getServiceName();
        int servicePrice = ses.getBooking().getService().getPrice();

        List<Map<String, Object>> medicineList = new ArrayList<>();
        int totalMedicine = 0;

        PatientReport report = patientReportService.findBySession(ses).orElse(null);
        if (report != null) {
            for (ReportMedicine rm : report.getReportMedicines()) {
                Medicine med = rm.getMedicine();
                if (med != null) {
                    int quantity = rm.getQuantity();
                    int price = med.getPrice();
                    medicineList.add(Map.of(
                            "name", med.getMedicineName(),
                            "price", price,
                            "quantity", quantity
                    ));
                }
            }
        }

        Map<String, Object> response = Map.of(
                "serviceName", serviceName,
                "servicePrice", servicePrice,
                "medicines", medicineList
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-invoice")
    public String updateInvoice(@RequestParam Integer invoiceId,
                             @RequestParam(required = false) String status,
                            @RequestParam(required = false) String totalFee,
                            HttpSession session
                           ) {
        Invoice invoice = invoiceService.getById(invoiceId).orElse(null);
        invoice.setStatus(status);
        invoice.setPaidTime(new Date());
        invoice.setTotalFee(BigDecimal.valueOf(Long.parseLong(totalFee)));
        invoiceService.save(invoice);
        Booking booking = invoice.getSession().getBooking();
        Integer userId = (Integer) session.getAttribute("userId");
        User u = userService.findByUserId(userId);
        booking.setStatus("Completed");
        booking.setUpdatedUser(u);
        booking.setLastUpdatedTime(new Date());
        bookingService.save(booking);
        return "redirect:"+session.getAttribute("historyUrl");
    }
}

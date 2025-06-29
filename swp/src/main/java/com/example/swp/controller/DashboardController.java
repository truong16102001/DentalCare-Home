package com.example.swp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/receptionist-manage")
    public String getReceptionistManagePage(
    ) {
        return "receptionist/receptionist-manage";
    }

    @GetMapping("/manager-manage")
    public String getManagerManagePage(
    ) {
        return "manager/manager-manage";
    }

    @GetMapping("/admin-manage")
    public String getAdminManagePage(
    ) {
        return "admin/admin-manage";
    }

    @GetMapping("/doctor-manage")
    public String getDoctorManagePage(
    ) {
        return "doctor/doctor-manage";
    }
}

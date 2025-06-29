package com.example.swp.controller;

import com.example.swp.entity.Medicine;
import com.example.swp.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MedicineController {
    @Autowired
    MedicineService medicineService;

    @GetMapping("/search-medicine")
    @ResponseBody
    public List<Medicine> searchMedicine(@RequestParam("name") String name) {
        return medicineService.searchByName(name); // Ví dụ: findByMedicineNameContainingIgnoreCase
    }

    @GetMapping("/medicines")
    @ResponseBody
    public List<Medicine> getAllMedicines() {
        return medicineService.findAll();
    }

    @GetMapping("/medicine-manage")
    public String getMedicines(Model model) {
        List<Medicine> medicines = medicineService.findAll();
        medicines = medicines.stream()
                .filter(m -> Boolean.TRUE.equals(m.getIsActive()))
                .collect(Collectors.toList());
        model.addAttribute("medicines", medicines);
        return "manager/medicine-manage";
    }

    @PostMapping("/create-medicine")
    public String createMedicine(
            @RequestParam String medicineId,
            @RequestParam String medicineName,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer remainingQuantity,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String ingredients,
            @RequestParam("imageFile") MultipartFile imageFile) {

        Medicine medicine = new Medicine();
        medicine.setMedicineId(medicineId);
        medicine.setMedicineName(medicineName);
        medicine.setUnit(unit);
        medicine.setPrice(price);
        medicine.setRemainingQuantity(remainingQuantity);
        medicine.setManufacturer(manufacturer);
        medicine.setIngredients(ingredients);
        medicine.setIsActive(true);

        try {
            if (!imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image ="data:image/png;base64,"  + Base64.getEncoder().encodeToString(imageBytes);
                medicine.setImage(base64Image);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Có thể log lỗi hoặc redirect với thông báo
        }

        medicineService.save(medicine);
        return "redirect:/medicine-manage";
    }

    @PostMapping("/delete-medicine")
    public String deleteMedicine(@RequestParam String medicineId) {
        Medicine m = medicineService.findById(medicineId).orElse(null);
        if (m != null) {
            m.setIsActive(false);
            medicineService.save(m);
        }
        return "redirect:/medicine-manage";
    }

    @PostMapping("/update-medicine")
    public String updateMedicine(@RequestParam String medicineId,
                                 @RequestParam String medicineName,
                                 @RequestParam(required = false) MultipartFile imageFile,
                                 @RequestParam String unit,
                                 @RequestParam Integer price,
                                 @RequestParam Integer remainingQuantity,
                                 @RequestParam String manufacturer,
                                 @RequestParam String ingredients) throws IOException {

        Medicine medicine = medicineService.findById(medicineId).orElse(null);
        if (medicine != null) {
            medicine.setMedicineName(medicineName);
            medicine.setUnit(unit);
            medicine.setPrice(price);
            medicine.setRemainingQuantity(remainingQuantity);
            medicine.setManufacturer(manufacturer);
            medicine.setIngredients(ingredients);

            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                String base64Image ="data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
                medicine.setImage(base64Image);
            }

            medicineService.save(medicine);
        }

        return "redirect:/medicine-manage";
    }

}

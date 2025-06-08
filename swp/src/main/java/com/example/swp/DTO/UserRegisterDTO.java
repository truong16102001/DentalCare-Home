package com.example.swp.DTO;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterDTO {
    String email;
    String password;
    String fullName;
    String phoneNumber;
    String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date dob;
}

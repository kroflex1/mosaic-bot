package org.example.mosaic_bot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mosaic_bot.dao.dto.AdminDTO;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.exceptions.AlreadyExistsAdminException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController()
public class WebController {
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public WebController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/admins/register")
    @Operation(summary = "Зарегестрировать нового админа")
    public ResponseEntity<String> createAdmin(@RequestBody Admin admin) {
        try {
            String hashedPassword = passwordEncoder.encode(admin.getPassword());
            adminService.registerAdmin(admin.getName(), hashedPassword);
        } catch (AlreadyExistsAdminException e) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Админ с таким именем уже существует");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/admins")
    @Operation(summary = "Получить имена всех админов")
    public ResponseEntity<List<String>> getAdminNames() {
        List<AdminDTO> allAdmins = adminService.getAdmins();
        List<String> result = new ArrayList<>();
        for (AdminDTO adminDTO : allAdmins) {
            result.add(adminDTO.getName());
        }
        return ResponseEntity.of(Optional.of(result));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Admin{
        private String name;
        private String password;
    }
}

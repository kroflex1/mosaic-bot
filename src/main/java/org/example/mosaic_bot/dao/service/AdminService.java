package org.example.mosaic_bot.dao.service;

import org.example.mosaic_bot.dao.dto.AdminDTO;
import org.example.mosaic_bot.exceptions.AlreadyExistsAdminException;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    AdminDTO registerAdmin(String name, String password) throws AlreadyExistsAdminException;

    Optional<AdminDTO> getAdminById(Long adminId);
    Optional<AdminDTO> getAdminByName(String adminName);

    Optional<AdminDTO> getAdminByNameAndPassword(String name, String password);

    List<AdminDTO> getAdmins();
}

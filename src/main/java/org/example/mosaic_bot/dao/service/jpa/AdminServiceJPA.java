package org.example.mosaic_bot.dao.service.jpa;

import org.example.mosaic_bot.dao.dto.AdminDTO;
import org.example.mosaic_bot.dao.entity.Admin;
import org.example.mosaic_bot.dao.repository.AdminRepository;
import org.example.mosaic_bot.dao.service.AdminService;
import org.example.mosaic_bot.exceptions.AlreadyExistsAdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceJPA implements AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceJPA(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional
    public AdminDTO registerAdmin(String name, String password) throws AlreadyExistsAdminException {
        Admin admin = new Admin(name, password);
        if (!adminRepository.findByNameIs(name).isEmpty()) {
            throw new AlreadyExistsAdminException(name);
        }
        adminRepository.save(admin);
        return admin.convertToDTO();
    }

    @Override
    @Transactional
    public Optional<AdminDTO> getAdminById(Long adminId) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isEmpty()) {
            Optional.empty();
        }
        return Optional.of(admin.get().convertToDTO());
    }

    @Override
    @Transactional
    public Optional<AdminDTO> getAdminByNameAndPassword(String name, String password) {
        Admin admin = adminRepository.getAdminByNameAndPassword(name, password);
        if (admin == null) {
            return Optional.empty();
        }
        return Optional.of(admin.convertToDTO());
    }

    @Override
    @Transactional
    public Optional<AdminDTO> getAdminByName(String name) {
        List<Admin> admins = adminRepository.findByNameIs(name);
        if (admins.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(admins.get(0).convertToDTO());
    }

    @Override
    @Transactional
    public List<AdminDTO> getAdmins() {
        List<AdminDTO> allAdmins = new ArrayList<>();
        adminRepository.findAll().forEach(admin -> allAdmins.add(admin.convertToDTO()));
        return allAdmins;
    }
}

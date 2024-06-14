package org.example.mosaic_bot.dao.repository;

import org.example.mosaic_bot.dao.entity.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Long> {
    List<Admin> findByNameIs(String name);

    Admin getAdminByNameAndPassword(String name, String password);
}

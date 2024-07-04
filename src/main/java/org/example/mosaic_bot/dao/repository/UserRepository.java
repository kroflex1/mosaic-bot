package org.example.mosaic_bot.dao.repository;

import org.example.mosaic_bot.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
   List<User> findByUsername(String username);
}

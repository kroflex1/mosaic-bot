package org.example.mosaic_bot.dao.repository;

import org.example.mosaic_bot.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

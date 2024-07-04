package org.example.mosaic_bot.dao.repository;

import org.example.mosaic_bot.dao.entity.TelegramUser;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
}

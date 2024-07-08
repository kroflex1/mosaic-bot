package org.example.mosaic_bot.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mosaic_bot.dao.dto.TelegramUserDTO;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "telegramUsers")
@Getter
@Setter
@NoArgsConstructor
public class TelegramUser {
    @Id
    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TelegramUserStatus status;

    public TelegramUser(Long chatId) {
        this.chatId = chatId;
        this.isAdmin = false;
        this.status = TelegramUserStatus.CHILLING;
    }

    public TelegramUserDTO convertToDTO() {
        return new TelegramUserDTO(chatId, status, isAdmin);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "chatId = " + chatId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        TelegramUser that = (TelegramUser) o;
        return getChatId() != null && Objects.equals(getChatId(), that.getChatId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package org.example.mosaic_bot.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mosaic_bot.dao.dto.AdminDTO;
import org.example.mosaic_bot.dao.dto.UserDTO;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "activeChats")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "chatId")
    private Long chatId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_lead_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    public User(Long chatId) {
        this.chatId = chatId;
        this.status = UserStatus.CHILLING;
    }

    public UserDTO convertToDTO() {
        AdminDTO adminDTO = admin == null ? null : admin.convertToDTO();
        return new UserDTO(chatId, adminDTO, status);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "chatId = " + chatId + ", " +
                "admin = " + admin + ")";
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
        User that = (User) o;
        return getChatId() != null && Objects.equals(getChatId(), that.getChatId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

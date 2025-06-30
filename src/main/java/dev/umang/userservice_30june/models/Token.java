package dev.umang.userservice_30june.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Token extends BaseModel{
    private String token;
    @ManyToOne
    private User user;
    private Long expiresAt;
}

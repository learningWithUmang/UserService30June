package dev.umang.userservice_30june.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
public class Role extends BaseModel{
    private String name;
}

package org.s1024.hotelbooking.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter

public class Contacts {
    private String phone;
    private String email;
}
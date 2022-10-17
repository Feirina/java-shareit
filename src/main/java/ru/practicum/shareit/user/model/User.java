package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class User {
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}

package com.example.feedservice.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UserDTO {

    private String username;

    private String photo_url;

    private String firstname;

    private String lastname;
}

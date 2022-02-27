package com.decagon.clads.model.dto;

import com.decagon.clads.utils.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CladUser  {
    private Long id = null;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    private String otherName = "";
    @NotNull
    @NotBlank
    private String email;
    private String country;
    private String gender;
    private String phoneNumber;
    private String thumbnail;
    private Role role;
    private String password;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt = LocalDateTime.now();
    private boolean locked;
    private boolean enabled;
}

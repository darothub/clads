package com.decagon.clads.customer.entities;

import com.decagon.clads.artisans.entities.Address;
import com.decagon.clads.artisans.entities.JointEmailAndId;
import com.decagon.clads.artisans.entities.Measurement;
import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.ConstantUtils;
import com.decagon.clads.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
@IdClass(JointEmailAndId.class)
public class Client implements UserDetails{
    @Id
    @SequenceGenerator(name = "client_sequence", sequenceName = "client_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence")
    private Long id;
    @ElementCollection
    private Set<Long> artisanId = new HashSet<>();
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    private String phoneNumber;
    @Id
    @NotBlank
    @NotNull
    @Email
    private String email;
    private String password;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    private String country;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    private String thumbnail;
    @ElementCollection
    private Set<Address> deliveryAddresses = new HashSet<>();
    @ElementCollection
    private Set<Measurement> measurements = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private AUTHPROVIDER authprovider = AUTHPROVIDER.REGULAR;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt;
    private boolean enabled = false;
    private boolean locked = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Client(String firstName, String lastName, String email, Role role, String thumbnail, AUTHPROVIDER authprovider, LocalDateTime createdAt){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.thumbnail = thumbnail;
        this.authprovider = authprovider;
        this.createdAt = createdAt;
    }


}

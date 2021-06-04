package com.decagon.clads.entities.artisan;

import com.decagon.clads.utils.ConstantUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(ArtisanId.class)
public class Artisan implements UserDetails {
    @Id
    @SequenceGenerator(name = "artisan_sequence", sequenceName = "artisan_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="artisan_sequence")
    private Long id;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    private String otherName;
    @NotNull
    @NotBlank
    @Pattern(regexp = ConstantUtils.CATEGORY_PATTERN, message = "Invalid category")
    private String role;
    @NotNull
    private String password;
    @Pattern(regexp = ConstantUtils.IMAGE_PATTERN, message = "Invalid image")
    private String thumbnail;
    @Id
    @Email
    @NotBlank
    @NotNull
    private String email;
    @Pattern(regexp = ConstantUtils.PHONE_NUMBER_PATTERN, message = "Invalid phone number")
    private String phoneNumber;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    private String country;
    private Address workshopAddress = new Address();
    private Address showroomAddress = new Address();
    private int noOfEmployees;
    @OneToOne(mappedBy = "artisan", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Association union;
    private boolean enabled = false;
    private boolean locked = false;
    private String auth_provider = "regular";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
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

}
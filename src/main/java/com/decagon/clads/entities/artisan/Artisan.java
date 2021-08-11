package com.decagon.clads.entities.artisan;

import com.decagon.clads.utils.AUTHPROVIDER;
import com.decagon.clads.utils.ConstantUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(ArtisanId.class)
public class Artisan implements UserDetails {
    @Id
    @SequenceGenerator(name = "artisan_sequence", sequenceName = "artisan_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artisan_sequence")
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
    private String password;
    private String thumbnail;
    @Id
    @NotBlank
    @NotNull
    @Email
    private String email;
    @Pattern(regexp = ConstantUtils.PHONE_NUMBER_PATTERN, message = "Invalid phone number")
    private String phoneNumber;
    @Pattern(regexp = ConstantUtils.GENDER_PATTERN, message = "Invalid gender type")
    private String gender;
    private String country;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "work_street")),
            @AttributeOverride(name = "city", column = @Column(name = "work_city")),
            @AttributeOverride(name = "state", column = @Column(name = "work_state")),
            @AttributeOverride(name = "longitude", column = @Column(name = "work_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "work_latitude"))
    })
    private Address workshopAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "show_room_street")),
            @AttributeOverride(name = "city", column = @Column(name = "show_room_city")),
            @AttributeOverride(name = "state", column = @Column(name = "show_room_state")),
            @AttributeOverride(name = "longitude", column = @Column(name = "show_room_longitude")),
            @AttributeOverride(name = "latitude", column = @Column(name = "show_room_latitude"))
    })
    private Address showroomAddress;
    private int noOfEmployees;
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "assoc_name")),
            @AttributeOverride(name = "ward", column = @Column(name = "assoc_ward")),
            @AttributeOverride(name = "lga", column = @Column(name = "assoc_lga")),
            @AttributeOverride(name = "state", column = @Column(name = "assoc_state")),

    })
    @Embedded
    @Valid
    private Association union;
    @ElementCollection
    private Set<String> specialties = new HashSet<>();
    @ElementCollection
    private Set<String> genderFocus = new HashSet<>(Arrays.asList("Male", "Female", "Kids", "Unisex"));
    private boolean trained = false;
    @Embedded
    private MeasurementOption measurementOption = new MeasurementOption();
    private String deliveryTime;
    @ElementCollection
    private Set<String> paymentTerms = new HashSet<>();
    @ElementCollection
    private Set<String> paymentOptions = new HashSet<>();
    private boolean enabled = false;
    private boolean locked = false;
    @Enumerated(EnumType.STRING)
    private AUTHPROVIDER authprovider = AUTHPROVIDER.REGULAR;

    public Artisan(String firstName, String lastName, String otherName, String role, String thumbnail, String email, AUTHPROVIDER authprovider){
        this.firstName = firstName;
        this.lastName = lastName;
        this.otherName = otherName;
        this.role = role;
        this.thumbnail = thumbnail;
        this.email = email;
        this.authprovider = authprovider;
    }

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
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
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
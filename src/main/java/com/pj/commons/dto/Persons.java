package com.pj.commons.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Persons {

    @Tolerate
    public Persons() {
    }

    private UUID uuid;
    private String fullName;
    private String name;
    private String middleName;
    private String lastName;
    private String motherLastName;
    private String email;
    private String taxId;
    private String curp;

    @JsonSerialize(as = LocalDate.class, using = CustomJsonDateSerializer.class)
    @JsonDeserialize(as = LocalDate.class, using = CustomJsonDateDeserializer.class)
    private LocalDate birthday;
    private Integer yearOfBirth;
    private String Work;
    private Integer educationLevelId;
    private String country;
    private Integer sex;
    private Integer maritalStatus;
    private String religion;

    /*@OneToMany(targetEntity = Phones.class)
    @JoinColumn(name = "Sourceuuid", referencedColumnName = "Uuid", updatable = false, insertable = false)
    @Fetch(FetchMode.JOIN)
    private Set<Phones> phones = new HashSet<>();*/
}

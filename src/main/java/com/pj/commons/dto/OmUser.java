package com.pj.commons.dto;

import com.pj.commons.utils.OmCommons;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
public class OmUser {

    @Tolerate public OmUser(){}

    /*@NotNull
    @Size(min = 4, max = 200)*/
    private String userName;
    @Email
    private String email;
    private UUID uuid;
    @Size(max = 20)
    //@NotNull
    private String phone;
    @Valid
    @NotNull
    private Persons person;
    @NotNull
    private String registryType;
    private Boolean registryCompleted;
    private UUID organizationUuid;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;
    private OmCommons.PLATFORM_ORIGIN origin;
    private LocalDateTime issued;

    private OffsetDateTime createdAt;
    private UUID createdBy;
    private String created;
}

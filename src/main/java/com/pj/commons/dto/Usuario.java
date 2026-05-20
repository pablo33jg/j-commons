package com.pj.commons.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Usuario {

    private Integer id;

    private String nombre;

    private String userName;

    private Integer entidadId;
    private Integer grupoId;
    private Integer almacenId;
    private Integer vendedorId;
    private Integer ejecutivoId;
}

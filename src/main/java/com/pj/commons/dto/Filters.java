package com.pj.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filters {

    @Tolerate
    public Filters(){}

    private Integer page;
    private Integer size;
    private List<Filter> filtros;
    private List<SortModel> sortModel;

    public String toString(){
        var filtros = this.filtros.stream().map(Filter::toString).collect(Collectors.joining(", "));
        return String.format("p -> %d, s -> %d, %n f -> %s", page, size, filtros);
    }
}

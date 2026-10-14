package com.pj.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pj.commons.utils.FiltersFunction;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private FiltersFunction.SQL_OPERATORS mainOperator;
    private String timezone;

    public String toString(){
        var filtros = this.filtros != null
                ? this.filtros.stream().map(Filter::toString).collect(Collectors.joining(", "))
                : "";
        return String.format("p -> %d, s -> %d, tz -> %s, %n f -> %s", page, size, timezone, filtros);
    }
}

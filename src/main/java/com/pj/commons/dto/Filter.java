package com.pj.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pj.commons.utils.FiltersFunction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    @Tolerate
    public Filter(){}

    private String name;
    private String filter;
    @JsonIgnore
    private Double doubleValue;
    @JsonIgnore
    private Long longValue;
    @JsonIgnore
    private Boolean booleanValue;
    @JsonIgnore
    private String value;
    private FiltersFunction.FILTER_DATA_TYPE filterType;
    private FiltersFunction.FILTER_TYPE type;
    private List<Integer> inParameters;
    private List<String> txParameters;

    /*AND, OR*/
    private String operator;
    private List<Filter> condiciones;

    @JsonDeserialize(using = CustomJsonDateTimeDeserializer.class, as = LocalDateTime.class)
    @JsonSerialize(using = CustomJsonDateTimeSerializer.class, as = LocalDateTime.class)
    private LocalDateTime dateFrom;

    @JsonDeserialize(using = CustomJsonDateTimeDeserializer.class, as = LocalDateTime.class)
    @JsonSerialize(using = CustomJsonDateTimeSerializer.class, as = LocalDateTime.class)
    private LocalDateTime dateTo;

    public Double getDouble(String value) {
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public Long getLong(String value) {
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public Boolean getBoolean(String value) {
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public Integer getInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public String toString(){
        return String.format("%n %s, %s, %s, %s", name, type, filterType, filter);
    }
}

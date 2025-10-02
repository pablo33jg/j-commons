package com.pj.commons.utils;


import com.pj.commons.dto.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class FiltersFunction {

    private static final Logger log = LoggerFactory.getLogger(FiltersFunction.class);

    public static Filter createFilter(String name, FILTER_TYPE type, FILTER_DATA_TYPE filterType, String filter){
        return Filter.builder()
                .name(name)
                .type(type)
                .filterType(filterType)
                .filter(filter)
                .build();
    }

    public static Filter createNumFilter(String name, FILTER_TYPE type, String filter){
        return Filter.builder()
                .name(name)
                .type(type)
                .filterType(FILTER_DATA_TYPE.number)
                .filter(filter)
                .build();
    }

    public static Filter createUuidFilter(String name, FILTER_TYPE type, UUID filter){
        return Filter.builder()
                .name(name)
                .type(type)
                .filterType(FILTER_DATA_TYPE.UUID)
                .filter(filter.toString())
                .build();
    }

    public static Filter createTextFilter(String name, FILTER_TYPE type, String filter){
        return Filter.builder()
                .name(name)
                .type(type)
                .filterType(FILTER_DATA_TYPE.text)
                .filter(filter)
                .build();
    }

    public static Filter createInFilter(String name, List<Integer> param){
        return Filter.builder()
                .name(name)
                .type(FILTER_TYPE.in)
                .filterType(FILTER_DATA_TYPE.number)
                .inParameters(param)
                .build();
    }

    public static Filter createInTextFilter(String name, List<String> param){
        return Filter.builder()
                .name(name)
                .type(FILTER_TYPE.in)
                .filterType(FILTER_DATA_TYPE.text)
                .txParameters(param)
                .build();
    }

    public static Filter createUuidInFilter(String name, List<UUID> param){
        return Filter.builder()
                .name(name)
                .type(FILTER_TYPE.in)
                .filterType(FILTER_DATA_TYPE.UUID)
                .txParameters(param.stream().map(UUID::toString).toList())
                .build();
    }

    public static Filter createDateFilter(String name, LocalDateTime value){
        return Filter.builder()
                .name(name)
                .type(FILTER_TYPE.equals)
                .filterType(FILTER_DATA_TYPE.date)
                .dateFrom(value)
                .build();
    }

    public static void printFilters(List<Filter> filters){
        filters.forEach(f -> log.info(f.toString()));
    }

    public enum FILTER_TYPE {
        in,
        equals,
        inRange,
        contains,
        greaterThan,
        greaterThanOrEqual,
        lessThan,
        lessThanOrEqual,
        notEqual
    }

    public enum FILTER_DATA_TYPE {
        number,
        text,
        date,
        UUID,
        bool
    }
}

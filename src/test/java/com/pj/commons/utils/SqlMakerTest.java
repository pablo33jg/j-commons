package com.pj.commons.utils;

import com.pj.commons.dto.Filter;
import com.pj.commons.dto.Filters;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SqlMakerTest {

    public static class SampleEntity {
        private OffsetDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Test
    void testInRangeWithOffsetDateTimeAndFiltersTimezone() {
        SqlMaker sqlMaker = new SqlMaker();
        sqlMaker.clase = SampleEntity.class.getName();

        LocalDateTime fromLocal = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        LocalDateTime toLocal = LocalDateTime.of(2024, 10, 10, 23, 59, 59);

        Filter filter = Filter.builder()
                .name("createdAt")
                .type(FiltersFunction.FILTER_TYPE.inRange)
                .filterType(FiltersFunction.FILTER_DATA_TYPE.date)
                .dateFrom(fromLocal)
                .dateTo(toLocal)
                .build();

        Filters filters = Filters.builder()
                .filtros(List.of(filter))
                .mainOperator(FiltersFunction.SQL_OPERATORS.AND)
                .timezone("America/Bogota") // UTC-5
                .build();

        sqlMaker.filters = filters;

        Query query = mock(Query.class);
        sqlMaker.setParametersWhere(query);

        ArgumentCaptor<OffsetDateTime> fromCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> toCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);

        verify(query).setParameter(eq("createdAtFrom"), fromCaptor.capture());
        verify(query).setParameter(eq("createdAtTo"), toCaptor.capture());

        OffsetDateTime capturedFrom = fromCaptor.getValue();
        OffsetDateTime capturedTo = toCaptor.getValue();

        assertEquals(ZoneOffset.of("-05:00"), capturedFrom.getOffset());
        assertEquals(fromLocal, capturedFrom.toLocalDateTime());

        assertEquals(ZoneOffset.of("-05:00"), capturedTo.getOffset());
        assertEquals(toLocal, capturedTo.toLocalDateTime());
    }

    @Test
    void testInRangeWithOffsetDateTimeAndFilterLevelTimezone() {
        SqlMaker sqlMaker = new SqlMaker();
        sqlMaker.clase = SampleEntity.class.getName();

        LocalDateTime fromLocal = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        LocalDateTime toLocal = LocalDateTime.of(2024, 10, 10, 23, 59, 59);

        Filter filter = Filter.builder()
                .name("createdAt")
                .type(FiltersFunction.FILTER_TYPE.inRange)
                .filterType(FiltersFunction.FILTER_DATA_TYPE.date)
                .timezone("Europe/Madrid") // UTC+2 (in Oct CEST)
                .dateFrom(fromLocal)
                .dateTo(toLocal)
                .build();

        Filters filters = Filters.builder()
                .filtros(List.of(filter))
                .mainOperator(FiltersFunction.SQL_OPERATORS.AND)
                .timezone("America/Bogota") // Overridden by filter level
                .build();

        sqlMaker.filters = filters;

        Query query = mock(Query.class);
        sqlMaker.setParametersWhere(query);

        ArgumentCaptor<OffsetDateTime> fromCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);
        ArgumentCaptor<OffsetDateTime> toCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);

        verify(query).setParameter(eq("createdAtFrom"), fromCaptor.capture());
        verify(query).setParameter(eq("createdAtTo"), toCaptor.capture());

        OffsetDateTime capturedFrom = fromCaptor.getValue();
        OffsetDateTime capturedTo = toCaptor.getValue();

        assertEquals(ZoneOffset.of("+02:00"), capturedFrom.getOffset());
        assertEquals(fromLocal, capturedFrom.toLocalDateTime());

        assertEquals(ZoneOffset.of("+02:00"), capturedTo.getOffset());
        assertEquals(toLocal, capturedTo.toLocalDateTime());
    }

    @Test
    void testInRangeWithLocalDateTimeDoesNotAffectTimezone() {
        SqlMaker sqlMaker = new SqlMaker();
        sqlMaker.clase = SampleEntity.class.getName();

        LocalDateTime fromLocal = LocalDateTime.of(2024, 10, 10, 0, 0, 0);
        LocalDateTime toLocal = LocalDateTime.of(2024, 10, 10, 23, 59, 59);

        Filter filter = Filter.builder()
                .name("updatedAt")
                .type(FiltersFunction.FILTER_TYPE.inRange)
                .filterType(FiltersFunction.FILTER_DATA_TYPE.date)
                .dateFrom(fromLocal)
                .dateTo(toLocal)
                .build();

        Filters filters = Filters.builder()
                .filtros(List.of(filter))
                .mainOperator(FiltersFunction.SQL_OPERATORS.AND)
                .timezone("America/Bogota")
                .build();

        sqlMaker.filters = filters;

        Query query = mock(Query.class);
        sqlMaker.setParametersWhere(query);

        // For LocalDateTime, it must pass the LocalDateTime directly without timezone conversion
        verify(query).setParameter("updatedAtFrom", fromLocal);
        verify(query).setParameter("updatedAtTo", toLocal);
    }
}

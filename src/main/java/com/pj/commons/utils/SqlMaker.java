package com.pj.commons.utils;

import com.pj.commons.dto.Filters;
import com.pj.commons.dto.SortModel;
import com.pj.commons.dto.Filter;
import com.pj.commons.exception.InvalidFieldException;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

//@Component
public class SqlMaker {

    public Filters filters;
    public String clase;

    @Value("${app.database.like-operator:LIKE}")
    private String likeOperator;

    private final Logger log = LoggerFactory.getLogger(SqlMaker.class);

    public String order() {
        String order = "";
        if (filters.getSortModel() != null) {
            for (SortModel sort : filters.getSortModel()) {
                if (!order.isEmpty())
                    order += ", ";
                var type = getType(sort.getColId());
                order += sort.getColId() + " " + sort.getSort();
            }
        }
        if (!order.isEmpty())
            order = " ORDER BY " + order;
        return order;
    }

    public String where() {
        String where = "";
        if (filters.getFiltros() != null)
            for (Filter filter : filters.getFiltros()) {
                if (!where.isEmpty())
                    where += String.format(" %s ", filters.getMainOperator());
                where += createSentence(filter);//filtro.getName() + " " + tipoOperador(filtro.getType()) + " :" + filtro.getName().replace(".", "");
            }
        if (!where.isEmpty())
            where = " WHERE " + where;
        return where;
    }

    private String createSentence(Filter filter) {
        String sentence = filter.getName();
        if (filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.number) || filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.date) || filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.UUID)) {
            sentence = conditionsSentence(filter);
        }
        if (filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.text) || filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.bool)) {
            sentence = filter.getName() + " " + tipoOperador(filter.getType()) + " :" + filter.getName().replace(".", "") + (filter.getType().equals(FiltersFunction.FILTER_TYPE.in) ? "In" : "");
        }
        return sentence;
    }

    private String conditionsSentence(Filter filter) {
        String conditions = "";
        if (filter.getType() == null) {
            Boolean moreOptions = false;
            /*for(Filtro condition: filtro.getCondiciones()){
                if(moreOptions)
                    conditions += " " + filtro.getOperator() + " ";
                if(condition.getType().equals("inRange"))
                    conditions += filtro.getName() + " " + tipoOperador(condition.getType()) + " :" + nameWithoutDot(filtro.getName()) + "From" + condition.getName() + " AND :" + nameWithoutDot(filtro.getName()) + "To" + condition.getName();
                else
                    conditions += filtro.getName() + " " + tipoOperador(condition.getType()) + ":" + filtro.getName().replace(".", "") + condition.getName();
                moreOptions = true;
            }*/
        } else {
            if (filter.getType().equals(FiltersFunction.FILTER_TYPE.inRange)) {
                conditions += filter.getName() + " " + tipoOperador(filter.getType()) + " :" + nameWithoutDot(filter.getName()) + "From AND :" + nameWithoutDot(filter.getName()) + "To";
            } else {
                //if (filtro.getType().equals("equals") || filtro.getType().equals("in")) {
                conditions += filter.getName() + " " + tipoOperador(filter.getType()) + " :" + nameWithoutDot(filter.getName()) + (filter.getType().equals(FiltersFunction.FILTER_TYPE.in) ? "In" : "");
            }
        }
        return conditions;
    }

    private String nameWithoutDot(String name) {
        return name.replace(".", "");
    }

    public void setParametersWhere(Query query) {
        if (filters.getFiltros() != null)
            for (Filter filter : filters.getFiltros()) {
                String type = getType(filter.getName());
                if (type.endsWith("Long"))
                    query.setParameter(filter.getName().replace(".", ""), filter.getLong(filter.getFilter()));
                else if (type.endsWith("Double")) {
                    query.setParameter(filter.getName().replace(".", ""), filter.getDouble(filter.getFilter()));
                } else if (type.endsWith("Integer")) {
                    /*if (filtro.getType().equals("equals")) {
                        query.setParameter(filtro.getName().replace(".", ""), filtro.getLong(filtro.getFilter()).intValue());
                    }*/
                    if (filter.getType().equals(FiltersFunction.FILTER_TYPE.in)) {
                        query.setParameter(filter.getName().replace(".", "") + "In", filter.getInParameters());
                    } else {
                        query.setParameter(filter.getName().replace(".", ""), filter.getLong(filter.getFilter()).intValue());
                    }
                } else if (type.endsWith("Boolean"))
                    query.setParameter(filter.getName().replace(".", ""), filter.getBoolean(filter.getFilter()));
                else if (type.toUpperCase(Locale.ROOT).endsWith("UUID")) {
                    if (filter.getType().equals(FiltersFunction.FILTER_TYPE.in)) {
                        query.setParameter(filter.getName().replace(".", "") + "In", filter.getTxParameters());
                    } else {
                        query.setParameter(filter.getName().replace(".", ""), UUID.fromString(filter.getFilter()));
                    }
                } else if (filter.getType().equals(FiltersFunction.FILTER_TYPE.inRange)) {
                    if (type.endsWith("OffsetDateTime")) {
                        ZoneId clientZone = getClientZoneId(filter);
                        log.info("Filtro tipo offsetdatetime con timezone: {}", clientZone);
                        var from = filter.getDateFrom() != null
                                ? filter.getDateFrom().atZone(clientZone).toOffsetDateTime()
                                : null;
                        var to = filter.getDateTo() != null
                                ? filter.getDateTo().atZone(clientZone).toOffsetDateTime()
                                : null;
                        query.setParameter(filter.getName().replace(".", "") + "From", from);
                        query.setParameter(filter.getName().replace(".", "") + "To", to);
                    } else if (type.endsWith("LocalDate")) {
                        log.info("Filtro tipo LocalDate");
                        var from = filter.getDateFrom() != null ? filter.getDateFrom().toLocalDate() : null;
                        var to = filter.getDateTo() != null ? filter.getDateTo().toLocalDate() : null;
                        query.setParameter(filter.getName().replace(".", "") + "From", from);
                        query.setParameter(filter.getName().replace(".", "") + "To", to);
                    } else {
                        query.setParameter(filter.getName().replace(".", "") + "From", filter.getDateFrom());
                        query.setParameter(filter.getName().replace(".", "") + "To", filter.getDateTo());
                    }
                } else {
                    if (filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.date)) {
                        var classType = getClassType(filter.getName());
                        log.info("El tipo obtenido en un filtro tipo {} es {}, {}|{}", FiltersFunction.FILTER_DATA_TYPE.date, classType, filter.getType(), filter.getDateFrom());
                        if (classType == LocalDate.class) {
                            query.setParameter(filter.getName().replace(".", ""), filter.getDateFrom() != null ? filter.getDateFrom().toLocalDate() : null);
                        } else if (classType == OffsetDateTime.class) {
                            ZoneId clientZone = getClientZoneId(filter);
                            query.setParameter(filter.getName().replace(".", ""), filter.getDateFrom() != null ? filter.getDateFrom().atZone(clientZone).toOffsetDateTime() : null);
                        } else {
                            query.setParameter(filter.getName().replace(".", ""), filter.getDateFrom());
                        }
                    } else {
                        if (filter.getType().equals(FiltersFunction.FILTER_TYPE.equals) || filter.getType().equals(FiltersFunction.FILTER_TYPE.contains) || filter.getType().equals(FiltersFunction.FILTER_TYPE.notEqual)) {
                            Class<?> field = isEnum(filter.getName());
                            if (!field.isEnum()) {
                                query.setParameter(filter.getName().replace(".", ""), tipoParameter(filter.getType()).replace(":param", filter.getFilter()));
                            } else {
                                query.setParameter(filter.getName().replace(".", ""), this.getFilter(field, filter));
                            }
                        } else if (filter.getType().equals(FiltersFunction.FILTER_TYPE.in)) {
                            query.setParameter(filter.getName().replace(".", "") + "In", filter.getTxParameters());
                        } else {
                            query.setParameter(filter.getName().replace(".", ""), filter.getFilter());
                        }
                    }
                }
            }
    }

    private ZoneId getClientZoneId(Filter filter) {
        String tz = (filter != null && filter.getTimezone() != null && !filter.getTimezone().isBlank())
                ? filter.getTimezone()
                : (filters != null && filters.getTimezone() != null && !filters.getTimezone().isBlank()
                ? filters.getTimezone()
                : null);

        if (tz != null) {
            try {
                return ZoneId.of(tz);
            } catch (Exception e) {
                log.warn("Invalid timezone '{}', falling back to system default: {}", tz, e.getMessage());
            }
        }
        return ZoneId.systemDefault();
    }

    private Class<?> getBaseEnum(String className, String field) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> entityClass = Class.forName(className);
        Field statusField = entityClass.getDeclaredField(field);
        return statusField.getType();
    }

    private Enum<?> getFilter(Class<?> field, Filter filter) {
        try {
            ClassLoader appClassLoader = Thread.currentThread().getContextClassLoader();
            //Class<? extends Enum> enumClass = field.asSubclass(Enum.class);
            Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(field.getName(), true, appClassLoader);
            // Obtener los valores del enum
            Enum<?>[] enumConstants = enumClass.getEnumConstants();

            if (filter.getFilterType().equals(FiltersFunction.FILTER_DATA_TYPE.number)) {
                int value = Integer.valueOf(filter.getFilter());

                var method = enumClass.getMethod("fromValue", int.class);
                Enum<?> result = (Enum<?>) method.invoke(enumClass, value);
                return result;
            } else {
                var method = enumClass.getMethod("fromValue", String.class);
                Enum<?> result = (Enum<?>) method.invoke(enumClass, filter.getFilter());
                return result;
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //return null;
    }

    private Class<?> isEnum(String fieldName) {
        String[] fields = fieldName.split("\\.");
        Class cl = null;
        try {
            cl = Class.forName(clase);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new InvalidFieldException(clase);
        }
        boolean hasSuperClass;
        Class superClass = cl;
        do {
            Integer i = 0;
            hasSuperClass = false;
            Class relacionada = superClass;
            superClass = superClass.getSuperclass();
            if (!superClass.equals(Object.class))
                hasSuperClass = true;
            for (String f : fields) {
                Field fl = null;
                try {
                    fl = relacionada.getDeclaredField(f);
                    relacionada = fl.getType();
                    if (i.equals(fields.length - 1)) {
                        return fl.getType();
                    }
                } catch (NoSuchFieldException e) {
                    //e.printStackTrace();
                    if (!hasSuperClass)
                        throw new InvalidFieldException(f);
                }
                i++;
            }
        } while (hasSuperClass);
        return null;
    }

    private String getType(String fieldsString) {
        //fieldsString = fieldsString.replace(".", "-");
        String[] fields = fieldsString.split("\\.");
        Boolean original = true;
        Class cl = null;
        try {
            cl = Class.forName(clase);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new InvalidFieldException(clase);
        }
        Field[] fls = cl.getDeclaredFields();
        boolean hasSuperClass;
        Class superClass = cl;
        do {
            hasSuperClass = false;

            if (fields.length == 0) {
                Field field = null;
                try {
                    field = superClass.getField(fieldsString);
                    return field.getType().getName();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    //throw new InvalidFieldException(fieldsString);
                }
            }

            Integer i = 0;
            Class relacionada = superClass;
            superClass = superClass.getSuperclass();
            if (!superClass.equals(Object.class))
                hasSuperClass = true;
            for (String f : fields) {
                Field fl = null;
                try {
                    fl = relacionada.getDeclaredField(f);
                    relacionada = fl.getType();
                    if (i.equals(fields.length - 1)) {
                        return fl.getType().getName();
                    }
                } catch (NoSuchFieldException e) {
                    //e.printStackTrace();
                    if (!hasSuperClass)
                        throw new InvalidFieldException(f);
                }
                i++;
            }
        } while (hasSuperClass);
        return "";
    }

    private Class<?> getClassType(String fieldsString) {
        //fieldsString = fieldsString.replace(".", "-");
        String[] fields = fieldsString.split("\\.");
        Boolean original = true;
        Class cl = null;
        try {
            cl = Class.forName(clase);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new InvalidFieldException(clase);
        }
        Field[] fls = cl.getDeclaredFields();
        boolean hasSuperClass;
        Class superClass = cl;
        do {
            hasSuperClass = false;

            if (fields.length == 0) {
                Field field = null;
                try {
                    field = superClass.getField(fieldsString);
                    return field.getType();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    //throw new InvalidFieldException(fieldsString);
                }
            }

            Integer i = 0;
            Class relacionada = superClass;
            superClass = superClass.getSuperclass();
            if (!superClass.equals(Object.class))
                hasSuperClass = true;
            for (String f : fields) {
                Field fl = null;
                try {
                    fl = relacionada.getDeclaredField(f);
                    relacionada = fl.getType();
                    if (i.equals(fields.length - 1)) {
                        return fl.getType();
                    }
                } catch (NoSuchFieldException e) {
                    //e.printStackTrace();
                    if (!hasSuperClass)
                        throw new InvalidFieldException(f);
                }
                i++;
            }
        } while (hasSuperClass);
        return null;
    }

    private String tipoOperador(FiltersFunction.FILTER_TYPE tipo) {
        switch (tipo) {
            case contains:
                return likeOperator;//"like";
            case inRange:
                return " BETWEEN ";
            case in:
                return " IN ";
            case greaterThan:
                return " > ";
            case greaterThanOrEqual:
                return " >= ";
            case lessThan:
                return " < ";
            case lessThanOrEqual:
                return " <= ";
            case notEqual:
                return " <> ";
        }
        return "=";
    }

    private String tipoParameter(FiltersFunction.FILTER_TYPE tipo) {
        switch (tipo) {
            case contains:
                return "%:param%";
        }
        return ":param";
    }
}

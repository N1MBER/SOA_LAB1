package ru.itmo.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import ru.itmo.entity.Color;
import ru.itmo.entity.Country;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FieldConverter {

    public static LocalDateTime localDateTimeConvert(String date, String pattern){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(date, formatter);
        } catch (Exception e){
            return null;
        }
    }

    public static Float floatConvert(String number){
        try {
            return Float.parseFloat(number);
        } catch (Exception e){
            return null;
        }
    }

    public static Long longConvert(String number){
        try {
            return Long.parseLong(number);
        } catch (Exception e){
            return null;
        }
    }

    public static Color colorConvert(String color){
        try {
            return Color.valueOf(color);
        } catch (Exception e){
            return null;
        }
    }

    public static Country countryConvert(String country){
        try {
            return Country.valueOf(country);
        } catch (Exception e){
            return null;
        }
    }

    public static Integer intConvert(String number){
        try {
            return Integer.parseInt(number);
        } catch (Exception e){
            return null;
        }
    }

    public static Integer intConvert(String number, int defaultInteger){
        try {
            return Integer.parseInt(number);
        } catch (Exception e){
            return defaultInteger;
        }
    }


    public static Double doubleConvert(String number){
        try {
            return Double.parseDouble(number);
        } catch (Exception e){
            return null;
        }
    }

    public static Boolean booleanConvert(String bool){
        try {
            return Boolean.parseBoolean(bool);
        } catch (Exception e){
            return null;
        }
    }

    public static String sortFieldConvert(String sortField, List<String> fields){
        return fields.contains(sortField) ? sortField : null;
    }

    public static String addPrefixFieldConvert(String prefix, String field){
        return prefix + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static String stringLikeConvert(String string){
        return string + "%";
    }

    public static String bodyToStringConvert(HttpServletRequest request) throws IOException {
        return IOUtils.toString(request.getReader());
    }

    public static String removePrefixFieldConvert(String field, String prefix){
        return field.replaceAll(prefix, "").toLowerCase();
    }
}

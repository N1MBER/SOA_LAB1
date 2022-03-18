package com.example.back.converter;

import com.example.back.utils.PersonParams;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XMLLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        return FieldConverter.localDateTimeConvert(s, PersonParams.DATE_PATTERN);
    }

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return localDateTime.format(DateTimeFormatter.ofPattern(PersonParams.DATE_PATTERN));
    }
}


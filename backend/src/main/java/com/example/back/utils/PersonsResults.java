package com.example.back.utils;

import lombok.AllArgsConstructor;
import com.example.back.entity.Person;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@XmlRootElement(name = "person_result")
public class PersonsResults {
    @XmlElement
    private final long totalPersons;
    @XmlElementWrapper(name = "persons")
    @XmlElement(name = "person")
    private final List<Person> list;
    public PersonsResults(){
        this.totalPersons = 0;
        this.list = new ArrayList<>();
    }

    public List<Person> getList() {
        return list;
    }
}

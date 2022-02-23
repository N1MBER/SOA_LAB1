package com.example.back.utils;

import com.example.back.stringEntity.Person;
import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@XmlRootElement(name = "person_result")
public class PersonResultsDTO {
    @XmlElement
    private long totalPersons;
    @XmlElementWrapper(name = "persons")
    @XmlElement(name = "person")
    private List<Person> list;

    public PersonResultsDTO(){
        this.totalPersons = 0;
        this.list = new ArrayList<>();
    }

    public List<Person> getList() {
        return list;
    }

    public long getTotalPersons() {
        return totalPersons;
    }
}

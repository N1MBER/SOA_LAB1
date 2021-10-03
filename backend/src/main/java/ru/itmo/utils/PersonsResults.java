package ru.itmo.utils;

import lombok.AllArgsConstructor;
import ru.itmo.entity.Person;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@XmlRootElement(name = "person_result")
public class PersonsResults {
    @XmlElementWrapper(name = "persons")
    @XmlElement(name = "person")
    private final List<Person> list;
    public PersonsResults(){
        this.list = new ArrayList<>();
    }
}

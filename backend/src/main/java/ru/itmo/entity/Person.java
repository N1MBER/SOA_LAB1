package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.converter.XMLLocalDateTimeAdapter;
import ru.itmo.converter.FieldConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@XmlRootElement
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private int id;

    @NotBlank
    @XmlElement
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    @XmlElement
    private Coordinates coordinates;

    @NotNull
    @XmlElement
    @XmlJavaTypeAdapter(XMLLocalDateTimeAdapter.class)
    private LocalDateTime creationDate;

    @NotNull
    @Positive
    @XmlElement
    private Float height;


    @NotBlank
    @XmlElement
    @NotNull
    @Column(unique = true)
    @Size(min = 0, max = 34)
    private String passportID;

    @NotBlank
    @XmlElement
    @NotNull
    private Color hairColor;

    @NotBlank
    @XmlElement
    @NotNull
    private Country nationality;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    @XmlElement
    @NotNull
    private Location location;

    public static List<String> getAllFields(){
        Field[] fields = Person.class.getDeclaredFields();
        List<String> fieldList = Arrays
                .stream(fields)
                .map(Field::getName)
                .filter(field -> !field.equals("coordinates"))
                .collect(Collectors.toList());
        Arrays
                .stream(Coordinates.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> FieldConverter.addPrefixFieldConvert("coordinates", field.getName()))
                .forEach(fieldList::add);
        Arrays
                .stream(Person.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("location"))
                .map(field -> FieldConverter.addPrefixFieldConvert("person", field.getName()))
                .forEach(fieldList::add);
        Arrays
                .stream(Location.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> FieldConverter.addPrefixFieldConvert("location", field.getName()))
                .forEach(fieldList::add);
        return fieldList;
    }

    public void update(Person personUpdate){
        this.name = personUpdate.getName();
        this.coordinates.update(personUpdate.getCoordinates());
        this.creationDate = personUpdate.getCreationDate();
        this.height = personUpdate.getHeight();
        this.passportID = personUpdate.getPassportID();
        this.hairColor = personUpdate.getHairColor();
        this.nationality = personUpdate.getNationality();
        this.location.update(personUpdate.getLocation());
    }
}
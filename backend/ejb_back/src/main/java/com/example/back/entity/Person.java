package com.example.back.entity;

import com.example.back.utils.PersonParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.back.converter.XMLLocalDateTimeAdapter;
import com.example.back.converter.FieldConverter;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
    //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    @NotNull
    private int id;

    //Поле не может быть null, Строка не может быть пустой
    @XmlElement
    @NotBlank
    @NotNull
    private String name;

    //Поле не может быть null
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    @XmlElement
    private Coordinates coordinates;

    //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    @XmlElement
    @XmlJavaTypeAdapter(XMLLocalDateTimeAdapter.class)
    @NotNull
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();

    //Поле не может быть null, Значение поля должно быть больше 0
    @NotNull
    @Positive
    @XmlElement
    @Min(0)
    private Float height;

    //Длина строки не должна быть больше 34, Значение этого поля должно быть уникальным, Поле не может быть null
    @NotBlank
    @XmlElement
    @Column(unique = true)
    @Size(min = 0, max = 34)
    private String passportID;

    //Поле не может быть null
    @XmlElement
    @Enumerated(EnumType.STRING)
    @NotNull
    private Color hairColor;

    @XmlElement
    @Enumerated(EnumType.STRING)
    private EyeColor eyeColor;

    //Поле не может быть null
    @XmlElement
    @Enumerated(EnumType.STRING)
    @NotNull
    private Country nationality;

    //Поле не может быть null
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
                .stream(Location.class.getDeclaredFields())
                .filter(field -> !field.getName().equals("id"))
                .map(field -> FieldConverter.addPrefixFieldConvert("location", field.getName()))
                .forEach(fieldList::add);
        return fieldList;
    }

    public void setCreationDateByNew(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreationDate(String date) {
        this.creationDate = FieldConverter.localDateTimeConvert(date, PersonParams.DATE_PATTERN);
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
        this.eyeColor = personUpdate.getEyeColor();
    }
}
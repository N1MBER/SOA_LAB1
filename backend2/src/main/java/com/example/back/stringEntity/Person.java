package com.example.back.stringEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement
public class Person {
    @XmlElement
    private String id;

    @XmlElement
    private String name;

    @XmlElement
    private Coordinates coordinates;

    @XmlElement
    private String creationDate;

    @XmlElement
    private String height;

    @XmlElement
    private String passportID;

    @XmlElement
    private String hairColor;

    @XmlElement
    private String eyeColor;

    @XmlElement
    private String nationality;

    @XmlElement
    private Location location;
}

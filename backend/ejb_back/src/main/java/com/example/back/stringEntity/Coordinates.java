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
public class Coordinates {

    @XmlElement
    private String id;

    @XmlElement
    private String x;

    @XmlElement
    private String y;
}
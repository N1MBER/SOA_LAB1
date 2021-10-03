package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@XmlRootElement
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private long id;

    @XmlElement
    @NotNull
    private double x;

    @XmlElement
    @NotNull
    private int y;

    public void update(Coordinates coordinatesUpdate){
        this.x = coordinatesUpdate.getX();
        this.y = coordinatesUpdate.getY();
    }
}
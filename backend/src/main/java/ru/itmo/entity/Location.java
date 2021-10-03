package ru.itmo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@XmlRootElement
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlElement
    private long id;

    @NotNull
    @XmlElement
    private Long x;

    @XmlElement
    private long y;

    @XmlElement
    private long z;

    public void update(Location locationUpdate){
        this.x = locationUpdate.getX();
        this.y = locationUpdate.getY();
        this.z = locationUpdate.getZ();
    }
}
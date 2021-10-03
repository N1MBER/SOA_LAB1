package ru.itmo.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.itmo.converter.FieldConverter;
import ru.itmo.entity.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PersonParams {
    String name;
    LocalDateTime creationDate;
    Double coordinatesX;
    Integer coordinatesY;
    String passportID;
    Float height;
    Long locationX;
    Long locationY;
    Long locationZ;
    Color hairColor;
    Country nationality;
    Integer pageIdx;
    Integer pageSize;
    String sortField;
    Boolean lessThanHeightFlag;

    public final static String DATE_PATTERN = "dd.MM.yyyy";

    public PersonParams(
            String name,
            String creationDate,
            String coordinatesX,
            String coordinatesY,
            String passportID,
            String height,
            String locationX,
            String locationY,
            String locationZ,
            String hairColor,
            String nationality,
            String pageIdx,
            String pageSize,
            String sortField,
            String lessThanHeightFlag
    ) {
        this.name = name;
        this.creationDate = FieldConverter.localDateTimeConvert(creationDate, DATE_PATTERN);
        this.coordinatesX = FieldConverter.doubleConvert(coordinatesX);
        this.coordinatesY = FieldConverter.intConvert(coordinatesY);
        this.passportID = passportID;
        this.height = FieldConverter.floatConvert(height);
        this.locationX = (Long)FieldConverter.longConvert(locationX);
        this.locationY = FieldConverter.longConvert(locationY);
        this.locationZ = FieldConverter.longConvert(locationZ);
        this.hairColor = FieldConverter.colorConvert(hairColor);
        this.nationality = FieldConverter.countryConvert(nationality);
        this.pageIdx = Math.max(FieldConverter.intConvert(pageIdx, 1), 1);
        this.pageSize = Math.max(FieldConverter.intConvert(pageSize, 10), 1);
        this.sortField = FieldConverter.sortFieldConvert(sortField, Person.getAllFields());
        this.lessThanHeightFlag = FieldConverter.booleanConvert(lessThanHeightFlag);
    }

    public List<Predicate> getPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Person> root,
            Join<Person, Coordinates> coordinatesJoin,
            Join<Person, Location> locationJoin
    ){
        List<Predicate> predicates = new ArrayList<>();
        if (this.name != null){
            predicates.add(criteriaBuilder.like(root.get("name"), FieldConverter.stringLikeConvert(this.name)));
        }
        if (this.creationDate != null){
            predicates.add(criteriaBuilder.equal(root.get("creationDate"), this.creationDate));
        }
        if (this.passportID != null){
            predicates.add(criteriaBuilder.equal(root.get("passportID"), this.passportID));
        }
        if (this.hairColor != null){
            predicates.add(criteriaBuilder.equal(root.get("hairColor"), this.hairColor));
        }
        if (this.nationality != null){
            predicates.add(criteriaBuilder.equal(root.get("nationality"), this.nationality));
        }
        if (this.coordinatesX != null){
            predicates.add(criteriaBuilder.equal(coordinatesJoin.get("x"), this.coordinatesX));
        }
        if (this.coordinatesY != null){
            predicates.add(criteriaBuilder.equal(coordinatesJoin.get("y"), this.coordinatesY));
        }
        if (this.height != null){
            predicates.add(criteriaBuilder.equal(root.get("height"), this.height));
        }
        if (this.locationX != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("x"), this.locationX));
        }
        if (this.locationY != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("y"), this.locationY));
        }
        if (this.locationZ != null){
            predicates.add(criteriaBuilder.equal(locationJoin.get("z"), this.locationZ));
        }
        return predicates;
    }

    public List<Predicate> getLessHeightPredicate(CriteriaBuilder criteriaBuilder, Root<Person> root){
        List<Predicate> predicates = new ArrayList<>();
        if (this.height != null){
            predicates.add(criteriaBuilder.lessThan(root.get("height"), this.height));
        }
        return predicates;
    }
}

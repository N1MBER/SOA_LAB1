package com.example.back.utils;

import com.example.back.converter.XMLConverter;
import com.example.back.validator.ValidatorMessage;
import com.example.back.validator.ValidatorResult;
import lombok.Getter;
import com.example.back.converter.FieldConverter;
import com.example.back.entity.*;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class PersonParams {

    private XMLConverter xmlConverter;

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
    String nameString;
    LocalDateTime creationDateString;
    Double coordinatesXString;
    Integer coordinatesYString;
    String passportIDString;
    Float heightString;
    Long locationXString;
    Long locationYString;
    Long locationZString;
    Color hairColorString;
    Country nationalityString;
    Integer pageIdxString;
    Integer pageSizeString;
    String sortFieldString;

    @Setter
    boolean lessLocationFlag = true;

    public final static String DATE_PATTERN = "dd.MM.yyyy";

    private ValidatorResult validatorResult;

    public PersonParams(){
        validatorResult = new ValidatorResult();
    }

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
            String sortField
    ) {
        xmlConverter = new XMLConverter();
        validatorResult = new ValidatorResult();
        this.name = name;
        this.creationDate = FieldConverter.localDateTimeConvert(creationDate, DATE_PATTERN);
        this.coordinatesX = FieldConverter.doubleConvert(coordinatesX, "Coordinates X", validatorResult);
        this.coordinatesY = FieldConverter.intConvert(coordinatesY, "Coordinates Y", validatorResult);
        this.passportID = passportID;
        this.height = FieldConverter.floatConvert(height, "Person height", validatorResult);
        this.locationX = FieldConverter.longConvert(locationX, "Location X", validatorResult);
        this.locationY = FieldConverter.longConvert(locationY, "Location Y", validatorResult);
        this.locationZ = FieldConverter.longConvert(locationZ, "Location Z", validatorResult);
        this.hairColor = FieldConverter.colorConvert(hairColor, "Person hairColor", validatorResult);
        this.nationality = FieldConverter.countryConvert(nationality, "Person nationality", validatorResult);
        this.pageIdx = FieldConverter.intConvert(pageIdx, -1);
        this.pageSize = FieldConverter.intConvert(pageSize, -1);
        this.sortField = FieldConverter.sortFieldFilterConvert(sortField, Person.getAllFields(), validatorResult);
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
            predicates.add(criteriaBuilder.like(root.get("passportID"), this.passportID));
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
        if (this.lessLocationFlag) {
            if (this.locationX != null){
                predicates.add(criteriaBuilder.lessThan(locationJoin.get("x"), this.locationX));
            }
            if (this.locationY != null){
                predicates.add(criteriaBuilder.lessThan(locationJoin.get("y"), this.locationY));
            }
            if (this.locationZ != null){
                predicates.add(criteriaBuilder.lessThan(locationJoin.get("z"), this.locationZ));
            }
        } else {
            if (this.locationX != null){
                predicates.add(criteriaBuilder.equal(locationJoin.get("x"), this.locationX));
            }
            if (this.locationY != null){
                predicates.add(criteriaBuilder.equal(locationJoin.get("y"), this.locationY));
            }
            if (this.locationZ != null){
                predicates.add(criteriaBuilder.equal(locationJoin.get("z"), this.locationZ));
            }
        }
        return predicates;
    }

    public boolean validateSortField(String field) {
        return (
                Objects.equals(field, "id")
                        || Objects.equals(field, "name")
                        || Objects.equals(field, "creationDate")
                        || Objects.equals(field, "passportID")
                        || Objects.equals(field, "height")
                        || Objects.equals(field, "nationality")
                        || Objects.equals(field, "hairColor")
                        || Objects.equals(field, "coordinatesX")
                        || Objects.equals(field, "coordinatesY")
                        || Objects.equals(field, "locationX")
                        || Objects.equals(field, "locationY")
                        || Objects.equals(field, "locationZ")
        );
    }

    public boolean validatePageIDX(Integer page) {
        return page > 0;
    }

    public boolean validatePageSize(Integer size) {
        return size > 0;
    }

    public ValidatorMessage validateParams() {
        ValidatorMessage validatorMessage = new ValidatorMessage();
        try {
            List<String> errorParams = new ArrayList<>();
            if (!validatePageIDX(this.pageIdx)) {
                errorParams.add("pageIdx");
            }
            if (!validatePageSize(this.pageSize)) {
                errorParams.add("pageSize");
            }
            if (!validateSortField(this.sortField)) {
                errorParams.add("sortField");
            }
            if (errorParams.size() > 0) {
                validatorMessage.setMessage("Unsupported value of params: " + String.join(", ", errorParams));
            } else {
                validatorMessage.setStatus(true);
            }
            return validatorMessage;
        } catch (Exception e) {
            validatorMessage.setMessage("Unsupported param, error: " + e.getMessage());
            return validatorMessage;
        }
    }

    public Response getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }
}

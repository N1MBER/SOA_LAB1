package com.example.back.validator;

import com.example.back.stringEntity.Person;
import lombok.Getter;
import com.example.back.converter.FieldConverter;
import com.example.back.utils.PersonParams;

@Getter
public class Validator {
    public static ValidatorResult validatePerson(Person stringPerson){
        ValidatorResult validatorResult = new ValidatorResult();

        FieldConverter.stringConvert(stringPerson.getName(), "Person Name", validatorResult);

        FieldConverter.colorConvert(stringPerson.getHairColor(), "Person hairColor", validatorResult);
        FieldConverter.countryConvert(stringPerson.getNationality(), "Person nationality", validatorResult);
        if (stringPerson.getCoordinates() != null){
            FieldConverter.intConvert(stringPerson.getCoordinates().getX(), "Coordinates X", validatorResult);
            FieldConverter.doubleConvert(stringPerson.getCoordinates().getY(), "Coordinates Y", validatorResult);
        } else {
            validatorResult.addMessage("Coordinates fields are not specified");
        }

        return validatorResult;
    }

    public static void validateCreationDate(Person stringPerson, ValidatorResult validatorResult){
        FieldConverter.localDateTimeConvert(stringPerson.getCreationDate(), "Person CreationDate", PersonParams.DATE_PATTERN, validatorResult);
    }

    public static void validateId(Person stringPerson, ValidatorResult validatorResult){
        Long labWorkId = FieldConverter.longConvert(stringPerson.getId(), "Person Id", validatorResult);
        if(labWorkId != null && labWorkId <= 0){
            validatorResult.addMessage("Person id must be more than 0");
        }
    }
}

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
        FieldConverter.stringConvert(stringPerson.getPassportID(), "Passport id", validatorResult);
        FieldConverter.colorConvert(stringPerson.getHairColor(), "Person hairColor", validatorResult);
        FieldConverter.eyeColorConvert(stringPerson.getEyeColor(), "Person eyeColor", validatorResult);
        FieldConverter.countryConvert(stringPerson.getNationality(), "Person nationality", validatorResult);
        FieldConverter.floatConvertPositive(stringPerson.getHeight(), "Height", validatorResult);
        if (stringPerson.getCoordinates() != null){
            FieldConverter.intConvert(stringPerson.getCoordinates().getX(), "Coordinates X", validatorResult);
            FieldConverter.doubleConvert(stringPerson.getCoordinates().getY(), "Coordinates Y", validatorResult);
        } else {
            validatorResult.addMessage("Coordinates fields are not specified");
        }
        if (stringPerson.getLocation() != null) {
            FieldConverter.longConvert(stringPerson.getLocation().getX(), "Location X", validatorResult);
            FieldConverter.longConvert(stringPerson.getLocation().getY(), "Location Y", validatorResult);
            FieldConverter.longConvert(stringPerson.getLocation().getZ(), "Location Z", validatorResult);
        } else {
            validatorResult.addMessage("Location fields are not specified");
        }

        return validatorResult;
    }

    public static void validateCreationDate(Person stringPerson, ValidatorResult validatorResult){
        FieldConverter.localDateTimeConvert(stringPerson.getCreationDate(), "Person CreationDate", PersonParams.DATE_PATTERN, validatorResult);
    }

    public static void validateId(Person stringPerson, ValidatorResult validatorResult){
        Long personId = FieldConverter.longConvert(stringPerson.getId(), "Person Id", validatorResult);
        if(personId != null && personId <= 0){
            validatorResult.addMessage("Person id must be more than 0");
        }
    }

}

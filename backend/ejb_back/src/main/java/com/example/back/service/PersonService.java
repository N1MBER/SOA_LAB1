package com.example.back.service;

import com.example.back.DAO.PersonsDAO;
import com.example.back.converter.FieldConverter;
import com.example.back.converter.XMLConverter;
import com.example.back.entity.Person;
import com.example.back.utils.PersonParams;
import com.example.back.utils.PersonsResults;
import com.example.back.utils.ResponseWrapper;
import com.example.back.utils.ServerResponse;
import com.example.back.validator.Validator;
import com.example.back.validator.ValidatorResult;
import lombok.SneakyThrows;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

@Stateless
public class PersonService implements PersonI, Serializable {
    private static final String NAME_PARAM = "name";
    private static final String CREATION_DATE_PARAM = "creationDate";
    private static final String PASSPORT_ID_PARAM = "passportID";
    private static final String HEIGHT_PARAM = "height";
    private static final String NATIONALITY_PARAM = "nationality";
    private static final String HAIR_COLOR_PARAM = "hairColor";
    private static final String EYE_COLOR_PARAM = "eyeColor";
    private static final String COORDINATES_X_PARAM = "coordinatesX";
    private static final String COORDINATES_Y_PARAM = "coordinatesY";
    private static final String PAGE_IDX_PARAM = "pageIdx";
    private static final String PAGE_SIZE_PARAM = "pageSize";
    private static final String SORT_FIELD_PARAM = "sortField";
    private static final String LOCATION_X_PARAM = "locationX";
    private static final String LOCATION_Y_PARAM = "locationY";
    private static final String LOCATION_Z_PARAM = "locationZ";

    private static final String MIN_NATIONALITY = "minNationality";
    private static final String MORE_HEIGHT = "moreHeight";
    private static final String LESS_LOCATION = "lessLocation";

    private XMLConverter xmlConverter;
    private PersonsDAO dao;

    public PersonService(){
        xmlConverter = new XMLConverter();
        dao = new PersonsDAO();
    }

    private PersonParams getPersonParams(HashMap<String, String> map) {
        PersonParams params = new PersonParams();
        params.setPersonParams(
                map.get(NAME_PARAM),
                map.get(CREATION_DATE_PARAM),
                map.get(COORDINATES_X_PARAM),
                map.get(COORDINATES_Y_PARAM),
                map.get(PASSPORT_ID_PARAM),
                map.get(HEIGHT_PARAM),
                map.get(LOCATION_X_PARAM),
                map.get(LOCATION_Y_PARAM),
                map.get(LOCATION_Z_PARAM),
                map.get(HAIR_COLOR_PARAM),
                map.get(EYE_COLOR_PARAM),
                map.get(NATIONALITY_PARAM),
                map.get(PAGE_IDX_PARAM),
                map.get(PAGE_SIZE_PARAM),
                map.get(SORT_FIELD_PARAM)
        );
        return params;
    }

    public ResponseWrapper getAllPersons(HashMap<String, String> map){
        PersonParams personParams = getPersonParams(map);
        return getPersons(personParams);
    }

    public ResponseWrapper getPersons(PersonParams params){
        if (!params.getValidatorResult().isStatus()){
            return getInfo(400, params.getValidatorResult().getMessage());
        }
        try {
            PersonsResults personsResults = dao.getAllPersons(params);
            return new ResponseWrapper(xmlConverter.toStr(personsResults));
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getMinNationality(){
        try{
            Person person = dao.getMinNationality();
            if (person == null){
                return getInfo(404, "No person");
            }
            return new ResponseWrapper(xmlConverter.toStr(person));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo( 500, "Server error, try again");
        }
    }

    @SneakyThrows
    public ResponseWrapper getPerson(String str_id){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "Person Id", validatorResult);
        if (!validatorResult.isStatus()){
            return getInfo( 400, validatorResult.getMessage());
        }
        try {
            Optional<Person> person = dao.getPerson(id);
            if (person.isPresent()){
                return new ResponseWrapper(xmlConverter.toStr(person.get()));
            } else {
                return getInfo( 404, "No person with such id: " + id);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper createPerson(String xmlStr){
        try {
            com.example.back.stringEntity.Person stringPerson = xmlConverter.fromStr(xmlStr, com.example.back.stringEntity.Person.class);
            ValidatorResult validatorResult = Validator.validatePerson(stringPerson);
            if (!validatorResult.isStatus()){
                return getInfo(400, validatorResult.getMessage());
            }
            Person person = xmlConverter.fromStr(xmlStr, Person.class);
            Long id = dao.createPerson(person);
            return new ResponseWrapper(200);
        } catch (JAXBException e){
            System.out.println(e.getMessage());
            return getInfo(400, "Unknown data structure");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper updatePerson(String str_id, String xmlStr){
        try {
            com.example.back.stringEntity.Person stringPerson = xmlConverter.fromStr(xmlStr, com.example.back.stringEntity.Person.class);
            ValidatorResult validatorResult = Validator.validatePerson(stringPerson);
            Long id = FieldConverter.longConvert(str_id, "Person id", validatorResult);
            if (!validatorResult.isStatus()){
                return getInfo(400, validatorResult.getMessage());
            }
            Person personUpdate = xmlConverter.fromStr(xmlStr, Person.class);
            Optional<Person> person = dao.getPerson(id);
            if (person.isPresent()) {
                Person personPresent = person.get();
                personPresent.update(personUpdate);
                dao.updatePerson(personPresent);
                return new ResponseWrapper(200);
            } else return getInfo(404, "No Person with such id: " + personUpdate.getId());
        } catch (JAXBException e){
            return getInfo(400, "Can't understand data structure");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo( 500, "Server error, try again");
        }
    }

    public ResponseWrapper deletePerson(String str_id){
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);

            if (!validatorResult.isStatus()){
                return getInfo(400, validatorResult.getMessage());
            }

            boolean result = dao.deletePerson(id, validatorResult);
            if (!result) return getInfo(validatorResult.getCode(), validatorResult.getMessage());
            else {
                return new ResponseWrapper(200);
            }
        }  catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    @Override
    public ResponseWrapper getLessLocation(HashMap<String, String> map) {
        PersonParams personParams = getPersonParams(map);
        personParams.setLessLocationFlag(true);
        return getPersons(personParams);
    }

    public ResponseWrapper countMoreHeight(String str_height) {
        ValidatorResult validatorResult = new ValidatorResult();
        Integer height = FieldConverter.intConvert(str_height, "Person height", validatorResult);

        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Long count = dao.countMoreHeight(height);
            if (count != null){
                return getInfo(200, "Count of persons with greater height than: " + str_height + " is: " + count);
            } else {
                return getInfo(500, "Not found persons for this height: " + str_height);
            }
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return new ResponseWrapper(code, xmlConverter.toStr(serverResponse));
    }

}

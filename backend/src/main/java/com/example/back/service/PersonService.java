package com.example.back.service;

import com.example.back.DAO.PersonsDAO;
import com.example.back.converter.FieldConverter;
import com.example.back.converter.XMLConverter;
import com.example.back.entity.Person;
import com.example.back.utils.PersonParams;
import com.example.back.utils.PersonsResults;
import com.example.back.utils.ServerResponse;
import com.example.back.validator.Validator;
import com.example.back.validator.ValidatorResult;
import lombok.SneakyThrows;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.util.Optional;

public class PersonService {
    private XMLConverter xmlConverter;
    private PersonsDAO dao;

    public PersonService(){
        xmlConverter = new XMLConverter();
        dao = new PersonsDAO();
    }

    public Response getAllPersons(PersonParams params){
        try {
            PersonsResults personsResults = dao.getAllPersons(params);
            return Response.ok(xmlConverter.toStr(personsResults)).build();
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public Response getMinNationality(){
        try{
            Person person = dao.getMinNationality();
            if (person == null){
                return getInfo(404, "No person");
            }
            return Response.ok(xmlConverter.toStr(person)).build();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo( 500, "Server error, try again");
        }
    }

    @SneakyThrows
    public Response getPerson(String str_id){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "Person Id", validatorResult);
        if (!validatorResult.isStatus()){
            return getInfo( 400, validatorResult.getMessage());
        }
        try {
            Optional<Person> person = dao.getPerson(id);
            if (person.isPresent()){
                return Response.ok(xmlConverter.toStr(person.get())).build();
            } else {
                return getInfo( 404, "No person with such id: " + id);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    public Response createPerson(String xmlStr){
        try {
            com.example.back.stringEntity.Person stringPerson = xmlConverter.fromStr(xmlStr, com.example.back.stringEntity.Person.class);
            ValidatorResult validatorResult = Validator.validatePerson(stringPerson);
            if (!validatorResult.isStatus()){
                return getInfo(400, validatorResult.getMessage());
            }
            Person person = xmlConverter.fromStr(xmlStr, Person.class);
            Long id = dao.createPerson(person);
            return Response.ok().build();
        } catch (JAXBException e){
            System.out.println(e.getMessage());
            return getInfo(400, "Unknown data structure");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    public Response updatePerson(String str_id, String xmlStr){
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
                return Response.ok().build();
            } else return getInfo(404, "No Person with such id: " + personUpdate.getId());
        } catch (JAXBException e){
            return getInfo(400, "Can't understand data structure");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo( 500, "Server error, try again");
        }
    }

    public Response deletePerson(String str_id){
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);

            if (!validatorResult.isStatus()){
                return getInfo(400, validatorResult.getMessage());
            }

            boolean result = dao.deletePerson(id, validatorResult);
            if (!result) return getInfo(validatorResult.getCode(), validatorResult.getMessage());
            else return Response.ok().build();
        }  catch (Exception e){
            System.out.println(e.getMessage());
            return getInfo(500, "Server error, try again");
        }
    }

    public Response countMoreHeight(String str_height) {
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

    public Response getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

}

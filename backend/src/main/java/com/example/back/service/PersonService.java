package com.example.back.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import com.example.back.utils.ServerResponse;
import com.example.back.validator.Validator;
import com.example.back.validator.ValidatorResult;
import lombok.SneakyThrows;
import com.example.back.DAO.PersonsDAO;
import com.example.back.converter.FieldConverter;
import com.example.back.converter.XMLConverter;
import com.example.back.entity.Person;
import com.example.back.utils.PersonParams;
import com.example.back.utils.PersonsResults;

import java.io.PrintWriter;
import java.util.Optional;

public class PersonService {
    private XMLConverter xmlConverter;
    private PersonsDAO dao;

    public PersonService(){
        xmlConverter = new XMLConverter();
        dao = new PersonsDAO();
    }

    public void getAllPersons(PersonParams params, HttpServletResponse response){
        try {
            PersonsResults personsResults = dao.getAllPersons(params);
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(xmlConverter.toStr(personsResults));
        } catch (Exception e){
            this.getInfo(response, 500, "Server error, try again");
        }
    }

    @SneakyThrows
    public void getPerson(String str_id, HttpServletResponse response){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "Person Id", validatorResult);
        if (!validatorResult.isStatus()){
            this.getInfo(response, 400, validatorResult.getMessage());
            return;
        }
        try {
            Optional<Person> person = dao.getPerson(id);
            if (person.isPresent()){
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(xmlConverter.toStr(person.get()));
            } else {
                this.getInfo(response, 404, "No person with such id: " + id);
            }
        } catch (Exception e){
            this.getInfo(response, 500, "Server error, try again");
        }
    }

    public void getError(HttpServletResponse response){

    }

    public void createPerson(HttpServletRequest request, HttpServletResponse response){
        try {
            String xmlStr = FieldConverter.bodyToStringConvert(request);
            com.example.back.stringEntity.Person stringPerson = xmlConverter.fromStr(xmlStr, com.example.back.stringEntity.Person.class);
            ValidatorResult validatorResult = Validator.validatePerson(stringPerson);
            if (!validatorResult.isStatus()){
                getInfo(response, 400, validatorResult.getMessage());
                return;
            }
            Person person = xmlConverter.fromStr(xmlStr, Person.class);
            Long id = dao.createPerson(person);
            response.setStatus(200);
        } catch (JAXBException e){
            System.out.println(e.getMessage());
            this.getInfo(response, 400, "Unknown data structure");
        }
        catch (Exception e){
            this.getInfo(response, 500, "Server error, try again");
        }
    }

    public void updatePerson(String str_id, HttpServletRequest request, HttpServletResponse response){
        try {
            String xmlStr = FieldConverter.bodyToStringConvert(request);
            com.example.back.stringEntity.Person stringPerson = xmlConverter.fromStr(xmlStr, com.example.back.stringEntity.Person.class);
            ValidatorResult validatorResult = Validator.validatePerson(stringPerson);
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);
            Validator.validateCreationDate(stringPerson, validatorResult);
            Validator.validateId(stringPerson, validatorResult);
            if (!validatorResult.isStatus()){
                getInfo(response, 400, validatorResult.getMessage());
                return;
            }
            Person personUpdate = xmlConverter.fromStr(xmlStr, Person.class);
            Optional<Person> person = dao.getPerson(id);
            if (person.isPresent()) {
                Person personPresent = person.get();
                personPresent.update(personUpdate);
                dao.updatePerson(personPresent);
                response.setStatus(200);
            } else getInfo(response, 404, "No Person with such id: " + personUpdate.getId());
        } catch (JAXBException e){
            this.getInfo(response, 400, "Can't understand data structure");
        }
        catch (Exception e){
            this.getInfo(response, 500, "Server error, try again");
        }
    }

    public void deletePerson(String str_id, HttpServletResponse response){
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);

            if (!validatorResult.isStatus()){
                this.getInfo(response, 400, validatorResult.getMessage());
                return;
            }

            boolean result = dao.deletePerson(id, validatorResult);
            if (!result) getInfo(response, validatorResult.getCode(), validatorResult.getMessage());
            else response.setStatus(200);
        }  catch (Exception e){
            this.getInfo(response, 500, "Server error, try again");
        }
    }

    public void getInfo(HttpServletResponse response, int code, String message){
        try {
            ServerResponse serverResponse = new ServerResponse(message);
            PrintWriter writer = response.getWriter();
            writer.write(xmlConverter.toStr(serverResponse));
            response.setStatus(code);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }

    }
}

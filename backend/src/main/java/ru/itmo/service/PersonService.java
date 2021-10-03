package ru.itmo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.itmo.DAO.PersonsDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.Person;
import ru.itmo.utils.PersonParams;
import ru.itmo.utils.PersonsResults;

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
            PersonsResults personResult = dao.getAllPersons(params);
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(xmlConverter.toStr(personResult));
        } catch (Exception e){
            this.getError(response);
        }
    }

    @SneakyThrows
    public void getPerson(Long id, HttpServletResponse response){
        if (id == null){
            this.getError(response);
            return;
        }
        Optional<Person> person = dao.getPerson(id);
        if (person.isPresent()){
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write(xmlConverter.toStr(person.get()));
        } else {
            this.getError(response);
        }
    }


    public void getError(HttpServletResponse response){

    }

    public void createPerson(HttpServletRequest request, HttpServletResponse response){
        try {
            String xmlStr = FieldConverter.bodyToStringConvert(request);
            Person person = xmlConverter.fromStr(xmlStr, Person.class);
            Long id = dao.createPerson(person);
            response.setStatus(200);
            response.getWriter().write(xmlConverter.toStr(id));
        } catch (Exception e){
            getError(response);
        }
    }

    public void updatePerson(HttpServletRequest request, HttpServletResponse response){
        try {
            Person personUpdate = xmlConverter.fromStr(FieldConverter.bodyToStringConvert(request), Person.class);
            Optional<Person> person = dao.getPerson(personUpdate.getId());
            if (person.isPresent()) {
                Person personPresent = person.get();
                personPresent.update(personUpdate);
                dao.updatePerson(personPresent);
                response.setStatus(200);
            } else getError(response);
        } catch (Exception e){
            getError(response);
        }
    }
}

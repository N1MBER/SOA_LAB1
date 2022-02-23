package com.example.back.service;

import com.example.back.converter.XMLConverter;
import com.example.back.utils.PersonResultsDTO;
import com.example.back.utils.ServerResponse;
import com.example.back.validator.ValidatorResult;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonService {
    private XMLConverter xmlConverter;

    public PersonService(){
        xmlConverter = new XMLConverter();
    }

    public Response eyeCount(String str_eye) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }
        try{
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/api/persons?pageSize=1&pageIdx=1&sortField=id&eyeColor=" + str_eye);
            String str = target.request().accept(MediaType.APPLICATION_XML).get(String.class);
            PersonResultsDTO personResultsDTO = xmlConverter.fromStr(str, PersonResultsDTO.class);
            return getInfo(200, "Count of persons with this eye color " + str_eye + " is: " + personResultsDTO.getTotalPersons());
        } catch (Exception e){
            return getInfo(500, e.getMessage() + " " + e.getLocalizedMessage());
        }
    }

    public Response countEyeColorWithNationality(String str_eye, String str_nationality) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/api/persons?pageSize=1&page=1&sortField=id&eyeColor=" + str_eye);
            PersonResultsDTO personResultsDTO = target.request().get().readEntity(PersonResultsDTO.class);
            return getInfo(200, "Count of persons with this eye color and nationality is: " + personResultsDTO.getTotalPersons());
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public Response getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

}

package com.example.back.service;

import com.example.back.DAO.PersonsDAO;
import com.example.back.converter.FieldConverter;
import com.example.back.converter.XMLConverter;
import com.example.back.entity.Country;
import com.example.back.entity.EyeColor;
import com.example.back.utils.ServerResponse;
import com.example.back.validator.ValidatorResult;

import javax.ws.rs.core.Response;

public class PersonService {
    private XMLConverter xmlConverter;
    private PersonsDAO dao;

    public PersonService(){
        xmlConverter = new XMLConverter();
        dao = new PersonsDAO();
    }

    public Response eyeCount(String str_eye) {
        ValidatorResult validatorResult = new ValidatorResult();
        EyeColor eyeColor = FieldConverter.eyeColorConvert(str_eye, "Person eyeColor", validatorResult);

        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Long count = dao.countEyeColor(eyeColor);
            if (count != null){
                return getInfo(200, "Count of persons with this eye color " + str_eye + " is: " + count);
            } else {
                return getInfo(500, "Not found persons for this eye color: " + str_eye);
            }
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public Response countEyeColorWithNationality(String str_eye, String str_nationality) {
        ValidatorResult validatorResult = new ValidatorResult();
        EyeColor eyeColor = FieldConverter.eyeColorConvert(str_eye, "Person eyeColor", validatorResult);
        Country nationality = FieldConverter.countryFilterConvert(str_nationality, "Person nationality", validatorResult);

        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Long count = dao.countEyeColorWithNationality(eyeColor, nationality);
            if (count != null){
                return getInfo(200, "Count of persons with this eye color and nationality is: " + count);
            } else {
                return getInfo(500, "Not found persons for this height: " + str_eye);
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

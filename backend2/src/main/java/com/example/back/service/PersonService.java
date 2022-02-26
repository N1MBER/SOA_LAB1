package com.example.back.service;

import com.example.back.converter.XMLConverter;
import com.example.back.utils.PersonResultsDTO;
import com.example.back.utils.ServerResponse;
import com.example.back.validator.ValidatorResult;
import lombok.SneakyThrows;
import org.glassfish.jersey.SslConfigurator;

import javax.naming.InitialContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HostnameVerifier;
import javax.naming.Context;


public class PersonService {
    private XMLConverter xmlConverter;


    @SneakyThrows
    public PersonService(){
        xmlConverter = new XMLConverter();
    }

    public Response eyeCount(String str_eye) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }
        try{
            Client client = createClientBuilderSSL();
            WebTarget target = client.target("https://localhost:8081/back/api/persons?pageSize=1&pageIdx=1&sortField=id&eyeColor=" + str_eye);
            Response response = target.request().accept(MediaType.APPLICATION_XML).get(Response.class);
            String str = response.readEntity(String.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()){
                try {
                    PersonResultsDTO personResultsDTO = xmlConverter.fromStr(str, PersonResultsDTO.class);
                    return getInfo(200, "Count of persons with this eye color " + str_eye + " is: " + personResultsDTO.getTotalPersons());
                } catch (Exception e) {
                    return getInfo(500, str + ", Error: " + e.getMessage());
                }
            }
            else {
                ServerResponse serverResponse = xmlConverter.fromStr(str, ServerResponse.class);
                return getInfo(500, serverResponse.getMessage());
            }
        } catch (Exception e){
            return getInfo(500, "Server error, try again. Error:" + e.getMessage());
        }
    }

    public Response countEyeColorWithNationality(String str_eye, String str_nationality) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Client client = createClientBuilderSSL();
            WebTarget target = client.target("https://localhost:8081/back/api/persons?pageSize=1&pageIdx=1&sortField=id&eyeColor=" + str_eye + "&nationality=" + str_nationality);
            Response response = target.request().accept(MediaType.APPLICATION_XML).get(Response.class);
            String str = response.readEntity(String.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()){
                try {
                    PersonResultsDTO personResultsDTO = xmlConverter.fromStr(str, PersonResultsDTO.class);
                    return getInfo(200, "Count of persons with this eye color and nationality is: " + personResultsDTO.getTotalPersons());
                } catch (Exception e) {
                    return getInfo(500, str + ", Error: " + e.getMessage());
                }
            }
            else {
                ServerResponse serverResponse = xmlConverter.fromStr(str, ServerResponse.class);
                return getInfo(500, serverResponse.getMessage());
            }
        } catch (Exception e){
            return getInfo(500, "Server error, try again. Error:" + e.getMessage());
        }
    }

    public Response getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return Response.status(code).entity(xmlConverter.toStr(serverResponse)).build();
    }

    private Client createClientBuilderSSL() {
        SSLContext sslContext = SslConfigurator.newInstance()
                .keyPassword("soasoa")
                .trustStorePassword("soasoa")
                .createSSLContext();
        HostnameVerifier hostnameVerifier = (hostname, sslSession) -> {
            System.out.println(" hostname = " + hostname);
            if (hostname.equals("localhost")) {
                return true;
            }
            return false;
        };
        return ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(hostnameVerifier)
                .build();
    }
}

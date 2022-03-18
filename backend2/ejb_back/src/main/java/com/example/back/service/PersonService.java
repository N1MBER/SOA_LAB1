package com.example.back.service;

import com.example.back.converter.XMLConverter;
import com.example.back.utils.PersonResultsDTO;
import com.example.back.utils.ResponseWrapper;
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
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
public class PersonService implements ServiceI, Serializable {
    private XMLConverter xmlConverter;

    private String backFirst;
    private String hostname;
    private String trustPassword;
    private String keyPassword;
    private String api;
    private String persons;
    private String params = "persons?pageSize=1&pageIdx=1&sortField=id&eyeColor=";

    @SneakyThrows
    public PersonService(){
        Context env = (Context)new InitialContext().lookup("java:comp/env");
        backFirst = (String)env.lookup("uri");
        hostname = (String)env.lookup("hostname");
        trustPassword = (String)env.lookup("keyPassword");
        keyPassword = (String)env.lookup("trustPassword");
        api = (String)env.lookup("api");
        persons = (String)env.lookup("persons");
        xmlConverter = new XMLConverter();
    }

    public ResponseWrapper getInfo(int code, String message){
        ServerResponse serverResponse = new ServerResponse(message);
        return new ResponseWrapper(code, xmlConverter.toStr(serverResponse));
    }

    public WebTarget getTarget() {
        URI uri = UriBuilder.fromUri(backFirst).build();
        Client client = createClientBuilderSSL();
        return client.target(uri).path(api).path(persons);
    }

    private Client createClientBuilderSSL() {
        SSLContext sslContext = SslConfigurator.newInstance()
                .keyPassword(keyPassword)
                .trustStorePassword(trustPassword)
                .createSSLContext();
        HostnameVerifier hostnameVerifier = (hostname, sslSession) -> {
            System.out.println(" hostname = " + hostname);
            if (hostname.equals(this.hostname)) {
                return true;
            }
            return false;
        };
        return ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(hostnameVerifier)
                .build();
    }

    @Override
    public ResponseWrapper getEyeColorCount(String string_eye_color) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }
        try{
            System.out.println(getTarget().path(params + string_eye_color).toString());
            Response response = getTarget()
                    .queryParam("pageSize", 1)
                    .queryParam("pageIdx", 1)
                    .queryParam("sortField", "id")
                    .queryParam("eyeColor", string_eye_color)
                    .request().accept(MediaType.APPLICATION_XML).get(Response.class);
            String str = response.readEntity(String.class);
            if (response.getStatus() == Response.Status.OK.getStatusCode()){
                try {
                    PersonResultsDTO personResultsDTO = xmlConverter.fromStr(str, PersonResultsDTO.class);
                    return getInfo(200, "Count of persons with this eye color " + string_eye_color + " is: " + personResultsDTO.getTotalPersons());
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
        }    }

    @Override
    public ResponseWrapper getEyeColorNationalityCount(String string_eye_color, String string_nationality) {
        ValidatorResult validatorResult = new ValidatorResult();
        if (!validatorResult.isStatus()){
            return getInfo(400, validatorResult.getMessage());
        }

        try{
            Response response = getTarget()
                    .queryParam("pageSize", 1)
                    .queryParam("pageIdx", 1)
                    .queryParam("sortField", "id")
                    .queryParam("eyeColor", string_eye_color)
                    .queryParam("nationality", string_nationality)
                    .request().accept(MediaType.APPLICATION_XML).get(Response.class);
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
        }    }
}

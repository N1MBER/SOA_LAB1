package com.example.back.controller;


import com.example.back.service.PersonI;
import com.example.back.service.RemoteBeanLookup;
import com.example.back.utils.ResponseWrapper;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;

@Path("")
public class PersonController {
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
    private static final String MORE_HEIGHT = "moreHeight/{height}";
    private static final String LESS_LOCATION = "lessLocation";
    private static final String GET_ID = "/persons/{id}";

    private PersonI service;
    @Context
    ServletContext servletContext;

    private Response unwrap(ResponseWrapper responseWrapper){
        return Response.status(responseWrapper.getCode()).entity(responseWrapper.getPayload()).build();
    }


    public PersonController() {
        service = RemoteBeanLookup.lookupRemoteStatelessBean();
    }

    @GET
    @Path("/persons/" + MORE_HEIGHT)
    public Response countMoreHeight(@PathParam("height") String height) {
        return unwrap(service.countMoreHeight(height));
    }

    @GET
    @Path("/persons/" + MIN_NATIONALITY)
    public Response getMinNationality(@Context UriInfo uri) {
        return unwrap(service.getMinNationality());
    }

    @GET
    @Path("/persons/" + LESS_LOCATION)
    public Response getLessLocation(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        HashMap<String, String> hashMap = new HashMap<>();
        map.forEach((key, value) -> hashMap.put(key, value.get(0)));
        return unwrap(service.getAllPersons(hashMap));
    }

    @GET
    @Path(GET_ID)
    public Response getPerson(@PathParam("id") String id){
        return unwrap(service.getPerson(id));
    }

    @POST
    @Path("/persons")
    public Response createPerson(String person){
        return unwrap(service.createPerson(person));
    }

    @PUT
    @Path(GET_ID)
    public Response updatePerson(@PathParam("id") String id, String person){
        return unwrap(service.updatePerson(id, person));
    }

    @DELETE
    @Path(GET_ID)
    public Response deletePerson(@PathParam("id") String id){
        return unwrap(service.deletePerson(id));
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }

    @GET
    @Path("/persons")
    public Response getAllPersons(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        HashMap<String, String> hashMap = new HashMap<>();
        map.forEach((key, value) -> hashMap.put(key, value.get(0)));
        return unwrap(service.getAllPersons(hashMap));

    }
}

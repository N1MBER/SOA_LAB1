package com.example.back.servlet;


import com.example.back.service.PersonService;
import com.example.back.utils.PersonParams;

import com.example.back.validator.ValidatorMessage;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("")
public class PersonServlet {
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

    private PersonService service;
    @Context
    ServletContext servletContext;

    private PersonParams getPersonParams(MultivaluedMap<String, String> map){
        return new PersonParams(
                map.getFirst(NAME_PARAM),
                map.getFirst(CREATION_DATE_PARAM),
                map.getFirst(COORDINATES_X_PARAM),
                map.getFirst(COORDINATES_Y_PARAM),
                map.getFirst(PASSPORT_ID_PARAM),
                map.getFirst(HEIGHT_PARAM),
                map.getFirst(EYE_COLOR_PARAM),
                map.getFirst(LOCATION_X_PARAM),
                map.getFirst(LOCATION_Y_PARAM),
                map.getFirst(LOCATION_Z_PARAM),
                map.getFirst(HAIR_COLOR_PARAM),
                map.getFirst(NATIONALITY_PARAM),
                map.getFirst(PAGE_IDX_PARAM),
                map.getFirst(PAGE_SIZE_PARAM),
                map.getFirst(SORT_FIELD_PARAM)
        );
    }

    public PersonServlet() {
        service = new PersonService();
    }

    @GET
    @Path("/persons/" + MORE_HEIGHT)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response getMoreHeight(@PathParam("height") String height) {
        return service.countMoreHeight(height);
    }

    @GET
    @Path("/persons/" + MIN_NATIONALITY)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response getMinNationality(@Context UriInfo uri) {
        return service.getMinNationality();
    }

    @GET
    @Path("/persons/" + LESS_LOCATION)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response getLessLocation(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        PersonParams filterParams = getPersonParams(map);
        filterParams.setLessLocationFlag(true);
        return service.getAllPersons(filterParams);
    }

    @GET
    @Path(GET_ID)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response getPerson(@PathParam("id") String id){
        return service.getPerson(id);
    }

    @POST
    @Path("/persons")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response createLabWork(String person){
        return service.createPerson(person);
    }

    @PUT
    @Path(GET_ID)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response changeLabWork(@PathParam("id") String id, String person){
        return service.updatePerson(id, person);
    }

    @DELETE
    @Path(GET_ID)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response deletePerson(@PathParam("id") String id){
        return service.deletePerson(id);
    }

    @OPTIONS
    @Path("{path : .*}")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response options() {
        return Response.ok()
                .build();
    }

    @GET
    @Path("/persons")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response getPersons(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        PersonParams filterParams = getPersonParams(map);
        ValidatorMessage validatorMessage = filterParams.validateParams();
        if (validatorMessage.getStatus()) {
            return service.getAllPersons(filterParams);
        } else {
            return filterParams.getInfo(400, validatorMessage.getMessage());
        }
    }
}

package com.example.back.servlet;


import com.example.back.service.PersonService;
import com.example.back.utils.PersonParams;

import com.example.back.converter.FieldConverter;
import com.example.back.service.PersonService;
import com.example.back.utils.PersonParams;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/persons")
public class PersonServlet {
    private static final String NAME_PARAM = "name";
    private static final String CREATION_DATE_PARAM = "creationDate";
    private static final String PASSPORT_ID_PARAM = "passportID";
    private static final String HEIGHT_PARAM = "height";
    private static final String NATIONALITY_PARAM = "nationality";
    private static final String HAIR_COLOR_PARAM = "hairColor";
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

    private PersonService service;

    @Context
    ServletContext servletContext;

    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    private PersonParams getPersonParams(MultivaluedMap<String, String> map){
        return new PersonParams(
                map.getFirst(NAME_PARAM),
                map.getFirst(CREATION_DATE_PARAM),
                map.getFirst(COORDINATES_X_PARAM),
                map.getFirst(COORDINATES_Y_PARAM),
                map.getFirst(PASSPORT_ID_PARAM),
                map.getFirst(HEIGHT_PARAM),
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
    public Response getPersons(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        PersonParams filterParams = getPersonParams(map);
        return service.getAllPersons(filterParams);
    }

    @GET
    @Path("/{id}")
    public Response getPerson(@PathParam("id") String id){
        return service.getPerson(id);
    }

    @GET
    @Path(MORE_HEIGHT)
    public Response getMoreHeight(@PathParam("height") String height) {
        return service.countMoreHeight(height);
    }

    @GET
    @Path(MIN_NATIONALITY)
    public Response getMinNationality(@Context UriInfo uri) {
        return service.getMinNationality();
    }

    @GET
    @Path(LESS_LOCATION)
    public Response getLessLocation(@Context UriInfo uri) {
        MultivaluedMap<String, String> map = uri.getQueryParameters();
        PersonParams filterParams = getPersonParams(map);
        filterParams.setLessLocationFlag(true);
        return service.getAllPersons(filterParams);
    }

    @POST
    public Response createLabWork(String person){
        return service.createPerson(person);
    }

    @PUT
    @Path("/{id}")
    public Response changeLabWork(@PathParam("id") String id, String person){
        return service.updatePerson(id, person);
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") String id){
        return service.deletePerson(id);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }
}

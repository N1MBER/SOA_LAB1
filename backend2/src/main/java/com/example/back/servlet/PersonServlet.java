package com.example.back.servlet;


import com.example.back.entity.EyeColor;
import com.example.back.service.PersonService;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("")
public class PersonServlet {
    private PersonService service;
    @Context
    ServletContext servletContext;


    public PersonServlet() {
        service = new PersonService();
    }

    @GET
    @Path("/eye-color/{eyeColor}/")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEyeColorCount(@PathParam("eyeColor") String eyeColor) {
        return service.eyeCount(eyeColor);
    }

    @GET
    @Path("/eye-color/{eyeColor}/nationality/{nationality}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEyeColorNationalityCount(@PathParam("eyeColor") String eyeColor,
                                                @PathParam("nationality") String nationality) {
        return service.countEyeColorWithNationality(eyeColor, nationality);
    }


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.status(202)
                .build();
    }
}

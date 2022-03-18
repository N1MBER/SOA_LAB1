package com.example.back.controller;

import com.example.back.service.RemoteBeanLookup;
import com.example.back.service.ServiceI;
import com.example.back.utils.ResponseWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
public class PersonController {
    private ServiceI service;

    public PersonController() {
        service = RemoteBeanLookup.lookupRemoteStatelessBean();
    }

    private Response unwrap(ResponseWrapper responseWrapper){
        return Response.status(responseWrapper.getCode()).entity(responseWrapper.getPayload()).build();
    }

    @GET
    @Path("/eye-color/{eyeColor}/")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEyeColorCount(@PathParam("eyeColor") String eyeColor) {
        return unwrap(service.getEyeColorCount(eyeColor));
    }

    @GET
    @Path("/eye-color/{eyeColor}/nationality/{nationality}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getEyeColorNationalityCount(@PathParam("eyeColor") String eyeColor,
                                                @PathParam("nationality") String nationality) {
        return unwrap(service.getEyeColorNationalityCount(eyeColor, nationality));
    }


    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.status(202)
                .build();
    }
}

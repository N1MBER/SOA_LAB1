package com.example.back;


import com.example.back.service.PersonService;
import com.example.back.utils.PersonParams;

import com.example.back.converter.FieldConverter;
import com.example.back.service.PersonService;
import com.example.back.utils.PersonParams;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="persons", value="/persons/*")
public class PersonServlet extends HttpServlet {
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
    private static final String MORE_HEIGHT = "moreHeight";
    private static final String LESS_LOCATION = "lessLocation";

    private PersonService service;

    private PersonParams getPersonParams(HttpServletRequest request){
        return new PersonParams(
                request.getParameter(NAME_PARAM),
                request.getParameter(CREATION_DATE_PARAM),
                request.getParameter(COORDINATES_X_PARAM),
                request.getParameter(COORDINATES_Y_PARAM),
                request.getParameter(PASSPORT_ID_PARAM),
                request.getParameter(HEIGHT_PARAM),
                request.getParameter(LOCATION_X_PARAM),
                request.getParameter(LOCATION_Y_PARAM),
                request.getParameter(LOCATION_Z_PARAM),
                request.getParameter(HAIR_COLOR_PARAM),
                request.getParameter(NATIONALITY_PARAM),
                request.getParameter(PAGE_IDX_PARAM),
                request.getParameter(PAGE_SIZE_PARAM),
                request.getParameter(SORT_FIELD_PARAM)
        );
    }

    @Override
    public void init() {
        service = new PersonService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/xml");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null){
            PersonParams filterParams = getPersonParams(req);
            service.getAllPersons(filterParams, resp);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                switch (parts[1]) {
                    case LESS_LOCATION:
                        PersonParams parameters = getPersonParams(req);
                        parameters.setLessLocationFlag(true);
                        service.getAllPersons(parameters, resp);
                        break;
                    case MIN_NATIONALITY:
                        service.getMinNationality(resp);
                        break;
                    case MORE_HEIGHT:
                        service.countMoreHeight(parts[2], resp);
                        break;
                    default: {
                        String[] params = pathInfo.split("=");
                        if (params.length > 1) {
                            PersonParams personParams = getPersonParams(req);
                            service.getAllPersons(personParams, resp);
                        } else {
                            service.getPerson(parts[1], resp);
                        }
                        break;
                    }
                }
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        service.createPerson(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/ml");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                service.updatePerson(parts[1], req, resp);
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        } else {
            service.getInfo(resp, 400, "Unknown query");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] parts = pathInfo.split("/");
            if (parts.length > 1) {
                service.deletePerson(parts[1], resp);
            } else {
                service.getInfo(resp, 400, "Unknown query");
            }
        } else {
            service.getInfo(resp, 400, "Unknown query");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        resp.setStatus(200);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doHead(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doTrace(req, resp);
    }
}

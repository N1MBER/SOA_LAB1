package com.example.back.service;

import com.example.back.utils.ResponseWrapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Properties;

public class RemoteBeanLookup {

    public static PersonI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            final javax.naming.Context context = new InitialContext(jndiProperties);
            final String appName = (String) env.lookup("appName");
            final String moduleName = (String) env.lookup("moduleName");
            final String beanName = (String) env.lookup("beanName");
            final String viewClassName = PersonI.class.getName();
            final String scope = (String) env.lookup("scope");
            String lookupName = scope + ":" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;

            return (PersonI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new PersonI() {

                @Override
                public ResponseWrapper getAllPersons(HashMap<String, String> map) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getPerson(String str_id) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }


                @Override
                public ResponseWrapper createPerson(String xmlStr) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper updatePerson(String str_id, String xmlStr) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper deletePerson(String str_id) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getLessLocation(HashMap<String, String> map) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getMinNationality() {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper countMoreHeight(String str_height) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }
            };
        }
    }
}

package com.example.back.service;


import com.example.back.utils.ResponseWrapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteBeanLookup {

    public static ServiceI lookupRemoteStatelessBean() {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
        try {
            Context env = (Context)new InitialContext().lookup("java:comp/env");
            final Context context = new InitialContext(jndiProperties);
            final String appName = (String) env.lookup("appName");
            final String moduleName = (String) env.lookup("moduleName");
            final String beanName = (String) env.lookup("beanName");
            final String viewClassName = ServiceI.class.getName();
            final String scope = (String) env.lookup("scope");
            String lookupName = scope + ":" + appName + "/" + moduleName + "/" + beanName + "!" + viewClassName;
            System.out.println(lookupName);
            return (ServiceI) context.lookup(lookupName);
        } catch (NamingException e) {
            System.out.println("не получилось (");
            e.printStackTrace();
            return new ServiceI() {

                @Override
                public ResponseWrapper getEyeColorCount(String string_eye_color) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }

                @Override
                public ResponseWrapper getEyeColorNationalityCount(String string_eye_color, String string_nationality) {
                    return new ResponseWrapper(500, "Server error, try again!");
                }
            };
        }
    }
}
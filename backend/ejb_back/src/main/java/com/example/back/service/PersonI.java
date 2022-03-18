package com.example.back.service;


import com.example.back.utils.ResponseWrapper;

import javax.ejb.Remote;
import java.util.HashMap;

@Remote
public interface PersonI {
    public ResponseWrapper getAllPersons(HashMap<String, String> map);
    public ResponseWrapper getPerson(String str_id);
    public ResponseWrapper createPerson(String xmlStr);
    public ResponseWrapper updatePerson(String str_id, String xmlStr);
    public ResponseWrapper deletePerson(String str_id);
    public ResponseWrapper getLessLocation(HashMap<String, String> map);
    public ResponseWrapper getMinNationality();
    public ResponseWrapper countMoreHeight(String str_height);
}

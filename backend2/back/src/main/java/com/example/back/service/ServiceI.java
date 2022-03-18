package com.example.back.service;

import com.example.back.utils.ResponseWrapper;

import javax.ejb.Remote;

@Remote
public interface ServiceI {
    public ResponseWrapper getEyeColorCount(String string_eye_color);
    public ResponseWrapper getEyeColorNationalityCount(String string_eye_color, String string_nationality);
}

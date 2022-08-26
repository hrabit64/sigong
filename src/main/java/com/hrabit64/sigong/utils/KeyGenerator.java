package com.hrabit64.sigong.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KeyGenerator {

    public String generate(){

        return UUID.randomUUID().toString().replace("-", "");
    }

}

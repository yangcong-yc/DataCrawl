package com.yangcong.datacrawl;

import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TetsConfig {


    @Value(value = "${network.cookie_value}")
    private static String COOKIE_VALUE ;
    @Value(value = "${network.cookie_name}")
    private static String COOKIE_NAME ;


    public static void main(String[] args) {


     String s = "PRESTOLITE;PUROLATOR;SKY TRAK;TAKEUCHI;TCM;TENNANT;TEREX;TOYOTA;UNICARRIER;VOLKSWAGEN;WIX / AIR REFINER;YALE;YANG;";
        int purolator = s.indexOf("PUROLATOR");


        System.err.println(s.substring(purolator));


    }
}

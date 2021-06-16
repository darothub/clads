package com.decagon.clads.utils;



public class ConstantUtils {
    public static final String GENDER_PATTERN = "[Mm]ale|[Ff]emale";
    public static final String CATEGORY_PATTERN = "[Tt]ailor|[Ff]ashionista";
    public static final String CHAR_PATTERN = "[a-zA-z\\s]+";
    public static final String PHONE_NUMBER_PATTERN = "^0(80|70|90|81)([12356709])\\d{7}$";
    public static final String PASSWORD = "^(?=.*\\\\d) (?=\\\\S+$)(?=.* [@#$%^&+=])(?=.* [a-z]) (?=.* [A-Z]).{8,10}$";
    public static final String IMAGE_PATTERN = "[a-zA-z0-9]+.(png|jpg|jpeg)";
    public static final String BASE_URL = "/api/v1";
    public static final String CLIENT_ID ="962135252978-2lr1b25bpmrjcahu6u106kgimeagl0sp.apps.googleusercontent.com";
}

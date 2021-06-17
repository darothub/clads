package com.decagon.clads.utils;



public class ConstantUtils {
    public static final String GENDER_PATTERN = "[Mm]ale|[Ff]emale";
    public static final String CATEGORY_PATTERN = "[Tt]ailor|[Ff]ashionista";
    public static final String CHAR_PATTERN = "[a-zA-z\\s]+";
    public static final String PHONE_NUMBER_PATTERN = "^0(80|70|90|81)([12356709])\\d{7}$";
    public static final String PASSWORD = "^(?=.*\\\\d) (?=\\\\S+$)(?=.* [@#$%^&+=])(?=.* [a-z]) (?=.* [A-Z]).{8,10}$";
    public static final String IMAGE_PATTERN = "[a-zA-z0-9]+.(png|jpg|jpeg)";
    public static final String BASE_URL = "/api/v1";
    public static final String CLIENT_ID ="525232674561-10iuvisduhg0ebrjgksg0cpd7mdlr0nj.apps.googleusercontent.com";
    public static final String LOCAL_HOST = "http://localhost:8080/api/v1/";
    public static final String PRODUCTION_HOST = "https://clads-service.herokuapp.com/api/v1/";
}

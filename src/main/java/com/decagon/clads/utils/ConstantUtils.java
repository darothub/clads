package com.decagon.clads.utils;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ConstantUtils {
    public static final String GENDER_PATTERN = "[Mm]ale|[Ff]emale";
    public static final String CATEGORY_PATTERN = "[Tt]ailor|[Ff]ashionista";
    public static final String PHONE_NUMBER_PATTERN = "^0(80|70|90|81)([12356709])\\d{7}$";
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String BASE_URL = "/api/v1";
    public static final String CLIENT_ID ="525232674561-10iuvisduhg0ebrjgksg0cpd7mdlr0nj.apps.googleusercontent.com";
    @Value("${host.base}")
    public String host;
}

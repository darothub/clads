package com.decagon.clads.utils;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ConstantUtils {
    public static final String GENDER_PATTERN = "[Mm]ale|[Ff]emale";
    public static final String PHONE_NUMBER_PATTERN = "^0(80|70|90|81)([12356709])\\d{7}$";
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String BASE_URL = "/api/v1";
    public static final String ARTISAN_CLIENT_ID ="525232674561-jqs92483fpb2j2dv6qustgik08rkkoiu.apps.googleusercontent.com";
    public static final String CUSTOMER_CLIENT_ID ="972466068443-t81qt5slfbh7unh7s4l5o72907jhfrn1.apps.googleusercontent.com";
    public static final String IMAGE_PATTERN = "image\\/(png|jpg|jpeg)";
    @Value("${host.base}")
    public String host;
}

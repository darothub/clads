package com.decagon.clads.filter;

import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.services.ArtisanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Data
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {


    private JWTUtility jwtUtility;

    private ArtisanService artisanService;

    public static String  token = null;
    public static String userName = null;
    public static Long userId = 0L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");


        try{
            if (null != authorization && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                userName = jwtUtility.getEmailAddressFromToken(token);
                userId = jwtUtility.getIdFromToken(token);
            }
            else{
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.toString(), "You are not authorized");
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), error);
            }

        }
        catch (Exception e){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.toString(), e.getMessage());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }
        try{
            if (0 != userId && null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("token "+token + "\n" + "userId " + userId);

                UserDetails userDetails = artisanService.loadUserByUsername(userName);
                log.info("UserDTO {}", userDetails);
                if (jwtUtility.validateToken(token, userDetails)) {
                    log.info("token is valid");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            }

            filterChain.doFilter(request, response);

        }
        catch (Exception e){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR), "Internal server error");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Pattern.compile("/api/v1/(artisans|confirm|login)").matcher(path).matches();
    }
}

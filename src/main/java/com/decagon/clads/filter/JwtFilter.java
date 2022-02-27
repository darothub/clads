package com.decagon.clads.filter;

import com.decagon.clads.customer.service.CustomerService;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.artisans.services.auth.ArtisanService;
import com.decagon.clads.utils.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Data
@Component
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtility jwtUtility;
    private final ArtisanService artisanService;
    private final CustomerService customerService;

    public static String  token = null;
    public static String userName = null;
    public static Long userId = 0L;
    public static Role role;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("jwt filter");
        String authorization = request.getHeader(AUTHORIZATION);

        try{
            if (null != authorization && authorization.startsWith("Bearer ")) {
                token = authorization.substring("Bearer ".length()); 
                userName = jwtUtility.getEmailAddressFromToken(token);
                userId = jwtUtility.getIdFromToken(token);
                role = Role.valueOf(jwtUtility.getRoleFromToken(token));
            }
            else{
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), Optional.of("You are not authorized"));
                final ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getOutputStream(), error);
            }

        }
        catch (Exception e){
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }
        try{
            UserDetails userDetails;
            if (null != userName && role.equals(Role.TAILOR) && SecurityContextHolder.getContext().getAuthentication() == null) {
                userDetails = artisanService.loadUserByUsername(userName);
                validateToken(request, response, filterChain, userDetails);
            }
            else if(null != userName && role.equals(Role.CLIENT) && SecurityContextHolder.getContext().getAuthentication() == null){
                userDetails = customerService.loadUserByUsername(userName);
                validateToken(request, response, filterChain, userDetails);
            }

        }
        catch (Exception e){

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ErrorResponse error = new ErrorResponse(response.getStatus(), String.valueOf(response.getStatus()), e.getLocalizedMessage());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }

    }

    private void validateToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, UserDetails userDetails) throws IOException, ServletException {
        if (jwtUtility.validateToken(token, userDetails)) {

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
                    );
            usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Pattern.compile("/api/v1/(artisan/register|artisan/login|customer/register|customer/login|confirm|login/google|home|download/image/*|message|conversations)").matcher(path).find();
    }
}

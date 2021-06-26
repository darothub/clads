package com.decagon.clads.filter;

import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.model.response.ErrorResponse;
import com.decagon.clads.services.ArtisanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@NoArgsConstructor
@Component
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class JwtFilter extends OncePerRequestFilter {

    private JWTUtility jwtUtility;

    private ArtisanService artisanService;

    @Autowired
    public JwtFilter(JWTUtility jwtUtility, ArtisanService artisanService){
        this.artisanService = artisanService;
        this.jwtUtility = jwtUtility;
    }

    public static String  token = null;
    public static String userName = null;
    public static Long userId = 0L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("jwt filter");
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
            if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
//                log.info("token "+token + "\n");

                UserDetails userDetails = artisanService.loadUserByUsername(userName);
//                log.info("UserDTO {}{}", userDetails, userDetails.getPassword());
                if (jwtUtility.validateToken(token, userDetails)) {
                    log.info("token is valid");
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
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
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ErrorResponse error = new ErrorResponse(response.getStatus(), String.valueOf(response.getStatus()), e.getLocalizedMessage());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), error);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Pattern.compile("/api/v1/(artisans/register|confirm|login|login/google|home)").matcher(path).matches();
    }
}

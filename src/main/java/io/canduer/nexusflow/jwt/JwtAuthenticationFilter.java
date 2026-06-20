package io.canduer.nexusflow.jwt;

import io.canduer.nexusflow.auth.Impl.CustomUserDetails;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.exception.UserNotFoundException;
import io.canduer.nexusflow.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import io.jsonwebtoken.security.SignatureException;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
          try {
              System.out.println("HttpServletRequest in JwtAuthenticationFilter:" + request.getRequestURI());
              if(request.getRequestURI().startsWith("/h2-console")) {
                  System.out.println("Skipping JWT filter for H2");
                  filterChain.doFilter(request, response);
                  return;
              }
              String token = null;
              SecurityContext securityContext = SecurityContextHolder.getContext();
              //token : header.payload.signature :xxxx.yyyy.zzzz
              //step 1-2: extract headers
              String header = request.getHeader("Authorization");

              //step 3-4: extract user and token
              if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
                  token = header.substring(7);
                  //extract username which is email
                  String username= jwtService.extractUsername(token);
                  //load user from DB
                  User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("user not found with this credentials"));

                  //validate the user
                  UserDetails userDetails = new CustomUserDetails(user);
                  //validate the token
                  boolean tokenValid = jwtService.isTokenValid(token, userDetails);
                  if(tokenValid) {
                      //re create authentication object
                      //extract : principal, credentials and authorities
                      Authentication authenticationIdentity = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                      //set into security context
                      if(securityContext.getAuthentication() == null){
                          securityContext.setAuthentication(authenticationIdentity);
                      }
                  }
              }

              filterChain.doFilter(request, response);
          } catch (UserNotFoundException ex) {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json");

              response.getWriter()
                      .write("{\"message\":\"User not found with given credentials\"}");

              return;
          } catch (ExpiredJwtException ex) {
              // 401
              response.setStatus(401);
              response.setContentType("application/json");
              response.getWriter()
                      .write("{\"message\":\"Token expired\"}");
              return;
          } catch ( SignatureException ex) {
              // 401
              response.setStatus(401);
              response.setContentType("application/json");
              response.getWriter()
                      .write("{\"message\":\"Invalid token\"}");
              return;
          } catch (MalformedJwtException ex) {
              response.setStatus(401);
              response.setContentType("application/json");
              response.getWriter()
                      .write("{\"message\":\"Malformed token\"}");
              return;
          }
    }
}

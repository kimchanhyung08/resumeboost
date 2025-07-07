package org.project.resumeboost.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    Gson gson = new Gson();

    String jsonStr = gson.toJson(Map.of("error", "ERROR_LOGIN"));

    response.setContentType("application/json");

    PrintWriter printWriter = response.getWriter();
    printWriter.println(jsonStr);
    printWriter.close();
  }
}

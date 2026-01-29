package com.okayji.identity.service;

import com.okayji.identity.dto.request.AuthenticationRequest;
import com.okayji.identity.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    void logout(HttpServletRequest request, HttpServletResponse response);
}

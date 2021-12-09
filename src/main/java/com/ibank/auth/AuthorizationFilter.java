package com.ibank.auth;

import com.ibank.common.ErrorResponse;
import com.ibank.config.SecurityConstants;
import com.ibank.user.User;
import com.ibank.utils.JwtUtil;
import com.ibank.utils.ObjectJson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * En esta clase se comprueba que las peticiones de acceso a los recursos estan autorizadas.
 * Para ello, en cada peticion se comprueba el token enviado por el usuario y se valida.
 */
@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final ErrorResponse errorResponse;

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
        this.errorResponse = new ErrorResponse();
    }

    /**
     * Este metodo se encarga de extraer el token de la peticion
     *
     * @param request La peticion recibida donde se buscara el token
     * @return El String que contiene el token o un null si no lo encuentra
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = null;

        // 1. Se busca en la url el parametro "token"
        if (request.getParameter("token") != null) {
            return request.getParameter("token");
        }

        // 2. Buscamos el token en la cabecera "Authorization" (HEADER_AUTHORIZATION_KEY)
        if (request.getHeader(SecurityConstants.HEADER_AUTHORIZACION_KEY) != null) {
            token = request.getHeader(SecurityConstants.HEADER_AUTHORIZACION_KEY);

            // Si el token comienza con "Bearer token"
            if (token.startsWith(SecurityConstants.TOKEN_BEARER_PREFIX)) {
                token = token.split(" ")[1];
            }

            return token;
        }

        // 3. Se busca el token en el body json ...

        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // Si la peticion es a una URL que no requiere autorizacion ...
        if (request.getRequestURI().equals("/api/v1/login") || request.getRequestURI().equals("/api/v1/signup")) {
            // Procesamos la peticion sin necesidad de buscar el token
            chain.doFilter(request, response);
            return;
        }

        // A partir de aqui la autorizacion es necesaria, por lo que buscamos el token de seguridad
        String token = this.getTokenFromRequest(request);

        // Si no existe un token no se puede continuar, enviamos un mensaje de respuesta
        if (token == null) {
            // Creamos una respuesta con el error
            errorResponse.errorMessage = "Security token not found in the header request or url";

            // Transformamos la respuesta en json y procesamos la peticion
            response.getWriter().write(ObjectJson.toJson(errorResponse));
            chain.doFilter(request, response);
            return;
        }

        // A partir de aqui el token ha sido encontrado, procemos a validar el token
        User userToken = null;

        // El header contiene el token con el formato correcto ... comenzamos la validacion del token
        try {
            // Obtenemos el nombre de usuario del token cifrado
            userToken = JwtUtil.parseToken(token);
        } catch (MalformedJwtException e) {
            // El formato del token es invalido
            errorResponse.errorMessage = "Token format is not correct.";
        } catch (SignatureException e) {
            // La firma del token es invalidad (token modificado)
            errorResponse.errorMessage = "Invalid signature.";
        } catch (ExpiredJwtException e) {
            // El token expiro ya que el tiempo de vida ha pasado.
            errorResponse.errorMessage = "Token expired.";
        }

        // Si el userToken es nulo, hubo un fallo al parsear el token
        if (userToken == null) {
            response.getWriter().write(ObjectJson.toJson(errorResponse));
            chain.doFilter(request, response);
            return;
        }

        // Reconstruimos el objeto "Authentication" a partir del token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userToken, null, new ArrayList<>()
        );

        // Autorizamos al usuario
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Si no se produjo ninguna excepcion nuestra peticion quedara autorizada.
        // Pasamos la peticion al siguiente filtro
        chain.doFilter(request, response);
    }
}

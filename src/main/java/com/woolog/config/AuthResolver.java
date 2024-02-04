package com.woolog.config;

import com.woolog.config.data.UserSession;
import com.woolog.controller.AuthController;
import com.woolog.domain.Session;
import com.woolog.exception.Unauthorized;
import com.woolog.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info(">>>", appConfig);

        String jws = webRequest.getHeader("Authorization");
        if(jws == null || jws.equals("")){
            throw new Unauthorized();
        }


        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    .parseSignedClaims(jws);
            log.info(">>>>>>{}", claimsJws);


            String userId = claimsJws.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }


//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

//
//        if(servletRequest == null){
//            log.error("servletRequest null");
//            throw new Unauthorized();
//        }
//        Cookie[] cookies = servletRequest.getCookies();
//        if(cookies.length == 0){
//            log.error("cookie 없음");
//            throw new Unauthorized();
//        }
//
//        String accessToken = cookies[0].getValue();


        // DB 연계 확인 작업
//        Session session = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);

//        return new UserSession(session.getUser().getId());
    }
}

package com.woolog.config;

import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WoologMockSecurityContext.class)
public @interface WoologMockUser {

    String email() default "gw8413@gmail.com";

    String name() default "크림";

    String password() default "12345";

//    String role() default "ROLE_ADMIN";

}

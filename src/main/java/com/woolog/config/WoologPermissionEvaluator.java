package com.woolog.config;

import com.woolog.domain.Post;
import com.woolog.exception.PostNotFound;
import com.woolog.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class WoologPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Post post = postRepository.findById((Long) targetId).orElseThrow(PostNotFound::new);
        log.info("post.getUserId ={}", post.getUserId());
        log.info("principal.getUserId={}", principal.getUserId());
        return post.getUserId().equals(principal.getUserId());
    }
}

package com.woolog.repository;

import com.woolog.domain.Session;
import com.woolog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {

}

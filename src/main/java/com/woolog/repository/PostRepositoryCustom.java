package com.woolog.repository;

import com.woolog.domain.Post;
import com.woolog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);


}

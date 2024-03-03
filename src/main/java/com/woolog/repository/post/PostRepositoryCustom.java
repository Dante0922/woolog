package com.woolog.repository.post;

import com.woolog.domain.Post;
import com.woolog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);


}

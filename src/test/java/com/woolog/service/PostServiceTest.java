package com.woolog.service;

import com.woolog.domain.Post;
import com.woolog.exception.PostNotFound;
import com.woolog.repository.PostRepository;
import com.woolog.request.PostCreate;
import com.woolog.request.PostEdit;
import com.woolog.request.PostSearch;
import com.woolog.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertThat(postCreate.getTitle()).isEqualTo("제목입니다");
        assertThat(postCreate.getContent()).isEqualTo("내용입니다");
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test2() throws Exception {
        //given

        Post post = Post.builder()
                .title("아자아자")
                .content("힘내자")
                .build();
        postRepository.save(post);

        //when
        PostResponse result
                = postService.get(post.getId());

        //then
        assertNotNull(result);
        assertEquals("아자아자", result.getTitle());
        assertEquals("힘내자", result.getContent());
    }


    @Test
    @DisplayName("글 페이징 조회")
    void test3() throws Exception {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("우로그 제목 " + i)
                            .content("우로그 내용 " + i)
                            .build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);


//        Pageable pageable = PageRequest.of(0, 5);
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);
        //then
        assertEquals(10L, posts.size());
        assertEquals("우로그 제목 30", posts.get(0).getTitle());
        assertEquals("우로그 내용 26", posts.get(4).getContent());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() throws Exception {
        //given


        Post post = Post.builder()
                .title("이건우")
                .content("성공하자!")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이크림")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + post.getId()));
        assertEquals("이크림",changedPost.getTitle());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        //given
        Post post = Post.builder()
                .title("이건우")
                .content("성공하자!")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());
    }
    @Test
    @DisplayName("글 단건 조회 실패")
    void test7() throws Exception {
        //given

        Post post = Post.builder()
                .title("아자아자")
                .content("힘내자")
                .build();
        postRepository.save(post);

        //expected
         assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        }, "예외처리가 잘못 되었습니다.");
    }

    @Test
    @DisplayName("게시글 삭제 실패")
    void test8() {
        //given
        Post post = Post.builder()
                .title("이건우")
                .content("성공하자!")
                .build();

        postRepository.save(post);

        //when
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        }, "예외처리가 잘못 되었습니다.");
    }

    @Test
    @DisplayName("글 제목 수정 실패")
    void test9() throws Exception {
        //given


        Post post = Post.builder()
                .title("이건우")
                .content("성공하자!")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이크림")
                .build();

        //when

        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        }, "예외처리가 잘못 되었습니다.");

    }
}
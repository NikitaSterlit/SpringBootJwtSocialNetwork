package com.nikitasterlit.jwtauthentication.repository;

import com.nikitasterlit.jwtauthentication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findListByUserProfileId (Long userProfileId);
    Post getPostById (Long id);
}

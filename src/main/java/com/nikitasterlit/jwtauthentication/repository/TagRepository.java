package com.nikitasterlit.jwtauthentication.repository;

import com.nikitasterlit.jwtauthentication.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository <Tag, Long> {

}

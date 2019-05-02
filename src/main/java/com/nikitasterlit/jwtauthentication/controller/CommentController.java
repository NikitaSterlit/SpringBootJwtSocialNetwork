package com.nikitasterlit.jwtauthentication.controller;


import com.nikitasterlit.jwtauthentication.model.Comment;
import com.nikitasterlit.jwtauthentication.model.Post;
import com.nikitasterlit.jwtauthentication.model.User;
import com.nikitasterlit.jwtauthentication.model.UserProfile;
import com.nikitasterlit.jwtauthentication.repository.CommentRepository;
import com.nikitasterlit.jwtauthentication.repository.PostRepository;
import com.nikitasterlit.jwtauthentication.repository.UserProfileRepository;
import com.nikitasterlit.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @GetMapping("/api/comment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Comment> getComment() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
        List<Post> postList = postRepository.findListByUserProfileId(userProfile.getId());
        List<Comment> listComment = commentRepository.findAll();
        List<Comment> listComment2 = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);//
            for (int j = 0; j < listComment.size(); j++) {
                if (post.getId() == listComment.get(j).getId()) {
                    System.out.println(listComment.get(j));
                    listComment2.add(listComment.get(j));
                }
            }
        }

        return listComment2;
    }

    @GetMapping("/api/comment/user/another/{anotheruserid}")
    public List<Comment> getListAnotherUserId(@PathVariable("anotheruserid") long id) {
        User user = userRepository.findById(id).get();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
        List<Post> postList = postRepository.findListByUserProfileId(userProfile.getId());
        List<Comment> listComment = commentRepository.findAll();
        List<Comment> listcommen2 = new ArrayList<>();
        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            for (int j = 0; j < listComment.size(); j++) {
                if (post.getId() == listComment.get(j).getId())
                    listcommen2.add(listComment.get(j));
            }
        }
        return listcommen2;
    }

    @GetMapping("/api/comment/all")
    public List<Comment> getCommentAll() {
        //Authentication autentication = SecurityContextHolder.getContext().getAuthentication(); НЕ НУЖНО
        //User user = userRepository.findByUsername(autentication.getName()).get();
        List<Comment> commentList = commentRepository.findAll();
        return commentList;
    }
   // @PostMapping("/api/comment/")
   // public Comment postComment(@PathVariable ("postid")long id, @RequestBody Comment comment){
   //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   //     User user = userRepository.findByUsername(authentication.getName()).get();
   //     UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
   //     List<Post> postList = postRepository.
//
   // }
}

        // Post - контроллер дописать все методы
    // Comment - контроллер дописать все методы
    // создать еще пользователей, добавить новые данные(посты, комментарии) и с ними проделать операции

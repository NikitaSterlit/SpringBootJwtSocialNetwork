package com.nikitasterlit.jwtauthentication.controller;


import com.nikitasterlit.jwtauthentication.model.Post;
import com.nikitasterlit.jwtauthentication.model.Tag;
import com.nikitasterlit.jwtauthentication.model.User;
import com.nikitasterlit.jwtauthentication.model.UserProfile;
import com.nikitasterlit.jwtauthentication.repository.PostRepository;
import com.nikitasterlit.jwtauthentication.repository.TagRepository;
import com.nikitasterlit.jwtauthentication.repository.UserProfileRepository;
import com.nikitasterlit.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    TagRepository tagRepository;


    @GetMapping("/api/post/all")
    public List<Post> getAllPosts(){return postRepository.findAll();}

    @GetMapping("/api/post/my")
    public List<Post> getPostsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = user.getUserProfile();
        List<Post> listPost = postRepository.findAll();
        return listPost;
    }
    @GetMapping("/api/post/{userid}")
    public List<Post> getPostByUserId(@PathVariable("userid") long id) {
       // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(id).get();
        UserProfile userProfile = user.getUserProfile();
        List<Post> listPost = postRepository.findListByUserProfileId(userProfile.getId());
        return listPost;
    }

    @PostMapping("/api/allpost")
    public Post addPost(@RequestBody Post post){return postRepository.save(post);}

    @PostMapping("/api/post")
    public Post postPostByUser(@RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = user.getUserProfile();
        post.setUserProfile(userProfile);
        return postRepository.save(post);
    }

    @PostMapping("/api/post/tag/{tagid}")
    public Post addPostByTag (@PathVariable ("tagid")long id, @RequestBody Post post){ // в TAG добавляем POST
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
        post.setUserProfile(userProfile);
        Tag tag = tagRepository.findById(id).get(); // находим нужный TAG
        Set<Tag> setTag = new HashSet<>(); // создаем множество TAG
        setTag.add(tag); // добавляем найденный TAG
        post.setTags(setTag); // добавляем в POST список обьектов TAG
        return postRepository.save(post); // сохраняем POST в БД
    }
    @PutMapping("/api/post/{postid}")
    public Post putPost(@PathVariable("postid") long id, @RequestBody Post post) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //User user = userRepository.findByUsername(authentication.getName()).get();
        //UserProfile userProfile = user.getUserProfile();
        //List<Post> postList = postRepository.findListByUserProfileId(userProfile.getId());
        Post post1 = postRepository.getPostById(id);
        post1.setContent(post.getContent());
        post1.setDescription(post.getDescription());
        post1.setTitle(post.getTitle());
        return postRepository.save(post1);
    }

   // @PutMapping("/api/post/{postid}/tag/{tagid}")
   // public Post pustPostByTagId(@RequestBody Post post, @PathVariable ("tagid") long tagId, @PathVariable ("postid")long Postid){
   //    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
   //    //User user = userRepository.findByUsername(user.getName()).get();
   //    //UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
   //    //List<Post> = userProfile.get
   //    //if(tag.getPosts()!= null)
   //     Tag tag = tagRepository.findById(tagId).get();
   //     Set<Post> postSet = tag.getPosts();
   //
   // }
    @DeleteMapping("/api/post/{postid}")
    public String deletePost(@PathVariable("postid") long id) {
        Post post = postRepository.getPostById(id);
        postRepository.delete(post);
        return "Post has been delete";
    }
}

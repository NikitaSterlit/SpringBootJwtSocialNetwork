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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class TagController {


    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;


    @GetMapping("/api/alltag")
    public List<Tag> getAllTag() {
        return tagRepository.findAll();
    }

    @PostMapping ("/api/tag/{tagid}/{postid}")
    public Post addTagToPost(@PathVariable ("tagid")long tagId, @PathVariable ("postid")long postId){
        Post post = postRepository.findById(postId).get();
        Set<Tag> setTag = post.getTags();
        Tag tag = tagRepository.findById(tagId).get();
        setTag.add(tag);
        post.setTags(setTag);
        return postRepository.save(post);
    }
    @PostMapping("/api/tag")
    public Tag postTag(@RequestBody Tag tag){return tagRepository.save(tag);}

    @GetMapping("/api/tag")
    public List<Tag> getTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).get();
        List<Post> listPosts = postRepository.findListByUserProfileId(userProfile.getId());
        List<Tag> listTag = tagRepository.findAll();
        List<Tag> listTag2 = new ArrayList<>();
        for (int i = 0; i < listPosts.size(); i++) {
            Post post = listPosts.get(i);
            for (int j = 0; j < listTag.size(); j++){
                if (post.getId() == listTag.get(j).getId());
                listTag2.add(listTag.get(j));
            }
        }
        return listTag2;
    }


   @PostMapping("/api/tag/{postid}")
   public Post postTag(@PathVariable("postid")long id, @RequestBody Tag tag){
       Post post = postRepository.getPostById(id);
       Set<Tag> tagSet = post.getTags();
       tagSet.add(tag);
       post.setTags(tagSet);
       return postRepository.save(post);
   }

    @PutMapping("/api/tag/{tagid}")
    public Tag putTagById(@PathVariable("tagid") long id, @RequestBody Tag tag) {
        Tag tag1 = tagRepository.findById(id).get();
        tag1.setName(tag.getName());
        tag1.setPosts(tag.getPosts());
        return tagRepository.save(tag1);
    }

    @DeleteMapping("/api/tag/{tagid}")
    public String deleteTag(@PathVariable ("tagid")long id){
        tagRepository.deleteById(id);
        return "Tag has been delete";
    }
}

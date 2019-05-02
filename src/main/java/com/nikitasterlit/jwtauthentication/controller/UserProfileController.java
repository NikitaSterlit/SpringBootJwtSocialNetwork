package com.nikitasterlit.jwtauthentication.controller;

import com.nikitasterlit.jwtauthentication.model.User;
import com.nikitasterlit.jwtauthentication.model.UserProfile;
import com.nikitasterlit.jwtauthentication.repository.UserProfileRepository;
import com.nikitasterlit.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) //удаленный доступ к серверу
public class UserProfileController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @GetMapping("/api/userprofile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") // доступные роли по адресу
    public UserProfile userAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        return user.getUserProfile();
    }
    @PostMapping("/api/userprofile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserProfile getUserProfile(@RequestBody UserProfile userProfile){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        user.setUserProfile(userProfile);
        userProfile.setUser(user);
        return userRepository.save(user).getUserProfile();
    }
    @PutMapping("/api/userprofile/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public UserProfile putUserProfile(@PathVariable ("id")long id, @RequestBody UserProfile userProfile){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile1 = userProfileRepository.findById(id).get();
        userProfile1.setAddress1(userProfile.getAddress1());
        userProfile1.setAddress2(userProfile.getAddress2());
        userProfile1.setCity(userProfile.getCity());
        userProfile1.setCountry(userProfile.getCountry());
        userProfile1.setDateOfBirth(userProfile.getDateOfBirth());
        return userProfile1;
    }
    @DeleteMapping("/api/userprofile/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String deleteUserProfile(@PathVariable ("id")long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).get();
        UserProfile userProfile = userProfileRepository.findById(id).get();
        userProfileRepository.delete(userProfile);
        return "UserRepository by " + id +" is delete";
    }
}

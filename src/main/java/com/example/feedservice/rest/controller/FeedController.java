package com.example.feedservice.rest.controller;

import org.keycloak.KeycloakPrincipal;
import com.example.feedservice.dto.FeedDTO;
import com.example.feedservice.dto.UserDTO;
import com.example.feedservice.service.FeedService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/feed")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody String text, @RequestBody(required = false) MultipartFile[] files, KeycloakAuthenticationToken principal) throws IOException {
        FeedDTO feedDTO = new FeedDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername((principal).getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
        feedDTO.setUser(userDTO);
        feedDTO.setText(text);
        feedDTO.setDate(LocalDateTime.now());
        if(!Objects.isNull(files)) {
            feedDTO.setContent(feedService.saveContent(files));
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody FeedDTO feedDTO, KeycloakAuthenticationToken principal) {
        if (!feedService.getFeedOwner(feedDTO).equals((principal).getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        feedService.deleteFeed(feedDTO);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping(path = "/")
    public List<FeedDTO> getFeed(KeycloakAuthenticationToken principal) throws IOException {
        return feedService.getFeeds((principal).getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername());
    }

    @GetMapping(path = "/{username}")
    public List<FeedDTO> getFeedsByUsername(@PathVariable String username){
        return feedService.getUserFeeds(username);
    }

    @GetMapping(path = "/test")
    public String getTest()  {
        return "Шел мужик по парку. Нашел шляпу, примерил, а она ему как два!";
    }
}

package com.example.feedservice.rest.controller;

import org.keycloak.KeycloakPrincipal;
import com.example.feedservice.dto.FeedDTO;
import com.example.feedservice.dto.UserDTO;
import com.example.feedservice.service.FeedService;
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

@RestController
@Validated
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody String text, @RequestBody MultipartFile[] files, Principal principal) throws IOException {
        FeedDTO feedDTO = new FeedDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(((KeycloakPrincipal) principal).getKeycloakSecurityContext().getToken().getPreferredUsername());
        feedDTO.setUser(userDTO);
        feedDTO.setText(text);
        feedDTO.setDate(LocalDateTime.now());
        feedDTO.setContent(feedService.saveContent(files));
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody FeedDTO feedDTO, Principal principal) {
        if (!feedService.getFeedOwner(feedDTO).equals(((KeycloakPrincipal) principal).getKeycloakSecurityContext().getToken().getPreferredUsername())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        feedService.deleteFeed(feedDTO);
        return new ResponseEntity(HttpStatus.OK);

    }

    @GetMapping
    public List<FeedDTO> getFeed(Principal principal) throws IOException {
        return feedService.getFeeds(((KeycloakPrincipal) principal).getKeycloakSecurityContext().getIdTokenString());
    }

    @GetMapping(path = "/test")
    public String getFeed()  {
        return "feedService.getFeeds(((KeycloakPrincipal) principal).getKeycloakSecurityContext().getToken().getPreferredUsername());";
    }
}

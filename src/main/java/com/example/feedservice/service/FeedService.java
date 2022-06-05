package com.example.feedservice.service;

import com.example.feedservice.dto.FeedDTO;
import com.example.feedservice.dto.NewFeedDTO;
import com.example.feedservice.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FeedService {

    void createFeed(FeedDTO feedDTO);

    void deleteFeed(FeedDTO feedDTO);

    List<FeedDTO> getFeeds(String username) throws IOException;

    void saveContent(List<MultipartFile> files);


}
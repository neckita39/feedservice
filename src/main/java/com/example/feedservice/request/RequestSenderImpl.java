package com.example.feedservice.request;


import com.example.feedservice.dto.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class RequestSenderImpl implements RequestSender{
    @Value("${USER_SERVICE_URL}")
    private String userServiceUrl;
    private final OkHttpClient httpClient = new OkHttpClient();

    public List<UserDTO> getPublishers(String username) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Request request = new Request.Builder()
                .url(userServiceUrl + "/profile/get/subscribes/"+username)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return mapper.readValue(Objects.requireNonNull(response.body()).string(), new TypeReference<>() {
        });
    }

    @Override
    public List<String> sendContent(List<MultipartFile> files) {

        ObjectMapper mapper = new ObjectMapper();

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), (File) files);
        Request request = new Request.Builder()
                .url(userServiceUrl + "/content/save")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
    }


}
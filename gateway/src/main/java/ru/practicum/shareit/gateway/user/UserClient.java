package ru.practicum.shareit.gateway.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.user.dto.UserPatchDto;
import ru.practicum.shareit.gateway.user.dto.UserPostDto;

import static ru.practicum.shareit.gateway.common.CommonUtils.SHAREIT_SERVER_URL_PROPERTY;
import static ru.practicum.shareit.gateway.common.CommonUtils.USERS_RESOURCE;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value(SHAREIT_SERVER_URL_PROPERTY) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + USERS_RESOURCE))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> post(UserPostDto userPostDto) {
        return post("", userPostDto);
    }

    public ResponseEntity<Object> getAll() {
        return get("");
    }

    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> patchById(long userId, UserPatchDto userPatchDto) {
        return patch("/" + userId, userPatchDto);
    }

    public ResponseEntity<Object> deleteById(long userId) {
        return delete("/" + userId);
    }

}

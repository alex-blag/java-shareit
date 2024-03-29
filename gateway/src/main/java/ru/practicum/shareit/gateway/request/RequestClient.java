package ru.practicum.shareit.gateway.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.request.dto.RequestPostDto;

import java.util.Map;

import static ru.practicum.shareit.gateway.common.CommonUtils.REQUESTS_RESOURCE;
import static ru.practicum.shareit.gateway.common.CommonUtils.SHAREIT_SERVER_URL_PROPERTY;

@Service
public class RequestClient extends BaseClient {

    @Autowired
    public RequestClient(@Value(SHAREIT_SERVER_URL_PROPERTY) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + REQUESTS_RESOURCE))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> post(long userId, RequestPostDto requestPostDto) {
        return post("", userId, requestPostDto);
    }

    public ResponseEntity<Object> getAllByRequesterId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllByUserId(String allResource, long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );

        return get(allResource + "?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getById(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

}

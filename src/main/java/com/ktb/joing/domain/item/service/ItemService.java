package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.response.ItemCreateResponse;
import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.user.entity.User;
import com.ktb.joing.domain.user.exception.UserErrorCode;
import com.ktb.joing.domain.user.exception.UserException;
import com.ktb.joing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemCreateResponse createItem(ItemCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Item item = Item.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .mediaType(request.getMediaType())
                .score(0)
                .user(user)
                .category(request.getCategory())
                .build();

        if (request.getEtcs() != null && !request.getEtcs().isEmpty()) {
            request.getEtcs().forEach(etcRequest -> {
                Etc etc = Etc.builder()
                        .name(etcRequest.getName())
                        .value(etcRequest.getValue())
                        .item(item)
                        .build();
                item.getEtcs().add(etc);
            });
        }

        Item savedItem = itemRepository.save(item);

        return ItemCreateResponse.builder()
                .item(savedItem)
                .build();
    }
}




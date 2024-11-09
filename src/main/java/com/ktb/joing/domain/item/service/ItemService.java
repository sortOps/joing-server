package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.response.ItemCreateResponse;
import com.ktb.joing.domain.item.entity.Etc;
import com.ktb.joing.domain.item.entity.Item;
import com.ktb.joing.domain.item.exception.ItemErrorCode;
import com.ktb.joing.domain.item.exception.ItemException;
import com.ktb.joing.domain.item.repository.ItemRepository;
import com.ktb.joing.domain.user.entity.ProductManager;
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

    // 기획안 생성
    public ItemCreateResponse createItem(ItemCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!(user instanceof ProductManager)) {
            throw new ItemException(ItemErrorCode.INVALID_USER_TYPE);  // 적절한 에러 코드 필요
        }

        Item item = Item.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .mediaType(request.getMediaType())
                .score(0)
                .category(request.getCategory())
                .build();

        item.setUser(user);

        if (request.getEtcs() != null && !request.getEtcs().isEmpty()) {
            request.getEtcs().forEach(etcRequest -> {
                Etc etc = Etc.builder()
                        .name(etcRequest.getName())
                        .value(etcRequest.getValue())
                        .build();
                item.addEtc(etc);
            });
        }

        Item savedItem = itemRepository.save(item);
        return ItemCreateResponse.builder()
                .item(savedItem)
                .build();
    }
}

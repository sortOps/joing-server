package com.ktb.joing.domain.item.service;

import com.ktb.joing.domain.item.dto.request.ItemCreateRequest;
import com.ktb.joing.domain.item.dto.request.ItemUpdateRequest;
import com.ktb.joing.domain.item.dto.response.ItemResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 기획안 생성
    public ItemResponse createItem(ItemCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (!(user instanceof ProductManager)) {
            throw new ItemException(ItemErrorCode.INVALID_USER_TYPE);
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
        return ItemResponse.builder()
                .item(savedItem)
                .build();
    }

    // 기획안 수정
    public ItemResponse updateItem(Long itemId, ItemUpdateRequest request, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getUser().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }

        item.update(request.getTitle(),
                request.getContent(),
                request.getMediaType(),
                request.getCategory());

        if (request.getEtcs() != null) {
            List<Etc> newEtcs = request.getEtcs().stream()
                    .map(etcRequest -> Etc.builder()
                            .name(etcRequest.getName())
                            .value(etcRequest.getValue())
                            .build())
                    .collect(Collectors.toList());

            item.updateEtcs(newEtcs);
        }

        return ItemResponse.builder()
                .item(item)
                .build();
    }

    // 기획안 삭제
    public void deleteItem(Long itemId, String username) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getUser().getUsername().equals(username)) {
            throw new ItemException(ItemErrorCode.ITEM_NOT_AUTHORIZED);
        }
        itemRepository.delete(item);
    }

}

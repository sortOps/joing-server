package com.ktb.joing.domain.item.exception;

import com.ktb.joing.common.exception.BusinessException;
import lombok.Getter;

@Getter
public class ItemException extends BusinessException {
    private final ItemErrorCode itemErrorCode;

    public ItemException(ItemErrorCode itemErrorCode) {
        super(itemErrorCode);
        this.itemErrorCode = itemErrorCode;
    }
}

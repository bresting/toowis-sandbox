package com.toowis.events;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import lombok.Getter;

@Getter
public class GenericEvent <T> implements ResolvableTypeProvider{
    private T result;
    protected boolean success;

    public GenericEvent(T result, boolean success) {
        this.result = result;
        this.success = success;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(this.result));
    }
}
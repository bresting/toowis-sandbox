package com.toowis.events;

import org.springframework.context.ApplicationEvent;

/**
 * 이벤트 객체
 * 
 * - 뭔가 발생되는 이벤트 내용을 담는 객체다.
 * - 아카 메시지 개념
 */
public class CustomEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3877107845229902672L;
    private String message;

    public CustomEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

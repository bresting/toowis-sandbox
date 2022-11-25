package com.toowis.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * CustomEvent 객체를 발행하는 게시자
 * 이곳을 통해 이벤트가 발생(발행)된다.
 * - 아카 Actor의 ask, tell개념
 */
@RequiredArgsConstructor
@Component
public class CustomEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(final String message) {
        System.out.println("Publishing custom event. ");
        CustomEvent customSpringEvent = new CustomEvent(this, message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}

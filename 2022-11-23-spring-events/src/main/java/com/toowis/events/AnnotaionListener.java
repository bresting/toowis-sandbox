package com.toowis.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * CustomEventListener와 같은 기능, annotation으로 처리한다
 */
@Component
public class AnnotaionListener {
    /**
     * @Async == AsyncEventConfig
     * @EventListener == ApplicationListener<?>
     */
    @Async
    @EventListener
    public void handleEvent(CustomEvent event) {
        try {
            Thread.sleep(3000);
            System.out.println("Received spring custom event by annotation listener - " + event.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

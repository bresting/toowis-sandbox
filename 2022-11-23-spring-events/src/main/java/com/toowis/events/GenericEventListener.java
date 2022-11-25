package com.toowis.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GenericEventListener {
    
    //@Async
    //@EventListener(condition = "#event.success")
    //public void handleEvent(GenericEvent<?> event) {
    //    System.out.println("Received spring generic event by annotation listener - " + event.getResult());
    //}
    
    @Async
    @EventListener
    public void handleEvent2(GenericEvent<String> event) {
        System.out.println("String Received spring generic event by annotation listener - " + event.getResult());
    }
}
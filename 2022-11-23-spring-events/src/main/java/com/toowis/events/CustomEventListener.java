package com.toowis.events;

import org.springframework.context.ApplicationListener;

/**
 * 발생(발행)된 이벤드 메시지를 듣고 있다 처리한다.
 * 아카 Actor의 receiveBuilder 개념
 */
//@Component
public class CustomEventListener implements ApplicationListener<CustomEvent>{
    @Override
    public void onApplicationEvent(CustomEvent event) {
        //try {
        //    Thread.sleep(5000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        System.out.println("Received spring custom event - " + event.getMessage());
    }
}
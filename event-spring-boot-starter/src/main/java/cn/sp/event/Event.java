package cn.sp.event;

import java.util.EventObject;

/**
 * @author 2YSP
 * @date 2022/4/16 16:00
 */
public class Event extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public Event(Object source) {
        super(source);
    }
}

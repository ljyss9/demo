package com.ljy.async;

import java.util.List;

/**
 * Created by ljy on 2017/2/19.
 */
public interface EventHandle {

        void doHandle(EventModel model);

        List<EventType> getSupportEventTypes();

}

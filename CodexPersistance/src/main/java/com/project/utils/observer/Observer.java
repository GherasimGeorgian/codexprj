package com.project.utils.observer;

import com.project.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
package com.seanachaidh.handyandroid;

public interface Subscribeable {
    void addSubscriber(Subscriber s);
    void notifySubscribers();
}

package com.evm.ms.notifier.domain.ports.out;

public interface NotifierPort<T> {

    boolean sendNotification(T notification);

}

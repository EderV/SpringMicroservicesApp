package com.evm.ms.userevents.domain.ports.out;

public interface MessageBrokerPort<T> {

    void sendMessage(String topic, T message) throws RuntimeException;

}

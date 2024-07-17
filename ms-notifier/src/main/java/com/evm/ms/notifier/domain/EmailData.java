package com.evm.ms.notifier.domain;

public record EmailData(String from, String to, String subject, String body) { }

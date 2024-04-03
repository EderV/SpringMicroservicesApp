package com.evm.ms.gateway.infrastructure.openfeign;

import com.evm.ms.gateway.domain.exceptions.ClientUnauthorizedException;
import com.evm.ms.gateway.domain.exceptions.ClientForbiddenException;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FeignConfig {

    @Bean
    public Encoder feignEncoder() {
        ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
        return new SpringEncoder(messageConverters);
    }

    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
        return new SpringDecoder(messageConverters);
    }

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return (s, response) -> {
            String responseBody;
            try (InputStream inputStream = response.body().asInputStream()) {
                responseBody = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            } catch (IOException ex) {
                return new RuntimeException("Error reading body");
            }

            switch (response.status()) {
                case 401 -> {
                    return new ClientUnauthorizedException(responseBody);
                }
                case 403 -> {
                    return new ClientForbiddenException(responseBody);
                }
                default -> {
                    return new RuntimeException(responseBody);
                }
            }
        };
    }

}

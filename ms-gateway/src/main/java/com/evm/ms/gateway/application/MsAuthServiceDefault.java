package com.evm.ms.gateway.application;

import com.evm.ms.gateway.domain.exceptions.ClientUnauthorizedException;
import com.evm.ms.gateway.domain.exceptions.ClientForbiddenException;
import com.evm.ms.gateway.domain.in.MsAuthServicePort;
import com.evm.ms.gateway.infrastructure.openfeign.MsAuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MsAuthServiceDefault implements MsAuthServicePort {

    private final MsAuthFeignClient msAuthClient;

    @Override
    public int isClientAllowed(String authorizationHeader, String path) {
        try {
            var res = msAuthClient.validateClient(authorizationHeader, path);
            log.info("Response from ms-auth: " + res);
            return 200;
        } catch (ClientUnauthorizedException ex) {
            log.warn("Client is unauthorized. Ex. " + ex);
            return 401;
        } catch (ClientForbiddenException ex) {
            log.warn("Client is forbidden. Ex: " + ex);
            return 403;
        } catch (RuntimeException ex) {
            log.warn("Something went wrong calling Ms-Auth. Ex" + ex);
            return 502;
        }
    }

}

package com.spider.commonUtil.config;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RSAConfig {

    @Value("#{rsaProperties['pub_rsa']}")
    private String pub_rsa;

    @Value("#{rsaProperties['private_rsa']}")
    private String private_rsa;
}

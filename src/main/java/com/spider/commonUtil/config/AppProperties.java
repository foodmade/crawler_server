package com.spider.commonUtil.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("#{appProperties['configPosition']}")
    private String configPosition;
    @Value("#{appProperties['parserDir']}")
    private String parserDir;
    @Value("#{appProperties['kqHost']}")
    private String kqHost;
}

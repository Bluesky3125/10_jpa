package com.ohgiraffers.springdatajpa.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /* 설명. DTO <-> Entity 매핑을 위한 modelmapper 라이브러리 bean 추가 */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

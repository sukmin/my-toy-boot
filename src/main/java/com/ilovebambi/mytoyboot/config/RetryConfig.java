package com.ilovebambi.mytoyboot.config;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
@Log
public class RetryConfig {

}

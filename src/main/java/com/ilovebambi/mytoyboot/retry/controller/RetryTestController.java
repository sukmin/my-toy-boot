package com.ilovebambi.mytoyboot.retry.controller;

import com.ilovebambi.mytoyboot.retry.service.RetryTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/retry")
@RestController
public class RetryTestController {

    private final RetryTestService retryTestService;

    @Autowired
    public RetryTestController(RetryTestService retryTestService) {
        this.retryTestService = retryTestService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/my-string")
    public String myString() {
        return retryTestService.getMyString();
    }

    @GetMapping("/getMyStringWithContext")
    public String getMyStringWithContext() {
        return retryTestService.getMyStringWithContext(100);
    }

    @GetMapping("/useTemplate")
    public String useTemplate() {
        return retryTestService.useTemplate();
    }
}

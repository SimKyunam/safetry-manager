package com.side.safetrymanager.controller;

import com.side.safetrymanager.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/test")
public class TestController {

    @GetMapping("/qr/read")
    public String send(@RequestParam String name, @RequestParam Long age) {
        log.info("이름: " + name);
        log.info("나이: " + age);
        return "test";
    }
}

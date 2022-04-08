package com.side.safetrymanager.controller;

import com.side.safetrymanager.service.FCMService;
import com.side.safetrymanager.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/push")
public class PushController {
    private final FCMService fcmService;

    @GetMapping("/send")
    public String send() throws Exception {
        fcmService.sendMessageTo("pushToken", "테스트", "내용");
        return "test";
    }
}

package com.side.safetrymanager.controller;

import com.side.safetrymanager.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
public class HomeController {

	@Value("${file.upload-dir}")
	private String fileDir;

	@GetMapping("/index")
	public String home(Model model) throws Exception {
		String targetUrl = "http://192.168.0.107:9090";

		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(targetUrl + "/rest/test/qr/read")
				.queryParam("name", "홍길동")
				.queryParam("age", 10)
				.build();

		String qrCode = CommonUtils.createQR(uriComponents.toString(), fileDir);
		model.addAttribute("qrCode", qrCode);

		return "test";
	}
}

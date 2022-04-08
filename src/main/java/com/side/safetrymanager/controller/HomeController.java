package com.side.safetrymanager.controller;

import com.side.safetrymanager.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class HomeController {

	@Value("${file.upload-dir}")
	private String fileDir;

	@GetMapping("/index")
	public String home(Model model) throws Exception {
		String qrCode = CommonUtils.createQR("https://naver.com", fileDir);
		model.addAttribute("qrCode", qrCode);

		return "test";
	}
}

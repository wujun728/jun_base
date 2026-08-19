package com.ruoyi.ai.controller;

import com.ruoyi.ai.service.impl.OllamaServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollama")
public class OllamaController {
    @Autowired
    private OllamaServiceImpl ollamaService;

    @GetMapping("/sendMessage")
    public AjaxResult sendMessage(String msg){
        ChatResponse chatResponse = ollamaService.sendMessage(msg);
        return AjaxResult.success(chatResponse.getResult().getOutput());
    }
}

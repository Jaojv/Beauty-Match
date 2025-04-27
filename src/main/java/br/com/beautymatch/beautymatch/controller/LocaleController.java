package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/locale")
public class LocaleController {

    private final MessageService messageService;

    public LocaleController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public ResponseEntity<Map<String, String>> getMessages(@RequestParam(required = false) String lang) {
        Map<String, String> messages = new HashMap<>();
        
        // Mensagens gerais
        messages.put("success", messageService.getMessage("message.generic.success"));
        messages.put("error", messageService.getMessage("message.generic.error"));
        messages.put("notFound", messageService.getMessage("message.generic.notFound"));
        
        // Mensagens de autenticação
        messages.put("loginSuccess", messageService.getMessage("message.auth.login.success"));
        messages.put("loginError", messageService.getMessage("message.auth.login.error"));
        
        // Mensagens de usuário
        messages.put("userCreated", messageService.getMessage("message.user.created"));
        messages.put("userNotFound", messageService.getMessage("message.user.notFound"));
        
        return ResponseEntity.ok(messages);
    }
} 
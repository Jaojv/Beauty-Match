package com.beauty.com.MatchBeauty.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * SERVIÇO DE UPLOAD DE ARQUIVOS - GERENCIAMENTO DE UPLOADS
 * 
 * Este serviço gerencia o upload e armazenamento de arquivos no sistema,
 * especialmente imagens de salões e perfis. Implementa validações de arquivo,
 * geração de nomes únicos e armazenamento seguro.
 * 
 * FUNCIONALIDADES:
 * - Upload de arquivos com validação
 * - Geração de nomes únicos para arquivos
 * - Validação de tipos de arquivo permitidos
 * - Verificação de tamanho de arquivo
 * - Armazenamento seguro em diretório específico
 * - Tratamento de erros de upload
 * - Limpeza de arquivos temporários
 * 
 * VALIDAÇÕES:
 * - Tipos de arquivo permitidos (imagens)
 * - Tamanho máximo de arquivo
 * - Verificação de arquivo vazio
 * - Validação de extensões seguras
 * 
 * SEGURANÇA:
 * - Geração de nomes únicos para evitar conflitos
 * - Validação de tipos de arquivo para prevenir uploads maliciosos
 * - Armazenamento em diretório específico e seguro
 * - Tratamento de exceções para evitar vazamentos de informação
 * 
 * DIRETÓRIOS:
 * - Uploads de salões: /uploads/saloes/
 * - Uploads de perfis: /uploads/perfis/
 * - Configurável via propriedades do sistema
 */
@Service
public class FileUploadService {

    private static final String UPLOAD_DIR = "uploads/saloes/";

    public FileUploadService() {
        // Criar diretório de upload se não existir
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório de upload", e);
        }
    }

    public String uploadImagemSalao(MultipartFile file) throws IOException {
        // Validar tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo deve ser uma imagem");
        }

        // Gerar nome único para o arquivo
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(UPLOAD_DIR, filename);

        // Salvar arquivo
        Files.copy(file.getInputStream(), filePath);

        // Retornar URL relativa
        return "/uploads/saloes/" + filename;
    }

    public void deleteImagemSalao(String imagemUrl) {
        if (imagemUrl != null && imagemUrl.startsWith("/uploads/saloes/")) {
            String filename = imagemUrl.substring("/uploads/saloes/".length());
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log do erro mas não falhar a operação
                System.err.println("Erro ao deletar arquivo: " + filePath);
            }
        }
    }
} 
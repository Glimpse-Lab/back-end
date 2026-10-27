package com.glimpse.gpm.service;

import com.glimpse.gpm.exception.DadosInvalidosException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB

    private static final Map<String, String> TIPOS_PERMITIDOS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp"
    );

    public String salvar(MultipartFile file, String baseUrl) {
        if (file == null || file.isEmpty()) return null;

        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new DadosInvalidosException("Arquivo excede o tamanho máximo de 5MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !TIPOS_PERMITIDOS.containsKey(contentType)) {
            throw new DadosInvalidosException("Tipo de arquivo não permitido. Use JPEG, PNG ou WebP.");
        }

        String extensao = TIPOS_PERMITIDOS.get(contentType);
        String fileName = UUID.randomUUID() + extensao;

        try {
            Path pastaUpload = Paths.get("./gpm_uploads").toAbsolutePath().normalize();
            if (!Files.exists(pastaUpload)) {
                Files.createDirectories(pastaUpload);
            }

            Path destino = pastaUpload.resolve(fileName);
            // Garante que o destino está dentro da pasta permitida (evita path traversal)
            if (!destino.startsWith(pastaUpload)) {
                throw new DadosInvalidosException("Caminho de arquivo inválido.");
            }

            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/" + fileName;
        } catch (DadosInvalidosException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar o arquivo no disco.", e);
        }
    }
}
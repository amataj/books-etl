package com.example.books.infrastructure.ocr;

import java.util.Map;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Tesseract.class)
public class TesseractConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "application.ocr", name = "enabled", havingValue = "true")
    public Tesseract tesseract(ApplicationProperties applicationProperties) {
        ApplicationProperties.Ocr ocrProperties = applicationProperties.getOcr();
        Tesseract tesseract = new Tesseract();
        if (ocrProperties.getDataPath() != null && !ocrProperties.getDataPath().isBlank()) {
            tesseract.setDatapath(ocrProperties.getDataPath());
        }
        if (ocrProperties.getLanguage() != null && !ocrProperties.getLanguage().isBlank()) {
            tesseract.setLanguage(ocrProperties.getLanguage());
        }
        if (ocrProperties.getPageSegMode() != null) {
            tesseract.setPageSegMode(ocrProperties.getPageSegMode());
        }
        if (ocrProperties.getOcrEngineMode() != null) {
            tesseract.setOcrEngineMode(ocrProperties.getOcrEngineMode());
        }
        for (Map.Entry<String, String> entry : ocrProperties.getTessVariables().entrySet()) {
            tesseract.setVariable(entry.getKey(), entry.getValue());
        }
        return tesseract;
    }
}

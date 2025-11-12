package com.example.books.infrastructure.ocr;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Tesseract.class)
@ConditionalOnProperty(value = "ocr.enabled", havingValue = "true", matchIfMissing = false)
public class TesseractConfig {

    @Bean
    public Tesseract tesseract(
        @Value("${ocr.datapath:/usr/share/tesseract-ocr/4.00/tessdata}") String dataPath,
        @Value("${ocr.lang:eng}") String lang
    ) {
        Tesseract t = new Tesseract();
        t.setDatapath(dataPath);
        t.setLanguage(lang);
        return t;
    }
}

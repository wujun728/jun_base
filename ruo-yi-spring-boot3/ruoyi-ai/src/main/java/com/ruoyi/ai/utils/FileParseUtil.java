package com.ruoyi.ai.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileParseUtil {

    public static String parseFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IOException("文件名为空");
        }

        if (filename.endsWith(".txt")) {
            return parseTxt(file);
        } else if (filename.endsWith(".pdf")) {
            return parsePdf(file);
        } else if (filename.endsWith(".doc") || filename.endsWith(".docx")) {
            return parseWord(file);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    private static String parseTxt(MultipartFile file) throws IOException {
        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }

    private static String parsePdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String parseWord(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null) return "";

        if (filename.endsWith(".docx")) {
            try (XWPFDocument docx = new XWPFDocument(file.getInputStream())) {
                XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                return extractor.getText();
            }
        } else if (filename.endsWith(".doc")) {
            try (HWPFDocument doc = new HWPFDocument(file.getInputStream())) {
                WordExtractor extractor = new WordExtractor(doc);
                return extractor.getText();
            }
        } else {
            return "";
        }
    }
}

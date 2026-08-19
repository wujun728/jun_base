package com.ruoyi.ai.utils;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author： 欧阳日峰
 * Description：切分文件工具
 **/
public class RuoyiDocumentSplitter implements DocumentSplitter {
    public static final String SPLIT_EXP="\\s*\\R\\s*\\R\\s*";
    @Override
    public List<TextSegment> split(Document document) {
        List<TextSegment> segments = new ArrayList<>();
        String[] parts = document.text().split(SPLIT_EXP);
        for (String part : parts) {
            segments.add(TextSegment.from(part));
        }
        return segments;
    }
}

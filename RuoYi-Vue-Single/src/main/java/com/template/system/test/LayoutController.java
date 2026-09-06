package com.template.system.test;


import com.alibaba.fastjson2.JSON;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test/layouts")
public class LayoutController {

    private static final String RELATIVE_PATH = "META-INF/config/layout.json";

    private String getSourceResourcePath() {
        String projectRoot = System.getProperty("user.dir");
        return projectRoot + File.separator + "src" + File.separator + "main" +
                File.separator + "resources" + File.separator + RELATIVE_PATH;
    }

    private String getTargetResourcePath() {
        try {
            return new File(Objects.requireNonNull(getClass().getClassLoader()
                    .getResource(RELATIVE_PATH)).toURI()).getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private String getPreferredFilePath() {
        String sourcePath = getSourceResourcePath();
        if (new File(sourcePath).exists()) {
            return sourcePath;
        }
        String targetPath = getTargetResourcePath();
        if (targetPath != null) {
            return targetPath;
        }
        return sourcePath;
    }

    private void ensureFileExists() {
        try {
            File file = new File(getPreferredFilePath());
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                Files.write(file.toPath(), "[]".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create JSON file", e);
        }
    }

    @GetMapping
    public List<LayoutConfig> readAll() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(getPreferredFilePath())));
            return JSON.parseArray(content, LayoutConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file", e);
        }
    }

    @PostMapping
    public void add(@RequestBody LayoutConfig item) {
        ensureFileExists();
        List<LayoutConfig> items = readAll();
        items.add(item);
        writeToFile(items);
    }

    @PutMapping
    public void update(@RequestBody LayoutConfig item) {
        ensureFileExists();
        List<LayoutConfig> items = readAll();
        items = items.stream()
                .map(i -> i.getId().equals(item.getId()) ? item : i)
                .collect(Collectors.toList());
        writeToFile(items);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        ensureFileExists();
        List<LayoutConfig> items = readAll();
        items = items.stream()
                .filter(i -> !i.getId().equals(id))
                .collect(Collectors.toList());
        writeToFile(items);
    }

    @GetMapping("/{id}")
    public LayoutConfig findById(@PathVariable String id) {
        return readAll().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void writeToFile(List<LayoutConfig> items) {
        try {
            Files.write(Paths.get(getPreferredFilePath()),
                    JSON.toJSONString(items).getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to JSON file", e);
        }
    }
}

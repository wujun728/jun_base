package com.template.system.test;

import java.util.List;

public class LayoutConfig {
    private String id;
    private String name;
    private String description;
    private List<String> components;

    // 构造方法、getter和setter
    public LayoutConfig() {
    }

    public LayoutConfig(String id, String name, String description, List<String> components) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.components = components;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "LayoutConfig{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", components=" + components +
                '}';
    }
}

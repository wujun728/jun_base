#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""批量创建6个OA模块"""

import os
import shutil
import re

SOURCE_ROOT = r"D:\workspace_github\jun_product_center_private_qixing\ruoyi-vue-oa"
TARGET_ROOT = r"D:\workspace_github\jun_base_elementui_jdk17\RuoYi-Vue-3.92-springboot3-jdk17\ruoyi-business"
RUOYI_VERSION = "${ruoyi.version}"

# 模块定义: (module_name, oa_source_path, package_dir)
MODULES = [
    ("ruoyi-oa-tools",      "ruoyi-parent-common/ruoyi-tools",      "com/ruoyi/tools"),
    ("ruoyi-oa-sms",        "ruoyi-parent-common/ruoyi-sms",        "com/ruoyi/sms"),
    ("ruoyi-oa-message",    "ruoyi-parent-msg/ruoyi-message",       "com/ruoyi/message"),
    ("ruoyi-oa-worksetting","ruoyi-parent-biz/ruoyi-worksetting",   "com/ruoyi/worksetting"),
    ("ruoyi-oa-biz-sdk",    "ruoyi-parent-biz/ruoyi-biz-sdk",       "com/ruoyi/biz"),
    ("ruoyi-oa-seal",       "ruoyi-parent-flow/ruoyi-seal",         "com/ruoyi/seal"),
]


def remove_bom(content_bytes):
    """去除UTF-8 BOM"""
    if content_bytes[:3] == b'\xef\xbb\xbf':
        return content_bytes[3:]
    return content_bytes


def transform_content(content):
    """转换Java文件内容"""
    # 删除 import RequestPart 行
    content = re.sub(r'^import org\.springframework\.web\.bind\.annotation\.RequestPart;\s*\n', '', content, flags=re.MULTILINE)

    # 替换 javax.annotation 为 jakarta.annotation
    content = content.replace('import javax.annotation.', 'import jakarta.annotation.')
    # 替换 javax.servlet 为 jakarta.servlet
    content = content.replace('import javax.servlet.', 'import jakarta.servlet.')

    # 其他可能的 javax 引用
    content = content.replace('javax.annotation.', 'jakarta.annotation.')
    content = content.replace('javax.servlet.', 'jakarta.servlet.')

    return content


def copy_and_transform_files(module_name, source_path, package_dir):
    """复制文件并执行转换"""
    src_java = os.path.join(SOURCE_ROOT, source_path, "src", "main", "java")
    tgt_java = os.path.join(TARGET_ROOT, module_name, "src", "main", "java")

    print(f"  [复制文件] {module_name}")
    print(f"    源: {src_java}")
    print(f"    目标: {tgt_java}")

    file_count = 0
    for root, dirs, files in os.walk(src_java):
        for fname in files:
            if fname.endswith('.java'):
                src_file = os.path.join(root, fname)
                rel_dir = os.path.relpath(root, src_java)
                tgt_dir = os.path.join(tgt_java, rel_dir)
                tgt_file = os.path.join(tgt_dir, fname)

                os.makedirs(tgt_dir, exist_ok=True)

                # 读取原始字节
                with open(src_file, 'rb') as f:
                    raw = f.read()

                # 去除BOM
                raw = remove_bom(raw)

                # 解码并转换
                content = raw.decode('utf-8')
                content = transform_content(content)

                with open(tgt_file, 'w', encoding='utf-8') as f:
                    f.write(content)

                file_count += 1

    print(f"    共处理 {file_count} 个文件")
    return file_count


def create_pom(module_name, dependencies_extra=""):
    """创建模块 pom.xml"""
    pom_content = f'''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>ruoyi-business</artifactId>
        <groupId>com.ruoyi</groupId>
        <version>{RUOYI_VERSION}</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>{module_name}</artifactId>
{dependencies_extra}
</project>
'''
    pom_path = os.path.join(TARGET_ROOT, module_name, "pom.xml")
    os.makedirs(os.path.dirname(pom_path), exist_ok=True)
    with open(pom_path, 'w', encoding='utf-8') as f:
        f.write(pom_content)
    print(f"  [创建POM] {pom_path}")


def get_extra_deps(module_name):
    """返回各模块的额外依赖XML"""
    deps = {
        "ruoyi-oa-tools": '''
    <dependencies>
        <dependency>
            <groupId>io.protostuff</groupId>
            <artifactId>protostuff-core</artifactId>
            <version>1.8.0</version>
        </dependency>
        <dependency>
            <groupId>io.protostuff</groupId>
            <artifactId>protostuff-runtime</artifactId>
            <version>1.8.0</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.belerweb</groupId>
            <artifactId>pinyin4j</artifactId>
            <version>2.5.1</version>
        </dependency>
    </dependencies>''',
        "ruoyi-oa-sms": '''
    <dependencies>
        <dependency>
            <groupId>com.aliyun</groupId>
            <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.tencentcloudapi</groupId>
            <artifactId>tencentcloud-sdk-java-sms</artifactId>
            <version>3.1.500</version>
        </dependency>
    </dependencies>''',
        "ruoyi-oa-message": '''
    <dependencies>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-tools</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
    </dependencies>''',
        "ruoyi-oa-worksetting": '''
    <dependencies>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-tools</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
    </dependencies>''',
        "ruoyi-oa-biz-sdk": '''
    <dependencies>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-tools</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-system</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
    </dependencies>''',
        "ruoyi-oa-seal": '''
    <dependencies>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
            <version>${ruoyi.version}</version>
        </dependency>
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext-asian</artifactId>
            <version>5.5.13.3</version>
        </dependency>
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk18on</artifactId>
            <version>1.78</version>
        </dependency>
    </dependencies>''',
    }
    return deps.get(module_name, "")


def update_parent_pom():
    """更新 ruoyi-business/pom.xml，在 ruoyi-oa-qixing 之前插入新模块"""
    pom_path = os.path.join(TARGET_ROOT, "pom.xml")
    with open(pom_path, 'r', encoding='utf-8') as f:
        content = f.read()

    new_modules = [
        "ruoyi-oa-tools",
        "ruoyi-oa-sms",
        "ruoyi-oa-message",
        "ruoyi-oa-worksetting",
        "ruoyi-oa-biz-sdk",
        "ruoyi-oa-seal",
    ]

    indent = "        "
    new_module_xml = "\n".join(f"{indent}<module>{m}</module>" for m in new_modules)

    # 在 <module>ruoyi-oa-qixing</module> 之前插入
    old_str = f"{indent}<module>ruoyi-oa-qixing</module>"
    new_str = f"{new_module_xml}\n{old_str}"
    content = content.replace(old_str, new_str)

    with open(pom_path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"\n[更新父POM] {pom_path}")
    print(f"  已插入 {len(new_modules)} 个新模块")


def main():
    print("=" * 60)
    print("开始批量创建OA模块")
    print("=" * 60)

    for module_name, source_path, package_dir in MODULES:
        print(f"\n>>> 处理模块: {module_name}")
        copy_and_transform_files(module_name, source_path, package_dir)
        create_pom(module_name, get_extra_deps(module_name))

    update_parent_pom()

    print("\n" + "=" * 60)
    print("所有模块创建完成！")
    print("=" * 60)


if __name__ == "__main__":
    main()

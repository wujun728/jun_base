#!/usr/bin/env python3
"""创建 ruoyi-oa-workflow-file 和 ruoyi-oa-workflow 两个 OA 模块"""

import os
import re
import shutil

TARGET_BASE = r"D:\workspace_github\jun_base_elementui_jdk17\RuoYi-Vue-3.92-springboot3-jdk17\ruoyi-business"

# ===== 模块1: ruoyi-oa-workflow-file =====
SRC_BASE_FILE = r"D:\workspace_github\jun_product_center_private_qixing\ruoyi-vue-oa\ruoyi-parent-flow\ruoyi-workflow-file"
MODULE_FILE = "ruoyi-oa-workflow-file"
TARGET_PKG_FILE = "com.ruoyi.workflow.file"   # 目标包名
SRC_PKG_FILE = "com.ruoyi.workfile"            # 源包名

# ===== 模块2: ruoyi-oa-workflow =====
SRC_BASE_WF = r"D:\workspace_github\jun_product_center_private_qixing\ruoyi-vue-oa\ruoyi-parent-flow\ruoyi-workflow"
MODULE_WF = "ruoyi-oa-workflow"
TARGET_PKG_WF = "com.ruoyi.workflow"           # 目标包名 = 源包名，无需改名
SRC_PKG_WF = "com.ruoyi.workflow"


def remove_bom(content):
    """去除 UTF-8 BOM"""
    if content.startswith('\ufeff'):
        return content[1:]
    return content


def transform_java(content, src_pkg, target_pkg):
    """对 Java 文件内容做以下转换:
    1. 去除 UTF-8 BOM
    2. javax -> jakarta 替换
    3. 删除 RequestPart import 行
    4. 替换包名
    """
    content = remove_bom(content)
    content = content.replace('javax.', 'jakarta.')
    # 删除含 RequestPart 的 import 行
    content = re.sub(r'^import\s+.*RequestPart.*\n', '', content, flags=re.MULTILINE)
    # 替换包名
    if src_pkg != target_pkg:
        # 替换 package 声明
        content = content.replace(f'package {src_pkg}', f'package {target_pkg}')
        # 替换 import 中的包名
        content = content.replace(f'import {src_pkg}', f'import {target_pkg}')
    return content


def copy_module(src_base, target_module_path, src_pkg, target_pkg):
    """复制整个模块的 Java 文件，应用转换"""
    java_src_dir = os.path.join(src_base, 'src', 'main', 'java')
    # 遍历目录
    for root, dirs, files in os.walk(java_src_dir):
        for f in files:
            if not f.endswith('.java'):
                continue
            src_file = os.path.join(root, f)
            # 计算相对路径，替换包名路径
            rel = os.path.relpath(src_file, java_src_dir)
            
            # src_pkg 路径 (com/ruoyi/workfile)
            src_pkg_path = src_pkg.replace('.', os.sep)
            target_pkg_path = target_pkg.replace('.', os.sep)
            
            # 替换路径中的包名部分
            if src_pkg_path in rel:
                new_rel = rel.replace(src_pkg_path, target_pkg_path, 1)
            else:
                new_rel = rel
            
            dest_dir = os.path.join(target_module_path, 'src', 'main', 'java', os.path.dirname(new_rel))
            os.makedirs(dest_dir, exist_ok=True)
            dest_file = os.path.join(dest_dir, f)
            
            with open(src_file, 'rb') as fh:
                raw = fh.read()
            # 先以 utf-8-sig 解码（自动去BOM），再以 utf-8 编码
            try:
                content = raw.decode('utf-8-sig')
            except UnicodeDecodeError:
                content = raw.decode('utf-8', errors='replace')
            
            content = transform_java(content, src_pkg, target_pkg)
            
            with open(dest_file, 'wb') as fh:
                fh.write(content.encode('utf-8'))
            print(f"  [OK] {rel} -> {new_rel}")


def create_pom_file(module_path, artifact_id, deps_xml):
    """创建 pom.xml"""
    pom_content = f'''<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>ruoyi-business</artifactId>
        <groupId>com.ruoyi</groupId>
        <version>${{ruoyi.version}}</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>{artifact_id}</artifactId>

    <dependencies>
{deps_xml}
    </dependencies>
</project>
'''
    pom_file = os.path.join(module_path, 'pom.xml')
    with open(pom_file, 'w', encoding='utf-8') as f:
        f.write(pom_content)
    print(f"  [OK] pom.xml 已创建")


def main():
    # ========== 模块1: ruoyi-oa-workflow-file ==========
    print("=" * 60)
    print("创建模块1: ruoyi-oa-workflow-file")
    print("=" * 60)
    target1 = os.path.join(TARGET_BASE, MODULE_FILE)
    os.makedirs(target1, exist_ok=True)
    copy_module(SRC_BASE_FILE, target1, SRC_PKG_FILE, TARGET_PKG_FILE)
    
    deps_file = """        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-tools</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-system</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <scope>provided</scope>
        </dependency>"""
    create_pom_file(target1, MODULE_FILE, deps_file)
    
    # ========== 模块2: ruoyi-oa-workflow ==========
    print("\n" + "=" * 60)
    print("创建模块2: ruoyi-oa-workflow")
    print("=" * 60)
    target2 = os.path.join(TARGET_BASE, MODULE_WF)
    os.makedirs(target2, exist_ok=True)
    copy_module(SRC_BASE_WF, target2, SRC_PKG_WF, TARGET_PKG_WF)
    
    deps_wf = """        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-framework</artifactId>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-tools</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-module-flowable</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-seal</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-workflow-file</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-biz-sdk</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-message</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-sms</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.ruoyi</groupId>
            <artifactId>ruoyi-oa-worksetting</artifactId>
            <version>3.9.2</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <scope>provided</scope>
        </dependency>"""
    create_pom_file(target2, MODULE_WF, deps_wf)
    
    # ========== 更新 ruoyi-business/pom.xml 添加 module 条目 ==========
    print("\n" + "=" * 60)
    print("更新 ruoyi-business/pom.xml")
    print("=" * 60)
    biz_pom = os.path.join(TARGET_BASE, 'pom.xml')
    with open(biz_pom, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 在 ruoyi-oa-qixing 之前添加两个新模块
    old_str = '        <module>ruoyi-oa-qixing</module>'
    new_str = '        <module>ruoyi-oa-workflow-file</module>\n        <module>ruoyi-oa-workflow</module>\n        <module>ruoyi-oa-qixing</module>'
    
    if new_str in content:
        print("  模块条目已存在，跳过")
    else:
        content = content.replace(old_str, new_str)
        with open(biz_pom, 'w', encoding='utf-8') as f:
            f.write(content)
        print("  [OK] 已添加 module 条目: ruoyi-oa-workflow-file, ruoyi-oa-workflow")
    
    print("\n" + "=" * 60)
    print("完成!")
    print("=" * 60)


if __name__ == '__main__':
    main()

#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")"/.. && pwd)"
LIBS_DIR="${LIBS_DIR:-"$ROOT/libs"}"
# REPO_DIR="${MAVEN_LOCAL_REPO:-"$ROOT/.m2/repo"}"

# 选择 mvn（优先 mvnw）
MVN_BIN="${MVN_BIN:-mvn}"
if [ -x "$ROOT/mvnw" ]; then MVN_BIN="$ROOT/mvnw"; fi

echo "项目根目录: $ROOT"
echo "扫描目录  : $LIBS_DIR"
# echo "本地仓库  : $REPO_DIR"
echo

installed=0
skipped=0
failed=0

# 遍历所有 pom，匹配同目录 jar（优先同名；否则目录内唯一的 jar，忽略 *-sources/*-javadoc）
while IFS= read -r pom; do
  dir="$(dirname "$pom")"
  base="${pom%*.pom}"
  jar=""
  if [ -f "${base}.jar" ]; then
    jar="${base}.jar"
  else
    mapfile -t jars < <(find "$dir" -maxdepth 1 -type f -name '*.jar' \
                         ! -name '*-sources.jar' ! -name '*-javadoc.jar' | sort)

    # 目录中没有 jar，则把该 POM 作为纯 POM 安装到本地仓库
    if [ "${#jars[@]}" -eq 0 ]; then
      echo ">>>> 安装纯 POM: $pom"
      if "$MVN_BIN" install:install-file \
            -Dfile="$pom" \
            -DpomFile="$pom" \
            -Dpackaging=pom \
            -DgeneratePom=false
      then
        installed=$((installed+1))
      else
        echo "!!!! 纯 POM 安装失败: $pom"
        failed=$((failed+1))
      fi
      echo
      continue
    fi

    if [ "${#jars[@]}" -ne 1 ]; then
      echo "跳过：$dir（未找到同名 jar 或存在多个 jar）"
      skipped=$((skipped+1))
      continue
    fi
    jar="${jars[0]}"
  fi

  echo ">>>> 安装: $jar"
  echo "     POM: $pom"

  if "$MVN_BIN" install:install-file \
        -Dfile="$jar" \
        -DpomFile="$pom"
  then
    # 拉取该 POM 的传递依赖
    "$MVN_BIN" -f "$pom" dependency:go-offline || true
    installed=$((installed+1))
  else
    echo "!!!! 安装失败: $jar"
    failed=$((failed+1))
  fi
  echo
done < <(find "$LIBS_DIR" -type f -name '*.pom' | sort)

echo "完成：成功 $installed，跳过 $skipped，失败 $failed"
# echo "目标仓库：$REPO_DIR"
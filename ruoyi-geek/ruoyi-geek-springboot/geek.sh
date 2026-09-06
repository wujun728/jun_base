#!/usr/bin/env bash
set -e

# 简单日志函数
log()  { printf '[INFO] %s\n' "$*"; }
err()  { printf '[ERR ] %s\n' "$*" >&2; }
die()  { err "$*"; exit 1; }

# 确保在 git 仓库根
if ! git rev-parse --show-toplevel >/dev/null 2>&1; then
  die "当前目录不是 git 仓库，请先 cd 到仓库根再执行。"
fi

REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "$REPO_ROOT" || die "无法切换到仓库根：$REPO_ROOT"

MODULE_YAML="$REPO_ROOT/geek-modules.yml"
[ -f "$MODULE_YAML" ] || die "配置文件 geek-modules.yml 不存在。"

# 解析 YAML (保持原有的解析逻辑)
parse_modules() {
  awk '
    BEGIN { inmods=0; name=""; url=""; path=""; }
    /^[[:space:]]*#/ { next }
    /^[[:space:]]*$/ { next }
    /^[[:space:]]*modules:/ { inmods=1; next }
    inmods {
      if ($0 ~ /^[[:space:]]*-[[:space:]]*name:[[:space:]]*/) {
        if (name != "" && url != "" && path != "") printf "%s\t%s\t%s\n", name, path, url;
        name=""; url=""; path="";
        sub(/^[^:]*:[[:space:]]*/, ""); name=$0; gsub(/^[[:space:]]+|[[:space:]]+$/, "", name);
        next;
      }
      if (inmods && $0 ~ /^[[:space:]]*url:[[:space:]]*/) { s=$0; sub(/^[^:]*:[[:space:]]*/, "", s); url=s; gsub(/^[[:space:]]+|[[:space:]]+$/, "", url); next; }
      if (inmods && $0 ~ /^[[:space:]]*path:[[:space:]]*/) { s=$0; sub(/^[^:]*:[[:space:]]*/, "", s); path=s; gsub(/^[[:space:]]+|[[:space:]]+$/, "", path); next; }
    }
    END { if (name != "" && url != "" && path != "") printf "%s\t%s\t%s\n", name, path, url; }
  ' "$MODULE_YAML"
}

# 核心拉取逻辑：极简版
cmd_add() {
  local name="$1"
  [ -n "$name" ] || die "用法：$0 add <module-name>"

  # 获取配置
  local info path url
  info="$(parse_modules | awk -v target="$name" -F '\t' '$1 == target {print $2 "\t" $3}')"
  [ -n "$info" ] || die "找不到模块: $name"

  path="$(printf '%s\n' "$info" | cut -f1)"
  url="$(printf '%s\n' "$info" | cut -f2)"

  # 1. 如果目录已存在（且里面有 .git），直接 pull
  if [ -d "$path/.git" ]; then
    log "目录已存在，正在更新: $path"
    (cd "$path" && git pull)
  
  # 2. 如果目录不存在，直接 clone
  elif [ ! -d "$path" ]; then
    log "正在克隆新项目: $name -> $path"
    # 创建父目录以防路径包含子文件夹
    mkdir -p "$(dirname "$path")"
    git clone "$url" "$path"
  
  # 3. 目录存在但不是 git 仓库（可能是手动创建的文件夹）
  else
    die "目录 '$path' 已存在但不是 git 仓库，请手动处理。"
  fi
}

# 批量同步
cmd_sync_all() {
  log "开始批量同步..."
  parse_modules | while IFS=$'\t' read -r name _ _; do
    [ -n "$name" ] || continue
    cmd_add "$name" || err "同步 $name 失败"
  done
  log "同步完成。"
}

# 删除目录
cmd_remove() {
  local name="$1"
  local path
  path="$(parse_modules | awk -v target="$name" -F '\t' '$1 == target {print $2}')"
  [ -n "$path" ] || die "找不到模块: $name"
  
  if [ -d "$path" ]; then
    log "删除目录: $path"
    rm -rf "$path"
  else
    log "目录不存在: $path"
  fi
}

cmd_remove_all() {
  log "批量删除..."
  parse_modules | while IFS=$'\t' read -r name _ _; do
    [ -n "$name" ] || continue
    cmd_remove "$name"
  done
}

cmd_list() {
  log "配置列表："
  parse_modules | while IFS=$'\t' read -r name path url; do
    printf ' - %-20s %s\n' "$name" "$path"
  done
}

usage() {
  cat <<EOF
用法：$0 <command> [args...]

command:
  list          列出配置
  sync-all      全部拉取/更新 (核心功能)
  remove-all    全部删除
  add <name>    单独拉取某个
  remove <name> 单独删除某个
EOF
}

case "$1" in
  list) cmd_list;;
  sync-all) cmd_sync_all;;
  remove-all) cmd_remove_all;;
  add) shift; cmd_add "$@";;
  remove) shift; cmd_remove "$@";;
  *) usage; exit 1;;
esac
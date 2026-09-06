#!/bin/sh
# 脚本用途：备份 nohup 日志
# 严格模式：遇错退出
set -e
# 计算脚本目录
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
# 配置：日志源目录（默认脚本上级目录）
SRC_DIR="${SRC_DIR:-${SCRIPT_DIR}/..}"
# 配置：备份保存目录（默认脚本上级目录 logs）
DEST_DIR="${DEST_DIR:-${SCRIPT_DIR}/../logs}"
# 配置：日志匹配模式
LOG_PATTERN="${LOG_PATTERN:-nohup*.out}"
# 配置：日志完整路径（为空时使用目录匹配）
NOHUP_PATH="${NOHUP_PATH:-${SCRIPT_DIR}/../bin/nohup.out}"
# 配置：时间格式
DATE_FORMAT="${DATE_FORMAT:-%Y%m%d%H%M%S}"
# 配置：是否压缩备份文件（true/false）
COMPRESS_ENABLED="${COMPRESS_ENABLED:-false}"
# 配置：是否截断原日志（true/false）
TRUNCATE_AFTER_BACKUP="${TRUNCATE_AFTER_BACKUP:-false}"
# 配置：备份保留天数（小于等于0表示不清理）
RETAIN_DAYS="${RETAIN_DAYS:-30}"
# 创建目标目录
mkdir -p "$DEST_DIR"
# 生成时间戳
DATE_STR=$(date +"$DATE_FORMAT")
FOUND_LOG=false
BACKUP_COUNT=0
SKIP_COUNT=0
echo "备份开始：SRC_DIR=$SRC_DIR DEST_DIR=$DEST_DIR LOG_PATTERN=$LOG_PATTERN NOHUP_PATH=$NOHUP_PATH DATE_FORMAT=$DATE_FORMAT COMPRESS_ENABLED=$COMPRESS_ENABLED TRUNCATE_AFTER_BACKUP=$TRUNCATE_AFTER_BACKUP RETAIN_DAYS=$RETAIN_DAYS"
# 遍历日志文件
if [ -n "$NOHUP_PATH" ] && [ -f "$NOHUP_PATH" ]
then
    LOG_FILES="$NOHUP_PATH"
else
    LOG_FILES="$SRC_DIR"/$LOG_PATTERN
fi
for LOG_FILE in $LOG_FILES
do
    # 判断是否为普通文件
    if [ -f "$LOG_FILE" ]
    then
        FOUND_LOG=true
        # 计算文件名
        FILE_NAME=$(basename "$LOG_FILE")
        # 生成备份文件路径
        BACKUP_FILE="${DEST_DIR}/${FILE_NAME}.${DATE_STR}"
        # 复制日志到备份目录
        cp "$LOG_FILE" "$BACKUP_FILE"
        echo "已备份：$LOG_FILE -> $BACKUP_FILE"
        BACKUP_COUNT=$((BACKUP_COUNT + 1))
        # 判断是否需要压缩
        if [ "$COMPRESS_ENABLED" = "true" ]
        then
            # 压缩备份文件
            gzip -f "$BACKUP_FILE"
            echo "已压缩：${BACKUP_FILE}.gz"
        fi
        # 判断是否需要截断原日志
        if [ "$TRUNCATE_AFTER_BACKUP" = "true" ]
        then
            # 清空原日志内容
            : > "$LOG_FILE"
            echo "已截断：$LOG_FILE"
        fi
    else
        SKIP_COUNT=$((SKIP_COUNT + 1))
    fi
done
if [ "$FOUND_LOG" = "false" ]
then
    if [ -n "$NOHUP_PATH" ]
    then
        echo "未找到匹配日志：$NOHUP_PATH"
    else
        echo "未找到匹配日志：$SRC_DIR/$LOG_PATTERN"
    fi
fi
# 判断是否需要清理历史备份
if [ "$RETAIN_DAYS" -gt 0 ]
then
    # 生成备份匹配模式
    BACKUP_PATTERN="${LOG_PATTERN}*"
    # 清理过期备份文件
    DELETED_COUNT=$(find "$DEST_DIR" -type f -name "$BACKUP_PATTERN" -mtime +"$RETAIN_DAYS" -print -delete | wc -l | tr -d ' ')
    if [ "$DELETED_COUNT" -gt 0 ]
    then
        echo "已清理过期备份：$DELETED_COUNT"
    else
        echo "无过期备份需要清理"
    fi
fi
echo "备份结束：备份文件数=$BACKUP_COUNT 跳过项数=$SKIP_COUNT"

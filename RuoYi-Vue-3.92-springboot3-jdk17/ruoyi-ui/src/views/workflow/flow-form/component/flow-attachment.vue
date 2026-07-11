<template>
  <!-- 附件 -->
  <div class="attach-list">
    <div class="box" v-loading="loading" :element-loading-text="loadingText" element-loading-spinner="el-icon-loading">
      <div class="tab-head">
        <div class="att-name">{{ attachmentConf.name }}</div>
        <div>
          <el-button size="mini" type="primary" plain icon="el-icon-download" @click="handleAllDownload()">全部下载</el-button>
        </div>
      </div>
      <div v-if="!isDetail" class="load">
        <uploader
          ref="uploader"
          :options="initOptions"
          :fileStatusText="fileStatusText"
          :autoStart="false"
          @file-added="onFileAdded"
          @file-success="onFileSuccess"
          @file-progress="onFileProgress"
          @file-error="onFileError"
          class="uploader-app"
        >
          <uploader-unsupport></uploader-unsupport>
          <uploader-drop>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              <span class="el-upload__desc">将文件拖到此处，或</span>
              <uploader-btn>点击上传</uploader-btn>
              <div class="el-upload__tip" slot="tip">
                注意：
                <span v-if="attachmentConf && attachmentConf.tipContent">{{ attachmentConf.tipContent }}、</span>
                <span>文件最大不超过{{ attachmentConf.limitSize }}Mb</span>
              </div>
            </div>
          </uploader-drop>
          <div class="global-uploader" v-show="panelShow">
            <uploader-list>
              <div class="file-panel" slot-scope="props" :class="{ collapse: collapse }">
                <ul class="file-list">
                  <li class="file-item" v-for="file in props.fileList" :key="file.id">
                    <uploader-file :class="['file_' + file.id, customStatus]" ref="files" :file="file" :list="true"></uploader-file>
                  </li>
                </ul>
              </div>
            </uploader-list>
          </div>
        </uploader>
      </div>
      <el-divider v-if="isDetail" />
      <div class="table">
        <!-- 拖拽排序 -->
        <draggable v-model="tableList" :group="group" animation="300" chosenClass="chosenClass" @sort="onSort">
          <transition-group>
            <div class="item" v-for="item in tableList" :key="item.uid">
              <div>
                <img :src="extNameImg(item)" class="item-img" />
              </div>
              <div class="item-title">
                <div>
                  <span>{{ item.name }}</span>
                  <span class="pl-5">
                    <el-button size="small" type="text" @click="handleReName(item)">
                      <svg-icon icon-class="file_rename" />
                    </el-button>
                  </span>
                </div>
                <div class="item-size">{{ calSize(item) }}</div>
              </div>
              <div class="item-btn">
                <el-button size="small" type="text" icon="el-icon-view" @click="previewFile(item)">预览</el-button>
                <el-button size="small" type="text" icon="el-icon-download" @click="downloadFile(item)">下载</el-button>
                <!-- <el-button size="small" type="text" icon="el-icon-download" @click="printFile(item)">打印</el-button> -->
                <el-button v-if="!isDetail" size="small" type="text" icon="el-icon-delete" @click="deleteFile(item)">删除</el-button>
              </div>
            </div>
          </transition-group>
        </draggable>
      </div>
      <el-empty v-if="tableList && tableList.length == 0" :image-size="100"></el-empty>
    </div>
    <!-- 修改文件名 -->
    <el-dialog title="文件重命名" :visible.sync="open" width="800px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="文件名" prop="fileName">
          <el-input v-model="form.fileName" type="text" maxlength="200" show-word-limit placeholder="请输入文件名" @input="change()" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import { getToken } from "@/utils/auth";
import { calculateFileSize, parseTime } from "@/utils/ruoyi";
import { fileImgMap, unknownImg } from "@/utils/fileMap.js";
import { rename, sort, delFile } from "@/api/file/operate.js";
import Base from "@/utils/base64";
import { listAttachment, addAttachment, remove } from "@/api/workflow/attachment";
import SparkMD5 from "spark-md5";

export default {
  name: "FlowAttachment",
  components: { draggable },
  data() {
    return {
      loading: false,
      loadingText: "加载中，请稍后...",
      // 请求头，必须携带，否则会被拦截并返回401
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      // 业务id
      businessId: null,
      tableList: [], // 文件列表数据
      open: false, // 是否显示弹出层
      form: {}, // 文件名修改表单
      // 表单校验
      rules: {
        fileName: [{ required: true, message: "文件名不能为空", trigger: "blur" }],
      },
      // 拖拽分组
      group: {
        name: "file",
        pull: true,
        put: true,
      },
      // 预览相关
      appUrl: process.env.VUE_APP_URL, // 访问url地址
      preViewUrl: process.env.VUE_APP_PREVIEW_URL, // 预览地址
      // 分片参数
      initOptions: {
        target: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile",
        chunkSize: 10 * 1024 * 1024, // 分片大小
        maxChunkRetries: 3, // 最大重试次数
        testChunks: true, // 是否开启服务器分片校验
        // 服务器分片校验函数，秒传及断点续传基础
        checkChunkUploadedByResponse: function (chunk, message) {
          let skip = false;
          try {
            let objMessage = JSON.parse(message);
            let data = objMessage.data[0];
            if (data.skipUpload) {
              skip = true;
            } else {
              skip = (data.uploaded || []).indexOf(chunk.offset + 1) >= 0;
            }
          } catch (e) {}
          return skip;
        },
        headers: {
          Authorization: "Bearer " + getToken(),
        },
        query: (file, chunk) => {
          return {
            ...file.params,
            chunkFlag: true, // 是否分片，是，必填
          };
        },
        parseTimeRemaining: function (timeRemaining, parsedTimeRemaining) {
          // timeRemaining{Number}, 剩余时间，秒为单位
          // parsedTimeRemaining{String}, 默认展示的剩余时间内容
          return parsedTimeRemaining
            .replace(/\syears?/, "年")
            .replace(/\days?/, "天")
            .replace(/\shours?/, "小时")
            .replace(/\sminutes?/, "分钟")
            .replace(/\sseconds?/, "秒");
        },
      },
      fileStatusText: {
        success: "上传成功",
        error: "上传失败",
        uploading: "上传中",
        paused: "已暂停",
        waiting: "等待上传",
      },
      panelShow: false, //选择文件后，展示上传panel
      collapse: false,
      customParams: {},
      customStatus: "",
    };
  },
  props: {
    attachmentConf: {
      type: Object,
      default: () => {},
    },
    isDetail: {
      type: Boolean,
      default: true,
    },
  },
  computed: {
    // 文件扩展名对应的图标
    extNameImg() {
      return function (row) {
        var ext = row.name.substring(row.name.lastIndexOf(".") + 1);
        return fileImgMap.get(ext.toLowerCase()) || unknownImg;
      };
    },
    // 计算文件大小
    calSize() {
      return function (row) {
        return calculateFileSize(row.size);
      };
    },
  },
  methods: {
    // 获取列表数据
    getAttachs(businessId) {
      this.businessId = businessId;
      this.loading = true;
      if (this.tableList && this.tableList.length > 0) {
        this.loading = false;
        return;
      }
      listAttachment(businessId).then((res) => {
        if (res.code === 200 && res.data) {
          this.tableList = res.data.map((file) => ({
            uid: file.fileId,
            name: file.fileName,
            size: file.fileSize,
            // status: file.status,
            fileId: file.fileId,
            identifier: file.identifier,
            sort: file.sort,
          }));
        }
        this.loading = false;
      });
    },
    // 删除
    deleteFile(file, index) {
      this.$confirm("确认删除：" + file.name + "吗？", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" })
        .then(() => {
          const fileId = file.fileId;
          this.tableList.some((item, index) => {
            if (item.fileId === fileId) {
              this.tableList.splice(index, 1);
              return true;
            }
          });
          // 逻辑删除
          delFile({ fileId: fileId }).then((res) => {
            if (res.code === 200) {
              // 同步删除业务表
              remove(fileId);
            }
          });
        })
        .catch(() => console.info("操作取消"));
    },
    // 强制刷新input
    change() {
      this.$forceUpdate();
    },
    // 文件重命名
    handleReName(row) {
      this.form.fileId = row.fileId;
      var name = row.name.substring(0, row.name.lastIndexOf("."));
      this.form.fileName = name;
      this.open = true;
    },
    // 文件重命名提交
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          rename(this.form).then((response) => {
            this.open = false;
            this.tableList.some((item) => {
              if (item.fileId === this.form.fileId) {
                var ext = item.name.substring(item.name.lastIndexOf("."));
                item.name = this.form.fileName + ext;
                return true;
              }
            });
          });
        }
      });
    },
    // 文件重命名取消
    cancel() {
      this.open = false;
      this.form = {};
    },
    // 下载
    downloadFile(row) {
      let param = { fileId: row.fileId };
      this.download(
        "file/operate/downloadfile",
        {
          ...param,
        },
        `${row.name}`
      );
    },
    // 全部下载
    handleAllDownload() {
      let arrs = [];
      this.tableList.forEach((item) => {
        arrs.push(item.fileId);
      });
      if (arrs.length <= 0) {
        this.$message.warning("没有可下载的文件！");
        return;
      }
      let fileName = parseTime(new Date(), "{y}{m}{d}{h}{i}{s}") + ".zip";
      this.download(
        "file/operate/batchDownloadFile",
        {
          fileIds: arrs,
          downType: "1",
        },
        fileName
      );
    },
    // 预览
    previewFile(row) {
      let token = "Bearer " + getToken();
      var base = new Base();
      var url = this.appUrl + "/file/operate/preview?Authorization=" + token + "&fileId=" + row.fileId + "&fullfilename=" + row.name; //要预览文件的访问地址
      window.open(this.preViewUrl + encodeURIComponent(base.encode(url)));
    },
    printFile(row) {
      window.print();
    },
    // 拖拽排序，位置变化时，重新排序
    onSort(e) {
      this.tableList.forEach((item, index) => {
        item.sort = index;
      });
      let param = { fileList: this.tableList };
      sort(param);
    },
    /** 添加文件 */
    onFileAdded(file) {
      // 校检文件类型
      if (this.attachmentConf && this.attachmentConf.limitType) {
        const limitType = this.attachmentConf.limitType;
        const fileName = file.name.split(".");
        const fileExt = fileName[fileName.length - 1];
        const isTypeOk = limitType.indexOf(fileExt) >= 0;
        if (!isTypeOk) {
          this.$message.error(`文件格式不正确，请上传${limitType}格式文件!`);
          file.cancel();
          return;
        }
      }
      // 校检文件大小
      if (this.attachmentConf && this.attachmentConf.limitSize) {
        const limitSize = Number(this.attachmentConf.limitSize || "500");
        const isLt = file.size / 1024 / 1024 > limitSize;
        if (isLt) {
          this.$modal.msgError(`上传文件大小不能超过 ${limitSize} MB!`);
          file.cancel();
          return;
        }
      }
      this.panelShow = true;
      this.collapse = false;
      // 将额外的参数赋值到每个文件上，以不同文件使用不同params的需求
      file.params = this.customParams;

      // 计算MD5
      this.computeMD5(file).then((result) => this.startUpload(result));
    },
    /**
     * 计算md5值，以实现断点续传及秒传
     * @param file
     * @returns Promise
     */
    computeMD5(file) {
      let fileReader = new FileReader();
      let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
      let currentChunk = 0;
      const chunkSize = 10 * 1024 * 1024;
      let chunks = Math.ceil(file.size / chunkSize);
      let spark = new SparkMD5.ArrayBuffer();

      // 文件状态设为"计算MD5"
      this.statusSet(file.id, "md5");
      // file.pause();
      loadNext();
      return new Promise((resolve, reject) => {
        fileReader.onload = (e) => {
          spark.append(e.target.result);
          if (currentChunk < chunks) {
            currentChunk++;
            loadNext();
          } else {
            let md5 = spark.end();
            // md5计算完毕
            resolve({ md5, file });
          }
        };
        fileReader.onerror = function () {
          this.$message.error(`文件${file.name}读取出错，请检查该文件`);
          file.cancel();
          reject();
        };
      });
      function loadNext() {
        let start = currentChunk * chunkSize;
        let end = start + chunkSize >= file.size ? file.size : start + chunkSize;
        fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end));
      }
    },

    // md5计算完毕，开始上传
    startUpload({ md5, file }) {
      file.identifier = md5;
      file.resume();
      this.statusRemove(file.id);
    },

    onFileSuccess(rootFile, file, response, chunk) {
      let res = JSON.parse(response);
      // 服务端自定义的错误（http状态码始终为200，失败时Uploader无法拦截），通过自定义码来处理失败
      if (res.code != 200) {
        this.statusSet(file.id, "failed");
        this.$message.error(res.msg);
        return;
      }
      const { fileId, identifier, sort } = res.data[0];
      this.tableList.push({
        uid: file.identifier,
        name: file.name,
        size: file.size,
        fileId: fileId,
        identifier: identifier,
        sort: sort,
      });
      // 添加到附件业务表中
      const data = {
        businessId: this.businessId,
        fileId: fileId,
      };
      addAttachment(data);
      // 上传完成后，关闭展示框
      setTimeout(() => {
        this.collapse = true;
      }, 3000);
    },
    /** 正在上传事件处理 */
    onFileProgress(rootFile, file, chunk) {},
    /** 上传失败处理 */
    onFileError(rootFile, file, response, chunk) {},
    /** 关闭 */
    close() {
      this.uploader.cancel();
      this.panelShow = false;
    },
    /** 新增的自定义的状态 */
    statusSet(id, status) {
      let statusMap = {
        md5: {
          text: "（校验MD5...）",
          bgc: "#E6A23C",
        },
        merging: {
          text: "（合并中...）",
          bgc: "#E6A23C",
        },
        transcoding: {
          text: "（转码中...）",
          bgc: "#E6A23C",
        },
        failed: {
          text: "上传失败",
          bgc: "#F56C6C",
        },
      };
      this.customStatus = status;
      this.$nextTick(() => {
        // 先移除原有的状态文本
        const existingStatus = document.querySelector(`.file_${id} .uploader-file-status`);
        if (existingStatus) {
          existingStatus.innerHTML = "";
        }
        const statusTag = document.createElement("span");
        statusTag.className = `custom-status-${id} custom-status`;
        statusTag.innerText = statusMap[status].text;
        statusTag.style.color = statusMap[status].bgc;
        const statusWrap = document.querySelector(`.file_${id} .uploader-file-status`);
        statusWrap.appendChild(statusTag);
      });
    },
    statusRemove(id) {
      this.customStatus = "";
      this.$nextTick(() => {
        const statusTag = document.querySelector(`.custom-status-${id}`);
        statusTag.remove();
      });
    },
  },
};
</script>

<style scoped lang="scss">
.attach-list {
  margin-left: 24px;
}
.box {
  background-color: #fff;

  .tab-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 6px;

    .att-name {
      font-size: 14px;
      font-weight: 600;
    }
  }

  .load {
    width: 100%;
    text-align: center;

    .upload {
      color: #409eff;
      cursor: pointer;
    }

    .el-upload__desc {
      font-size: 14px;
      color: #aeaeae;
    }

    .el-upload__tip {
      font-size: 13px;
      color: #aeaeae;
    }
  }

  ::v-deep .el-upload {
    width: 100%;
  }
  ::v-deep .el-upload .el-upload-dragger {
    width: 100%;
  }
}
.table {
  margin-top: 24px;

  .item {
    display: flex;
    align-items: center;
    line-height: 24px;
    padding-left: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
    border-bottom: solid 1px #ebebeb;
    background-color: #fff;

    .item-img {
      width: 35px;
      height: 35px;
    }
    .item-title {
      font-size: 14px;
      margin-left: 12px;
    }

    .item-size {
      color: #aeaeae;
      font-size: 12px;
    }

    .item-btn {
      margin-left: auto;
      margin-right: 10px;
    }
  }
  .item:hover {
    background-color: rgb(249, 249, 249);
  }
}

.chosenClass {
  background-color: rgb(249, 249, 249) !important;
  opacity: 1 !important;
}

.uploader-drop {
  border: 1px dashed #d9d9d9;
  background-color: #fff;
}
.el-icon-upload {
  font-size: 67px;
  color: #c0c4cc;
  line-height: 1;
}
.el-upload__text {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #606266;
  font-size: 14px;

  em {
    color: #409eff;
    font-style: normal;
    font-weight: 600;
  }
  .el-upload__desc {
    margin-bottom: 10px;
    font-size: 14px;
    color: #606266;
  }
  .el-upload__tip {
    padding: 0 20px;
    font-size: 12px;
    color: #909399;

    span {
      display: inline-block;
    }
  }
  .uploader-btn {
    margin-right: 0.2rem;
    border: 1px dashed #409eff;
    color: #409eff;
    border-radius: 0.15rem;
    &:hover {
      background-color: #fff;
    }
  }
}

.global-uploader {
  position: fixed;
  z-index: 20;
  right: 15px;
  bottom: 15px;
  box-sizing: border-box;
  width: 800px;
  opacity: 1;

  &.fadeOut {
    opacity: 0;
  }

  .uploader-app {
    width: 520px;
  }

  .file-panel {
    background-color: #fff;
    border: 1px solid #e2e2e2;
    border-radius: 7px 7px 0 0;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);

    .file-title {
      display: flex;
      height: 40px;
      line-height: 40px;
      padding: 0 15px;
      border-bottom: 1px solid #ddd;

      .operate {
        flex: 1;
        text-align: right;

        i {
          font-size: 18px;
        }
      }
    }

    .file-list {
      position: relative;
      height: 240px;
      overflow-x: hidden;
      overflow-y: auto;
      background-color: #fff;
      transition: all 0.3s;
      margin: 0;
      padding: 0;

      .file-item {
        background-color: #fff;
        list-style: none;
        padding: 5px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        ::v-deep .uploader-file {
          text-align: left;

          .uploader-file-name {
            text-align: left;
          }

          .uploader-file-meta {
            text-align: left;
          }
        }
      }
    }
    &:not(.collapse) .file-list {
      transition: height 0.3s ease-out;
    }
    &.collapse {
      .file-list {
        transition: height 1.5s ease-out;
        height: 0;
      }
    }
  }
}

// 如果需要更精确地控制uploader-file组件内部的样式
::v-deep .uploader-file {
  font-size: 14px;
  height: 42px;
}
</style>

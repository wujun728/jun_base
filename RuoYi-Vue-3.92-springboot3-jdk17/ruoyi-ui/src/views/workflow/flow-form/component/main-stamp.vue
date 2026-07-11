<template>
  <!-- 正文盖章 -->
  <div>
    <div class="pdf-container">
      <!-- 控制区域 -->
      <div class="controls">
        <div class="btns">
          <!-- 左侧按钮组 -->
          <div class="control-group">
            <el-button size="mini" @click="prevPage" :disabled="currentPage === 1" icon="el-icon-arrow-left" class="ml20">上一页</el-button>
            <span class="btn-text">{{ currentPage }} / {{ pageCount }}</span>
            <el-button size="mini" @click="nextPage" :disabled="currentPage === pageCount">
              下一页
              <i class="el-icon-arrow-right"></i>
            </el-button>
            <!-- <el-button size="mini" @click="zoomOut" icon="el-icon-zoom-out">缩小</el-button>
            <span class="btn-text">{{ Math.round(scale * 100) }}%</span>
            <el-button size="mini" @click="zoomIn" icon="el-icon-zoom-in">放大</el-button>-->
            <el-button size="mini" type="text" @click="goPageNum" class="to-page-btn mr5">跳转到</el-button>
            <el-input v-model="toPageNum" class="to-page-input" size="mini"></el-input>
          </div>
          <!-- 右侧按钮组 -->
          <div class="control-group">
            <div class="seal">
              <div class="help">
                <el-tooltip class="item" effect="dark" placement="bottom">
                  <div slot="content">
                    <div v-for="(line, index) in helpLines" :key="index">{{ line }}</div>
                  </div>
                  <svg-icon icon-class="help-light" class="svg"></svg-icon>
                </el-tooltip>
              </div>
              <div class="text" @click="addStamp">
                <svg-icon icon-class="gesture-right" class="gesture"></svg-icon>
              </div>
              <el-button size="mini" @click="addStamp" class="stamp-btn">
                <el-tooltip class="item" effect="dark" content="点此选择印章" placement="top">
                  <img src="@/views/workflow/mainSeal/images/gongzhang.png" alt="印章" class="stamp-icon" />
                </el-tooltip>
              </el-button>
            </div>
            <el-tooltip class="item" effect="dark" content="提交后，完成当前盖章。" placement="top">
              <el-button type="primary" size="mini" @click="submitStamps">提 交</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="返回预览页面。" placement="top">
              <el-button size="mini" @click="changePreview">关 闭</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="未提交前，可以撤销上一次盖章。" placement="top">
              <el-button size="mini" @click="undoStamp">撤 销</el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="未提交前，可以选中某一个印章删除。" placement="top">
              <el-button size="mini" @click="deleteSelected" class="mr20">删 除</el-button>
            </el-tooltip>
          </div>
        </div>
      </div>
      <div class="scroll-wrapper" ref="scrollContainer">
        <!-- PDF预览区域 -->
        <div class="pdf-viewer" ref="pdfContainer">
          <pdf :src="pdfUrl" :page="currentPage" @num-pages="pageCount = $event" @page-loaded="handlePageLoaded" ref="pdfViewer" class="pdf-document"></pdf>
          <div
            v-for="stamp in visibleStamps"
            :key="stamp.id"
            class="stamp-wrapper"
            :style="getStampStyle(stamp)"
            @mousedown="startDrag(stamp, $event)"
            @touchstart.passive="startDrag(stamp, $event)"
          >
            <img
              :src="stamp.chooseSealImgData"
              alt="印章"
              class="stamp-img"
              :class="{ selected: selectedStamp === stamp.id }"
              @click.stop="selectStamp(stamp.id)"
            />
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="选择印章" :visible.sync="open" width="800px" append-to-body :close-on-click-modal="false">
      <el-divider />
      <div class="seal-container" v-loading="sealLoading" element-loading-text="正在加载中..." element-loading-spinner="el-icon-loading">
        <el-radio-group v-model="chooseSeal" class="seal-grid">
          <div v-for="item in sealList" :key="item.id" class="seal-item">
            <div class="seal-card" :class="{ 'selected': chooseSeal === item.fileId }" @click="handleImageClick(item.fileId)">
              <div class="seal-img-container">
                <img :src="item.previewBase64" alt="印章预览" class="seal-image" />
                <div class="selection-mask">
                  <i class="el-icon-check check-icon"></i>
                </div>
              </div>
            </div>
            <el-radio :label="item.fileId" class="seal-label">{{ item.sealName }}</el-radio>
          </div>
        </el-radio-group>
        <el-empty v-if="!sealList || sealList.length <= 0" description="请前往：流程管理 -> 印章配置，配置正文印章！" :image-size="80"></el-empty>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="sealList && sealList.length > 0" type="primary" @click="submitSeal">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import pdf from "vue-pdf";
import { getToken } from "@/utils/auth";
import { preview } from "@/api/workflow/form";
import { stamp } from "@/api/workflow/mainText";
import { findAllSeals } from "@/api/workflow/mainSeal";

export default {
  name: "MainTextStamp",
  components: { pdf },
  data() {
    return {
      // pdf访问地址，可以是直接访问的url，也可以是文件流，但需要转成url
      pdfUrl: "",
      // 跳转到
      toPageNum: null,
      // 总页数
      pageCount: 0,
      // 当前页
      currentPage: this.pageCount | 1,
      // 初始缩放比例
      scale: 1,
      // 印章图片数据
      stamps: [],
      // 选中的印章
      selectedStamp: null,
      // 印章历史数据
      stampHistory: [],
      // 页面尺寸
      pageDimensions: {},
      // 容器尺寸
      containerRect: null,
      // pdf默认宽度（A4）
      pdfDefaultWidth: 595,
      // pdf默认高度（A4）
      pdfDefaultHeight: 842,
      // 选择印章弹框
      open: false,
      // 印章列表数据
      sealList: [],
      // 选择的印章
      chooseSeal: null,
      // 选择印章数据加载状态
      sealLoading: false,
      // 帮忙说明
      helpLines: [
        "使用帮助：",
        "1、点击右侧印章图片标识，从弹出框中选择印章；",
        "2、点击正文中的印章图片，拖动到需要的位置；",
        "3、未提交前，可以对印章进行撤销、删除操作；",
        "4、一次可以提交多个印章；",
        "5、提交后，本次盖章生效，如需再次盖章，请先还原印章。",
      ],
    };
  },
  props: {
    fileId: {
      type: String,
      default: null,
    },
    businessId: {
      type: String,
      default: null,
    },
    title: {
      type: String,
      default: null,
    },
  },
  computed: {
    visibleStamps() {
      return this.stamps.filter((stamp) => stamp.page === this.currentPage);
    },
  },
  mounted() {
    this.loadPdf();
    window.addEventListener("resize", this.updateContainerRect);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.updateContainerRect);
  },
  methods: {
    /** 初始化PDF */
    async loadPdf() {
      let token = "Bearer " + getToken();
      let param = {
        Authorization: token,
        fileId: this.fileId,
      };
      preview(param).then((res) => {
        const blob = new Blob([res], { type: "application/pdf" });
        this.pdfUrl = URL.createObjectURL(blob);
        this.loading = false;
        this.showPdf = true;
      });
    },
    /** 加载页数 */
    handlePageLoaded(page) {
      this.$nextTick(() => {
        // 更新容器尺寸计算
        this.updateContainerRect();
        const container = this.$refs.pdfContainer;
        if (container) {
          // 应用缩放
          container.style.transform = `scale(${this.scale})`;
          container.style.transformOrigin = "top center";
          // 记录页面尺寸
          this.pageDimensions[this.currentPage] = {
            width: container.offsetWidth,
            height: container.offsetHeight,
          };
        }
      });
    },
    /** 上一页 */
    prevPage() {
      this.currentPage = Math.max(1, this.currentPage - 1);
      this.handlePageChange();
    },
    /** 下一页 */
    nextPage() {
      this.currentPage = Math.min(this.pageCount, this.currentPage + 1);
      this.handlePageChange();
    },
    /** 页面变更 */
    handlePageChange() {
      this.$nextTick(() => {
        this.updateContainerRect();
      });
    },
    /** 跳转到 */
    goPageNum() {
      if (!this.toPageNum) {
        return;
      }
      let page = this.toPageNum > this.pageCount ? this.pageCount : this.toPageNum;
      this.currentPage = Number(page);
      this.handlePageChange();
    },
    /** 缩小 */
    zoomIn() {
      this.scale = Math.min(1.5, this.scale + 0.1);
      this.$nextTick(this.handlePageLoaded);
    },
    /** 放大 */
    zoomOut() {
      this.scale = Math.max(0.5, this.scale - 0.1);
      this.$nextTick(this.handlePageLoaded);
    },
    /** 添加印章 */
    addStamp() {
      this.open = true;
      this.sealLoading = true;
      findAllSeals()
        .then((res) => {
          if (res.code === 200) {
            this.sealList = res.data;
          }
          this.sealLoading = false;
        })
        .catch((e) => {
          this.sealLoading = false;
        });
    },
    /** 单选事件 */
    handleImageClick(sealFileId) {
      this.chooseSeal = sealFileId;
    },
    /** 选择印章确定 */
    submitSeal() {
      if (!this.chooseSeal) {
        this.$modal.msgError("请选择印章！");
        return;
      }
      let chooseSealImgData = null;
      this.sealList.some((item) => {
        if (item.fileId === this.chooseSeal) {
          chooseSealImgData = item.previewBase64;
        }
      });
      const newStamp = {
        id: new Date().getTime(),
        page: this.currentPage,
        position: { x: 50, y: 50 }, // 默认居中
        fixed: false,
        dimensions: this.pageDimensions[this.currentPage],
        sealFileId: this.chooseSeal,
        chooseSealImgData: chooseSealImgData,
      };
      this.stampHistory.push(JSON.stringify(this.stamps));
      this.stamps.push(newStamp);
      this.selectStamp(newStamp.id);
      this.open = false;
    },
    /** 印章样式 */
    getStampStyle(stamp) {
      return {
        left: `${stamp.position.x}%`,
        top: `${stamp.position.y}%`,
        cursor: stamp.fixed ? "default" : "move",
      };
    },
    /** 提交盖章 */
    async submitStamps() {
      if (!this.stamps || this.stamps.length <= 0) {
        this.$modal.msgError("请先盖章再提交！");
        return;
      }
      this.$emit("submitLoading", true);
      let sealInfos = this.stamps.map((stamp) => ({
        sealFileId: stamp.sealFileId,
        positionX: this.calculatePdfX(stamp),
        positionY: this.calculatePdfY(stamp),
        sealPage: stamp.page,
      }));
      let param = {
        businessId: this.businessId,
        title: this.title,
        sealInfos: sealInfos,
      };
      stamp(param).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess("保存成功");
          this.changePreview();
        }
      });
    },
    /** 返回预览视图 */
    changePreview() {
      this.$emit("showPreview");
    },
    /** 撤销操作 */
    undoStamp() {
      if (this.stampHistory.length > 0) {
        this.stamps = JSON.parse(this.stampHistory.pop());
      }
    },
    /** 删除选中 */
    deleteSelected() {
      if (this.selectedStamp) {
        this.stampHistory.push(JSON.stringify(this.stamps));
        this.stamps = this.stamps.filter((s) => s.id !== this.selectedStamp);
        this.selectedStamp = null;
      }
    },
    /** 手动拖拽 */
    startDrag(stamp, event) {
      if (stamp.fixed) return;
      this.selectStamp(stamp.id);
      const container = this.$refs.pdfContainer; // 指向滚动容器
      const isTouch = event.type === "touchstart";
      // 获取初始触点坐标（包含滚动补偿）
      const getTruePosition = (clientX, clientY) => ({
        x: clientX + container.scrollLeft,
        y: clientY + container.scrollTop,
      });
      // 初始触点坐标
      const startClientX = isTouch ? event.touches[0].clientX : event.clientX;
      const startClientY = isTouch ? event.touches[0].clientY : event.clientY;
      const startPos = getTruePosition(startClientX, startClientY);
      // 记录印章初始百分比位置
      const startStampX = stamp.position.x;
      const startStampY = stamp.position.y;
      const moveHandler = (e) => {
        // 实时获取最新触点坐标（含滚动补偿）
        const currentClientX = isTouch ? e.touches[0].clientX : e.clientX;
        const currentClientY = isTouch ? e.touches[0].clientY : e.clientY;
        const currentPos = getTruePosition(currentClientX, currentClientY);
        // 计算基于PDF文档的增量（物理像素）
        const deltaX = currentPos.x - startPos.x;
        const deltaY = currentPos.y - startPos.y;
        // 转换为百分比
        const newX = startStampX + (deltaX / this.pdfDefaultWidth) * 100;
        const newY = startStampY + (deltaY / this.pdfDefaultHeight) * 100;
        this.$set(stamp.position, "x", Math.max(2, Math.min(98, newX)));
        this.$set(stamp.position, "y", Math.max(2, Math.min(98, newY)));
      };
      const upHandler = () => {
        document.removeEventListener(isTouch ? "touchmove" : "mousemove", moveHandler);
        document.removeEventListener(isTouch ? "touchend" : "mouseup", upHandler);
      };
      document.addEventListener(isTouch ? "touchmove" : "mousemove", moveHandler);
      document.addEventListener(isTouch ? "touchend" : "mouseup", upHandler);
    },
    /** 更新容器尺寸 */
    updateContainerRect() {
      const container = this.$refs.pdfContainer;
      if (!container) return;
      const rect = container.getBoundingClientRect();
      this.containerRect = {
        width: rect.width,
        height: rect.height,
        left: rect.left,
        top: rect.top,
      };
    },
    /** 坐标转换 */
    calculatePdfX(stamp) {
      return (stamp.position.x / 100) * this.pdfDefaultWidth;
    },
    calculatePdfY(stamp) {
      return this.pdfDefaultHeight - (stamp.position.y / 100) * this.pdfDefaultHeight;
    },
    /** 选择印章 */
    selectStamp(id) {
      this.selectedStamp = id;
    },
  },
};
</script>

<style lang="scss" scoped>
.pdf-container {
  position: relative;
  margin: 0 auto;
  background: #d4d4d7;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .controls {
    background: #4e6896;

    .btns {
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: sticky;
      top: 0;
      z-index: 1;
    }

    .control-group {
      display: flex;
      align-items: center;

      .btn-text {
        font-size: 12px;
        color: #fff;
        text-align: center;
        min-width: 40px;
      }
    }

    .seal {
      display: flex;
      justify-content: space-between;
      margin: 5px;
      margin-right: 10px;

      .text {
        flex: 1;
        margin: auto 0;
        color: #fff;
        margin-right: 5px;
      }

      .help {
        flex: 1;
        margin: auto 0;
        color: #fff;
        margin-right: 10px;

        .svg {
          width: 20px;
          height: 20px;
        }
      }
    }

    .gesture {
      width: 20px;
      height: 20px;
    }

    .stamp-btn {
      padding: 1px;
      padding-left: 5px;
      padding-right: 5px;
      .stamp-icon {
        width: 32px;
        height: 32px;
      }
    }

    .to-page-btn {
      color: #fff;
    }

    .to-page-input {
      width: 50px;
      ::v-deep .el-input__inner {
        padding-left: 8px;
        padding-right: 8px;
      }
    }
  }

  .scroll-wrapper {
    flex: 1;
    overflow: auto;
    position: relative;
  }

  .pdf-viewer {
    border: 1px solid #e1e1e1;
    margin: 0 auto; /* 水平居中 */
    padding-bottom: 20px;
    width: 100%;
    max-width: 800px;
    min-height: 100%;
    background: white;

    .pdf-document {
      margin: 0 auto;
    }

    ::v-deep .page {
      margin: 20px auto;
      border: 1px solid #ccc;
      box-shadow: none;
      max-height: calc(100vh - 60px); // 控制单页最大高度
      .resize-sensor {
        display: none;
      }
    }
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: rgba(0, 0, 0, 0.2);
      border-radius: 2px;
      &:hover {
        background-color: rgba(0, 0, 0, 0.3);
      }
    }

    .stamp-wrapper {
      position: absolute;
      transform: translate(-50%, -50%);
      transition: transform 0.1s;
      z-index: 1000;
      cursor: move;

      .selected {
        filter: drop-shadow(0 0 4px #f00);
      }

      .stamp-img {
        width: 110px;
        height: 110px;
        transition: transform 0.1s;
        user-select: none;
        -webkit-user-drag: none;
        // filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));

        &:active {
          cursor: grabbing;
          transform: scale(1.1);
        }
        &.selected {
          filter: drop-shadow(0 0 4px #f00);
        }
      }
      &:active {
        z-index: 1001;
      }
    }
  }
}

.seal-container {
  padding: 10px 0;
  min-height: 300px;
  .seal-grid {
    width: 100%;
    display: grid;
    grid-template-columns: repeat(3, 1fr); // 3列布局
    gap: 30px; // 项目间距
    justify-items: center; // 水平居中
  }

  .seal-item {
    position: relative;
    cursor: pointer;
    transition: all 0.3s ease;
    width: 180px;

    &:hover {
      transform: translateY(-3px);

      .seal-card {
        border-color: #409eff;
      }
    }
  }

  .seal-card {
    border: 1px solid #dfdfdf;
    border-radius: 4px;
    padding: 8px;
    transition: all 0.3s;
    background: white;
    position: relative;

    &.selected {
      background: rgb(244, 250, 255);
      border-color: #409eff;
      box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
    }
  }

  .seal-img-container {
    position: relative;
    width: 120px;
    height: 120px;
    margin: 0 auto;

    .seal-image {
      width: 100%;
      height: 100%;
      object-fit: contain;
      border-radius: 4px;
    }

    .selection-mask {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(64, 158, 255, 0.5);
      border-radius: 4px;
      opacity: 0;
      transition: opacity 0.3s;
      display: flex;
      align-items: center;
      justify-content: center;

      .check-icon {
        color: white;
        font-size: 24px;
        font-weight: bold;
      }
    }
  }

  .seal-label {
    display: block;
    text-align: center;
    margin-top: 8px;
    font-weight: 600;

    ::v-deep .el-radio__label {
      color: #606266;
      font-size: 14px;
    }
  }
}
</style>
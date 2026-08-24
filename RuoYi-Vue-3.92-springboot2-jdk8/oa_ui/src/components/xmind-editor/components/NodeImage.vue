<template>
  <el-dialog class="nodeImageDialog" title="图片" :visible.sync="dialogVisible" width="400px" top="15vh">
    <div class="node-image-wrapper" v-if="img">
      <img class="node-image" :src="img" ref="nodeImage" />
      <div class="node-image-mask">
        <i class="el-icon-delete" @click="reset" />
      </div>
    </div>
    <el-upload accept="image/*" ref="uploader" :on-change="handleImgChange" :auto-upload="false" :limit="1" action="##"
      list-type="picture-card" :show-file-list="false" v-else>
      <i class="el-icon-plus" />
    </el-upload>

    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { NODE_ACTIVE, SHOW_NODE_IMAGE } from '../event-constant';
import compressImg from '@/utils/compressImg';

export default {
  name: 'NodeImage',
  data() {
    return {
      dialogVisible: false,
      img: '',
      activeNodes: null
    }
  },
  created() {
    this.$bus.$on(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$on(SHOW_NODE_IMAGE, this.handleShowNodeImage)
  },
  beforeDestroy() {
    this.$bus.$off(NODE_ACTIVE, this.handleNodeActive)
    this.$bus.$off(SHOW_NODE_IMAGE, this.handleShowNodeImage)
  },
  methods: {

    async handleImgChange(e) {
      if (!/^image\//.test(e.raw.type)) {
        this.$refs.uploader.clearFiles();
        return this.$message.error("请上传正确的图片!");
      }
      this.img = await compressImg(e.raw, e.raw.size >= 2097152);
    },

    handleNodeActive(...args) {
      this.activeNodes = [...args[1]]
    },

    handleShowNodeImage() {
      this.reset()
      if (this.activeNodes.length > 0) {
        let firstNode = this.activeNodes[0]
        let img = firstNode.getData('image') || ''
        if (img) {
          this.img = img
        }
      }
      this.dialogVisible = true
    },

    cancel() {
      this.dialogVisible = false
      this.reset()
    },

    reset() {
      this.img = ''
    },

    async confirm() {
      try {
        // 删除图片
        if (!this.img) {
          this.cancel()
          this.activeNodes.forEach(node => {
            node.setImage(null)
          })
          return
        }
        let img = this.img;
        let { naturalHeight, naturalWidth } = this.$refs.nodeImage;
        this.activeNodes.forEach(node => {
          node.setImage({
            url: img,
            title: "",
            width: naturalWidth || 100,
            height: naturalHeight || 100
          })
        })
        this.cancel()
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.nodeImageDialog {

  .node-image-wrapper {
    width: 148px;
    height: 148px;
    border-radius: 6px;
    overflow: hidden;
    cursor: pointer;
    position: relative;

    .node-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .node-image-mask {
      position: absolute;
      top: 0;
      left: 0;
      bottom: 0;
      right: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: rgba(0, 0, 0, .5);
      opacity: 0;
      transition: all ease .3s;
      color: #fff;
      font-size: 20px;

      &:hover {
        opacity: 1;
      }
    }
  }

}
</style>

<template>
  <div style="min-height: calc(100vh - 190px)">
    <div class="message">
      <el-card :body-style="{ padding: '0 20px 20px' }">
        <div class="">
          <el-tabs v-model="currentTab" @tab-click="changeTab">
            <el-tab-pane
              v-for="(item, index) in headerList"
              :key="index"
              :label="item.label"
              :name="item.value.toString()"
            />
          </el-tabs>
        </div>
        <el-alert v-if="parseInt(currentTab) === 1" closable>
          <template slot="title">
            <p>上传图片时会生成缩略图</p>
            <p>未设置按照系统默认生成，系统默认：大图800*800，中图300*300，小图150*150</p>
          </template>
        </el-alert>
        <el-alert v-else closable>
          <template slot="title">
            <p v-if="parseInt(currentTab) === 2">
              七牛云开通方法：<a href="https://doc.crmeb.com/single/v5/7792" target="_blank">点击查看</a>
            </p>
            <p v-if="parseInt(currentTab) === 3">
              阿里云oss开通方法：<a href="https://doc.crmeb.com/single/v5/7790" target="_blank">点击查看</a>
            </p>
            <p v-if="parseInt(currentTab) === 4">
              腾讯云cos开通方法：<a href="https://doc.crmeb.com/single/v5/7791" target="_blank">点击查看</a>
            </p>
            <p v-if="parseInt(currentTab) === 5">
              京东云cos开通方法：<a href="https://doc.crmeb.com/single/v5/8522" target="_blank">点击查看</a>
            </p>
            <p v-if="parseInt(currentTab) === 6">
              华为云cos开通方法：<a href="https://doc.crmeb.com/single/v5/8523" target="_blank">点击查看</a>
            </p>
            <p v-if="parseInt(currentTab) === 7">
              天翼云cos开通方法：<a href="https://doc.crmeb.com/single/v5/8524" target="_blank">点击查看</a>
            </p>
            <p>第一步： 添加【存储空间】（空间名称不能重复）</p>
            <p>第二步： 开启【使用状态】</p>
            <template v-if="parseInt(currentTab) === 2">
              <p>第三步（必选）： 选择云存储空间列表上的修改【空间域名操作】</p>
              <p>第四步（必选）： 选择云存储空间列表上的修改【CNAME配置】，打开后复制记录值到对应的平台解析</p>
            </template>
            <template v-else>
              <p>第三步（可选）： 选择云存储空间列表上的修改【空间域名操作】</p>
              <p>第四步（可选）： 选择云存储空间列表上的修改【CNAME配置】，打开后复制记录值到对应的平台解析</p>
            </template>
          </template>
        </el-alert>
      </el-card>
    </div>
    <div v-if="parseInt(currentTab) === 1" class="pt10">
      <el-card class="ivu-mt">
        <el-row>
          <el-col :span="24">
            <span class="save-type"> 存储方式： </span>
            <el-radio-group v-model="upload_type" @change="changeSave">
              <el-radio label="1">本地存储</el-radio>
              <el-radio label="2">七牛云存储</el-radio>
              <el-radio label="3">阿里云存储</el-radio>
              <el-radio label="4">腾讯云存储</el-radio>
              <el-radio label="5">京东云存储</el-radio>
              <el-radio label="6">华为云存储</el-radio>
              <el-radio label="7">天翼云存储</el-radio>
            </el-radio-group>
            <!-- <el-switch :active-value="1"  :inactive-value="0"
              v-model="localStorage"
              size="large"
              @change="addSwitch"
            >
              <span slot="open">开启</span>
              <span slot="close">关闭</span>
             </el-switch> -->
          </el-col>
        </el-row>
      </el-card>
      <el-card class="ivu-mt">
        <el-form ref="formValidate" :model="formValidate" :rules="ruleValidate">
          <div class="abbreviation">
            <div class="top">
              <div class="topBox">
                <div class="topLeft">
                  <div class="img">
                    <img alt="" class="imgs" src="@/assets/images/abbreviationBig.png" />
                  </div>
                  <div>缩略大图</div>
                </div>
                <div class="topRight">
                  <el-form-item label="宽：">
                    <el-input
                      v-model="formValidate.thumb_big_width"
                      class="topIput"
                      placeholder="请输入宽度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="高：">
                    <el-input
                      v-model="formValidate.thumb_big_height"
                      class="topIput"
                      placeholder="请输入高度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                </div>
              </div>
              <div class="topBox">
                <div class="topLeft">
                  <div class="img">
                    <img alt="" class="imgs" src="@/assets/images/abbreviation.png" />
                  </div>
                  <div>缩略中图</div>
                </div>
                <div class="topRight">
                  <el-form-item label="宽：">
                    <el-input
                      v-model="formValidate.thumb_mid_width"
                      class="topIput"
                      placeholder="请输入宽度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="高：">
                    <el-input
                      v-model="formValidate.thumb_mid_height"
                      class="topIput"
                      placeholder="请输入高度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                </div>
              </div>
              <div class="topBox">
                <div class="topLeft">
                  <div class="img">
                    <img alt="" class="imgs" src="@/assets/images/abbreviationSmall.png" />
                  </div>
                  <div>缩略小图</div>
                </div>
                <div class="topRight">
                  <el-form-item label="宽：">
                    <el-input
                      v-model="formValidate.thumb_small_width"
                      class="topIput"
                      placeholder="请输入宽度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="高：">
                    <el-input
                      v-model="formValidate.thumb_small_height"
                      class="topIput"
                      placeholder="请输入高度"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                </div>
              </div>
            </div>
            <div class="content mt30">
              <!--              <el-form-item label="是否开启水印：" label-width="160px">-->
              <!--                <el-switch-->
              <!--                  :active-value="1"-->
              <!--                  :inactive-value="0"-->
              <!--                  v-model="formValidate.image_watermark_status"-->
              <!--                  size="large"-->
              <!--                >-->
              <!--                  <span slot="open">开启</span>-->
              <!--                  <span slot="close">关闭</span>-->
              <!--                </el-switch>-->
              <!--              </el-form-item>-->
              <div v-if="formValidate.image_watermark_status === 1">
                <el-form-item label="水印类型：" label-width="160px">
                  <el-radio-group v-model="formValidate.watermark_type">
                    <el-radio :label="1">图片</el-radio>
                    <el-radio :label="2">文字</el-radio>
                  </el-radio-group>
                </el-form-item>
                <div v-if="formValidate.watermark_type === 1">
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印透明度：" label-width="160px" prop="name">
                      <el-input
                        v-model="formValidate.watermark_opacity"
                        class="topIput"
                        placeholder="请输入水印透明度"
                        type="number"
                      >
                      </el-input>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印倾斜度：" label-width="160px" prop="mail">
                      <el-input
                        v-model="formValidate.watermark_rotate"
                        class="topIput"
                        placeholder="请输入水印倾斜度"
                        type="number"
                      >
                      </el-input>
                    </el-form-item>
                  </div>
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印图片：" label-width="160px" prop="name">
                      <div class="picBox" @click="modalPicTap('单选')">
                        <div v-if="formValidate.watermark_image" class="pictrue">
                          <img :src="formValidate.watermark_image" />
                        </div>
                        <div v-else class="upLoad acea-row row-center-wrapper">
                          <i class="el-icon-picture-outline" style="font-size: 24px"></i>
                        </div>
                      </div>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印位置：" label-width="160px" prop="mail">
                      <div class="conents">
                        <div class="positionBox">
                          <div
                            v-for="(item, index) in boxs"
                            :key="index"
                            :class="positionId === item.id ? 'on' : ''"
                            class="topIput box"
                            @click="bindbox(item)"
                          ></div>
                        </div>
                        <div class="title">{{ positiontlt }}</div>
                      </div>
                    </el-form-item>
                  </div>
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印横坐标偏移量：" label-width="160px" prop="name">
                      <el-input
                        v-model="formValidate.watermark_x"
                        class="topIput"
                        placeholder="请输入水印横坐标偏移量"
                        style="width: 240px"
                        type="number"
                      >
                        <span slot="append">px</span>
                      </el-input>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印纵坐标偏移量：" label-width="160px" prop="mail">
                      <el-input
                        v-model="formValidate.watermark_y"
                        class="topIput"
                        placeholder="请输入水印纵坐标偏移量"
                        style="width: 240px"
                        type="number"
                      >
                        <span slot="append">px</span>
                      </el-input>
                    </el-form-item>
                  </div>
                </div>
                <!-- 水印类型为文字 -->
                <div v-else>
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印文字：" label-width="160px" prop="name">
                      <el-input v-model="formValidate.watermark_text" class="topIput" placeholder="请输入水印文字">
                      </el-input>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印文字大小：" label-width="160px">
                      <el-input
                        v-model="formValidate.watermark_text_size"
                        class="topIput"
                        placeholder="请输入水印文字大小"
                        type="number"
                      >
                      </el-input>
                    </el-form-item>
                  </div>
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印字体颜色：" label-width="160px" prop="name">
                      <el-color-picker v-model="formValidate.watermark_text_color"></el-color-picker>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印位置：" label-width="160px" prop="mail">
                      <div class="conents">
                        <div class="positionBox">
                          <div
                            v-for="(item, index) in boxs"
                            :key="index"
                            :class="positionId === item.id ? 'on' : ''"
                            class="topIput box"
                            @click="bindbox(item)"
                          ></div>
                        </div>
                        <div class="title">{{ positiontlt }}</div>
                      </div>
                    </el-form-item>
                  </div>
                  <div class="flex">
                    <el-form-item class="contentIput" label="水印字体旋转角度：" label-width="160px">
                      <el-input
                        v-model="formValidate.watermark_text_angle"
                        class="topIput"
                        placeholder="请输入水印字体旋转角度"
                        type="number"
                      >
                      </el-input>
                    </el-form-item>
                    <el-form-item class="contentIput" label="水印横坐标偏移量：" label-width="160px">
                      <el-input
                        v-model="formValidate.watermark_x"
                        class="topIput"
                        placeholder="请输入水印横坐标偏移量"
                        type="number"
                      >
                        <span slot="append">px</span>
                      </el-input>
                    </el-form-item>
                  </div>
                  <el-form-item class="contentIput" label="水印横坐纵偏移量：" label-width="160px" prop="mail">
                    <el-input
                      v-model="formValidate.watermark_y"
                      class="topIput"
                      placeholder="请输入水印横坐纵偏移量"
                      type="number"
                    >
                      <span slot="append">px</span>
                    </el-input>
                  </el-form-item>
                </div>
              </div>
            </div>
            <el-form-item>
              <el-button type="primary" @click="handleSubmit('formValidate')">保存</el-button>
            </el-form-item>
          </div>
        </el-form>
      </el-card>
    </div>
    <!-- 缩略图配置 -->
    <div v-else class="pt10">
      <el-card :bordered="false" class="ivu-mt" shadow="never">
        <el-row class="mb20">
          <el-col :span="24">
            <div class="btn-box">
              <el-button type="primary" size="mini" @click="addStorageBtn">添加存储空间</el-button>
              <!-- <el-button style="margin-left: 20px" type="success" @click="synchro">同步存储空间</el-button>
              <el-button style="float: right" @click="addConfigBtn">修改配置信息</el-button> -->
              <el-dropdown @command="handleCommand">
                <span class="iconfont icongengduo2 pointer ml10"></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="1">同步存储空间</el-dropdown-item>
                  <el-dropdown-item :command="2">修改配置信息</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </el-col>
        </el-row>
        <el-table
          ref="table"
          v-loading="loading"
          :data="levelLists"
          class="mt14"
          highlight-current-row
          no-filtered-userFrom-text="暂无筛选结果"
          no-userFrom-text="暂无数据"
        >
          <el-table-column label="储存空间名称" min-width="120">
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="区域" min-width="90">
            <template slot-scope="scope">
              <span>{{ scope.row._region }}</span>
            </template>
          </el-table-column>
          <el-table-column label="空间域名" min-width="130">
            <template slot-scope="scope">
              <span>{{ scope.row.domain }}</span>
            </template>
          </el-table-column>
          <el-table-column label="使用状态" min-width="90">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="1"
                :inactive-value="0"
                :value="scope.row.status"
                active-text="开启"
                class="defineSwitch"
                inactive-text="关闭"
                size="large"
                @change="changeSwitch(scope.row, index)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" min-width="130">
            <template slot-scope="scope">
              <span>{{ scope.row._add_time }}</span>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" min-width="130">
            <template slot-scope="scope">
              <span>{{ scope.row._update_time }}</span>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="220">
            <template slot-scope="scope">
              <template v-if="scope.row.domain && scope.row.domain != scope.row.cname">
                <span class="btn" @click="config(scope.row)">CNAME配置</span>
                <el-divider direction="vertical"></el-divider>
              </template>
              <span class="btn" @click="edit(scope.row)">修改空间域名</span>
              <el-divider direction="vertical"></el-divider>
              <span class="btn" @click="del(scope.row, '删除该数据', scope.$index)">删除</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="flex-right">
          <el-pagination
            v-if="total"
            :current-page="list.page"
            :page-size="list.limit"
            :page-sizes="[10, 15, 20]"
            :total="total"
            layout="total, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="getlist"
          />
        </div>
      </el-card>
    </div>
    <el-dialog :visible.sync="configuModal" width="550px">
      <el-descriptions :column="1" title="CNAME配置">
        <el-descriptions-item label="主机记录">{{ configData.domain }}</el-descriptions-item>
        <el-descriptions-item label="记录类型">CNAME</el-descriptions-item>
        <el-descriptions-item label="记录值" labelClassName="desc-label">
          {{ configData.cname
          }}<span :data-clipboard-text="configData.cname" class="copy copy-data" @click="insertCopy(configData.cname)"
            >复制</span
          >
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import ClipboardJS from 'clipboard'

import {
  addConfigApi,
  addStorageApi,
  storageListApi,
  storageSynchApi,
  storageSwitchApi,
  storageStatusApi,
  editStorageApi,
  positionInfoApi,
  positionPostApi,
  saveType,
  storageDelApi
} from '@/api/setting'
import Tips from '@/utils/tips'
export default {
  name: 'storages',
  data() {
    return {
      modalPic: false,
      saveType: 0,
      isChoice: '单选',
      gridBtn: {
        xl: 4,
        lg: 8,
        md: 8,
        sm: 8,
        xs: 8
      },
      gridPic: {
        xl: 6,
        lg: 8,
        md: 12,
        sm: 12,
        xs: 12
      },
      positionId: 1,
      positiontlt: '',
      formValidate: {
        thumb_big_height: '',
        thumb_big_width: '',
        thumb_mid_width: '',
        thumb_mid_height: '',
        thumb_small_height: '',
        thumb_small_width: '',
        image_watermark_status: 0,
        watermark_type: 1,
        watermark_opacity: '',
        watermark_rotate: '',
        watermark_position: 1,
        upload_type: ''
      },
      boxs: [
        { content: '左上', id: 1 },
        { content: '上', id: 2 },
        { content: '右上', id: 3 },
        { content: '左中', id: 4 },
        { content: '中', id: 5 },
        { content: '右中', id: 6 },
        { content: '左下', id: 7 },
        { content: '下', id: 8 },
        { content: '右下', id: 9 }
      ],
      upload_type: null,
      ruleValidate: {},
      configuModal: false,
      configData: '',
      headerList: [
        { label: '储存配置', value: 1 },
        { label: '七牛云储存', value: 2 },
        { label: '阿里云储存', value: 3 },
        { label: '腾讯云储存', value: '4' },
        { label: '京东云储存', value: '5' },
        { label: '华为云储存', value: '6' },
        { label: '天翼云储存', value: '7' }
        // { label: "缩略图配置", value: "10" },
      ],

      total: 0,
      list: {
        page: 1,
        limit: 15,
        type: '1'
      },
      levelLists: [],
      currentTab: '1',
      loading: false,
      addData: {
        input: '',
        select: '',
        jurisdiction: '1',
        type: '1'
      },
      confData: {
        AccessKeyId: '',
        AccessKeySecret: ''
      },
      localStorage: false
    }
  },
  async created() {
    this.changeTab()
  },
  watch: {
    upload_type(val) {
      this.formValidate.upload_type = val
    }
  },
  methods: {
    insertCopy() {
      this.$nextTick(() => {
        const clipboard = new ClipboardJS('.copy')
        clipboard.on('success', () => {
          this.$message.success('复制成功')
          clipboard.destroy()
        })
      })
    },
    async changeSave(type) {
      const res = await saveType(type)
      if (res.status == 200) {
        this.$set(this, 'upload_type', type)
      }
    },
    bindbox(item) {
      this.positionId = item.id
      this.positiontlt = item.content
      this.formValidate.watermark_position = item.id
    },
    handleSubmit(name) {
      if (this.formValidate.image_watermark_status) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.postMessage(this.formValidate)
          } else {
            this.$message.error('Fail!')
          }
        })
      } else {
        this.postMessage(this.formValidate)
      }
    },
    //保存接口
    postMessage(data) {
      positionPostApi(data)
    },
    // 选择图片
    modalPicTap() {
      this.modalPic = true
    },
    // 选中图片
    getPic(pc) {
      this.formValidate.watermark_image = pc.att_dir
      this.modalPic = false
    },
    config(row) {
      this.configuModal = true
      this.configData = row
    },
    //同步储存空间
    async synchro() {
      await storageSynchApi(this.currentTab)
      await this.getlist()
    },
    // 添加存储空间
    addStorageBtn() {
      this.$modalForm(addStorageApi(this.currentTab)).then(() => {
        this.getlist()
      })
    },
    // 修改配置信息
    addConfigBtn() {
      this.$modalForm(addConfigApi(this.currentTab)).then(() => {
        this.getlist()
      })
    },
    //修改空间域名
    edit(row) {
      this.$modalForm(editStorageApi(row.id)).then(() => {
        this.getlist()
      })
    },
    async changeSwitch(row, item) {
      await Tips.confirm({
        message: '您确认要切换使用状态吗',
        confirmButtonClass: 'btn-custom-cancel'
      })
      await storageStatusApi(row.id)
      await this.getlist()
    },
    async getlist() {
      this.loading = true
      const res = await storageListApi(this.list)
      this.total = res.data.count
      this.levelLists = res.data.list

      this.loading = false
    },
    changeTab() {
      this.list.type = this.currentTab
      this.list.page = 1
      if (parseInt(this.currentTab) === 1) {
        this.getposition()
      } else {
        this.getlist()
      }
    },
    async getposition() {
      let that = this
      const res = await positionInfoApi()
      this.formValidate = res.data
      this.formValidate.upload_type = res.data.upload_type.toString()
      this.upload_type = res.data.upload_type.toString()
      this.positionId = res.data.watermark_position
      for (var i = 0; i < this.boxs.length; i++) {
        if (this.boxs[i].id == res.data.watermark_position) {
          that.bindbox(this.boxs[i])
        }
      }
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.where.page = val
      this.getlist()
    },
    async addSwitch(e) {
      if (e) {
        this.localStorage = 1
      }
      await storageSwitchApi({ type: this.localStorage })
      await this.getlist()
    },
    // 删除
    async del(row, tit, num) {
      await Tips.confirm({ message: tit })
      await storageDelApi(row.id)
      await this.getlist()
    },
    handleCommand(command) {
      switch (command) {
        case 1:
          this.synchro()
          break
        case 2:
          this.addConfigBtn()
          break
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-card {
  border: none;
}
::v-deep .el-tabs__item {
  height: 54px !important;
  line-height: 54px !important;
}
.ivu-input-group > .ivu-input:last-child,
::v-deep .ivu-input-group-append {
  background: none;
  color: #999999;
}
::v-deep .ivu-input-group .ivu-input {
  border-right: 0px !important;
}
.content ::v-deep .ivu-form .ivu-form-item-label {
  width: 133px;
}
.topIput {
  width: 186px;
  background: #ffffff;
  border-right: 0px !important;
}
.abbreviation {
  .top {
    display: flex;
    justify-content: flex-start;
    .topBox {
      display: flex;
      .topRight {
        width: 254px;
        margin-left: 36px;
      }
      .topLeft {
        width: 94px;
        height: 94px;

        text-align: center;
        font-size: 13px;
        font-weight: 400;
        color: #000000;
        .img {
          // width: 84px;
          height: 67px;
          background: #f7fbff;
          border-radius: 4px;
          margin-bottom: 9px;
          .imgs {
            width: 70px;
            height: 51px;
            display: inline-block;
            text-align: center;
            margin-top: 8px;
          }
        }
      }
    }
  }
  .content {
    ::v-deep .ivu-form-item-label {
      width: 120px;
    }
    .flex {
      display: flex;
      justify-content: flex-start;
      // width: 400px;

      .contentIput {
        width: 400px;
      }
      .conents {
        display: flex;
        .title {
          width: 30px;
          margin-top: 70px;
          margin-left: 6px;
        }
        .positionBox {
          display: flex;
          flex-wrap: wrap;
          width: 101px;
          height: 99px;
          border-right: 1px solid #dddddd;
          .box {
            width: 33px;
            height: 33px;
            // border-radius: 4px 0px 0px 0px;
            border: 1px solid #dddddd;
            cursor: pointer;
          }
          .on {
            background: rgba(24, 144, 255, 0.1);
          }
        }
      }
    }
  }
}
</style>
<style scoped>
.message ::v-deep .ivu-table-header thead tr th {
  padding: 8px 16px;
}
.ivu-radio-wrapper {
  margin-right: 15px;
  font-size: 12px !important;
}
.message ::v-deep .ivu-tabs-tab {
  border-radius: 0 !important;
}
.table-box {
  padding: 20px;
}
.is-table {
  display: flex;
  /* justify-content: space-around; */
  justify-content: center;
}
.btn {
  cursor: pointer;
  color: #2d8cf0;
  font-size: 10px;
}
.is-switch-close {
  background-color: #504444;
}
.is-switch {
  background-color: #eb5252;
}
.notice-list {
  background-color: #308cf5;
  margin: 0 15px;
}
.table {
  padding: 0 18px;
}
.confignv {
  margin: 10px 0px;
}
.configtit {
  display: inline-block;
  width: 100px;
  text-align: right;
}
.copy {
  padding: 3px 5px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  color: #333;
  cursor: pointer;
  margin-left: 5px;
}
.copy:hover {
  border-color: #2d8cf0;
  color: #2d8cf0;
}
.picBox {
  display: inline-block;
  cursor: pointer;
}
.picBox .pictrue {
  width: 60px;
  height: 60px;
  border: 1px dotted rgba(0, 0, 0, 0.1);
  margin-right: 10px;
}

.picBox .pictrue img {
  width: 100%;
  height: 100%;
}
.picBox .upLoad {
  width: 58px;
  height: 58px;
  line-height: 58px;
  border: 1px dotted rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  background: rgba(0, 0, 0, 0.02);
}
h3 {
  margin: 5px 0 15px 0;
}
.table-box p {
  margin-bottom: 10px;
}
.save-type {
  font-size: 13px;
}
::v-deep .el-descriptions-item__container {
  display: inline-block;
}
.btn-box {
  display: flex;
  justify-content: flex-end;
}
</style>

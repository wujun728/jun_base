<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>汽车品牌</span>
          </div>
          <div class="item">
            <car @refreshSubView="refreshSubView" />
          </div>
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>[{{ carName }}]品牌名</span>
          </div>
          <div class="item">
            <car-model ref="carModel" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import car from './components/car'
import carModel from './components/carModel'

export default {
  name: 'TwoTable',
  components: { car, carModel },
  data() {
    return {
      carName: '未选择'
    }
  },
  methods: {
    refreshSubView(row) {
      console.log('row',row)
      this.carName = row.name
      this.$refs.carModel.refreshSubView(row)
    }
  }
}
</script>

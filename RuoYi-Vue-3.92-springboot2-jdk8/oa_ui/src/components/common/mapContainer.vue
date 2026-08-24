<!-- 
  @FileDescription: 高德地图组件
  功能：封装高德地图功能，支持地址搜索、标记点拖拽、范围显示等
-->
<template>
  <div id="all">
    <!-- 地址搜索区域 -->
    <div class="posInput">
      <el-input
        style="width: 350px"
        id="tipinput"
        placeholder="请输入搜索地址"
        v-model="address"
        @change="searchKeyWord"
      >
        <el-button 
          type="primary" 
          slot="append" 
          icon="el-icon-search" 
          @click="searchKeyWord"
        >
        </el-button>
      </el-input>
      
      <!-- 搜索结果列表 -->
      <div class="map_search_result" v-if="show">
        <ul>
          <li 
            v-for="(item, index) in poiList" 
            :key="index" 
            @click="markerResult(item)"
          >
            {{ item.name }}
          </li>
        </ul>
      </div>
    </div>
    
    <!-- 地图容器 -->
    <div id="allmap"></div>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader'

// 高德地图安全配置
window._AMapSecurityConfig = {
  securityJsCode: '60dcc64efdca6fb9bed8aeacbe21d2b8'
}

export default {
  name: 'MapContainer',
  props: {
    // 位置信息对象
    positionInfo: {
      type: Object,
      default: () => ({
        address: '',
        lng: 116.397428,  // 默认北京中心点经度
        lat: 39.90923,    // 默认北京中心点纬度
        effective_range: 1000
      })
    }
  },
  
  data() {
    return {
      address: '',          // 搜索地址
      marker: null,         // 地图标记点
      placeSearchComponent: null, // 地点搜索组件
      show: false,          // 是否显示搜索结果
      poiList: [],         // 搜索结果列表
      effective_range: 1000, // 有效范围(米)
      radius: 200,         // 标记点范围半径
      map: null,           // 地图实例
      geocoder: null,      // 地理编码实例
      geolocation: null    // 定位实例
    }
  },
  
  mounted() {
    this.initMap()
    this.address = this.positionInfo.address
  },
  
  watch: {
    // 监听位置信息变化
    positionInfo: {
      handler(newVal) {
        this.effective_range = newVal.effective_range
        this.address = newVal.address
      },
      deep: true
    }
  },

  methods: {
    /**
     * 初始化地图
     * @returns {Promise} 返回地图初始化Promise
     */
    initMap() {
      return new Promise((resolve, reject) => {
        AMapLoader.load({
          key: 'cf5c437b14780406af75a81b380cafac',
          version: '2.0',
          plugins: [
            'AMap.ToolBar',
            'AMap.Scale',
            'AMap.Geocoder',
            'AMap.Geolocation',
            'AMap.PlaceSearch',
            'AMap.AutoComplete',
            'AMap.CitySearch'
          ],
          resizeEnable: true
        }).then((AMap) => {
          // 创建地图实例
          this.map = new AMap.Map('allmap', {
            resizeEnable: true,
            zoom: 14,
            viewMode: '3D',
            center: [this.positionInfo.lng, this.positionInfo.lat]
          })
          
          // 添加地图控件
          this.map.addControl(new AMap.Scale())
          this.map.addControl(new AMap.ToolBar())
          
          // 初始化地理编码和搜索功能
          this.geocoder = new AMap.Geocoder({ 
            radius: 1000, 
            extensions: 'all', 
            city: '全国' 
          })
          
          this.mapSearchInit()
          this.getCurrentLocation()
          
          // 根据初始经纬度获取地址
          this.getAddressByLngLat(
            this.positionInfo.lng, 
            this.positionInfo.lat
          )
          
          resolve()
        }).catch(reject)
      })
    },

    /**
     * 根据经纬度获取地址信息
     * @param {Number} lng - 经度
     * @param {Number} lat - 纬度
     */
    getAddressByLngLat(lng, lat) {
      this.geocoder.getAddress([lng, lat], (status, result) => {
        if (status === 'complete' && result.regeocode) {
          this.address = result.regeocode.formattedAddress
        } else {
          this.$message.error('根据经纬度查询地址失败')
        }
      })
    },

    /**
     * 获取当前位置
     */
    getCurrentLocation() {
      this.geolocation = new AMap.Geolocation({
        timeout: 1000,
        enableHighAccuracy: true,
        zoomToAccuracy: true
      })
      
      this.geolocation.getCurrentPosition((status, result) => {
        if (status == 'complete') {
          this.onComplete(result)
        } else {
          this.onError(result)
        }
      })
    },

    /**
     * 定位成功回调
     * @param {Object} data - 定位数据
     */
    onComplete(data) {
      const [lng, lat] = data.position
      this.dynamicSign(lng, lat, this.radius)
    },

    /**
     * 定位失败回调
     */
    onError() {
      this.getLngLatLocation()
    },

    /**
     * 获取城市级别定位
     */
    getLngLatLocation() {
      this.geolocation.getCityInfo((status, result) => {
        if (status === 'complete') {
          this.showLocation(result.position)
        } else {
          this.$message.error('获取地址失败')
        }
      })
    },

    /**
     * 显示位置
     * @param {Object} data - 位置数据
     */
    showLocation(data) {
      this.dynamicSign(
        this.positionInfo.lng, 
        this.positionInfo.lat, 
        this.radius
      )
    },

    /**
     * 动态设置标记点
     * @param {Number} lng - 经度
     * @param {Number} lat - 纬度
     * @param {Number} radius - 范围半径
     */
    dynamicSign(lng, lat, radius) {
      // 创建标记点
      const marker = new AMap.Marker({
        position: new AMap.LngLat(lng, lat),
        draggable: true,
        cursor: 'move',
        riseOnHover: true,
        bubble: true
      })
      
      // 创建范围圆
      const circle = new AMap.Circle({
        center: new AMap.LngLat(lng, lat),
        radius: radius,
        strokeColor: '#1890ff',
        strokeOpacity: 1,
        strokeWeight: 1,
        fillColor: '#1890ff',
        fillOpacity: 0.35
      })
      
      // 清除并重新添加标记
      this.map.clearMap()
      this.map.add([marker, circle])
      marker.on('dragend', this.markerClick)
    },

    /**
     * 初始化地点搜索
     */
    mapSearchInit() {
      this.placeSearchComponent = new AMap.PlaceSearch()
    },

    /**
     * 搜索关键词
     */
    searchKeyWord() {
      this.placeSearchComponent.search(this.address, (status, result) => {
        if (status === 'complete' && result.info === 'OK') {
          this.show = true
          this.poiList = result.poiList.pois
        } else {
          this.show = false
          this.poiList = []
          this.$message.warning('没有查到结果')
        }
      })
    },

    /**
     * 标记点拖拽完成回调
     * @param {Object} e - 事件对象
     */
    markerClick(e) {
      const { lng, lat } = e.lnglat
      this.dynamicSign(lng, lat, this.radius)
      
      this.getAddressByLngLat(lng, lat)
      
      // 通知父组件位置变化
      this.$emit('select', {
        address: this.address,
        lng,
        lat,
        effective_range: this.effective_range
      })
    },

    /**
     * 选择搜索结果
     * @param {Object} data - 地点数据
     */
    markerResult(data) {
      this.show = false
      this.address = data.name
      
      // 创建新标记点
      const marker = new AMap.Marker({
        position: [Number(data.location.lng), Number(data.location.lat)],
        draggable: true,
        cursor: 'move',
        riseOnHover: true
      })
      
      this.map.clearMap()
      this.map.add(marker)
      
      // 居中显示并添加范围圆
      setTimeout(() => {
        this.map.setCenter(data.location)
        this.dynamicSign(data.location.lng, data.location.lat, 200)
      }, 50)
      
      // 通知父组件
      this.$emit('select', {
        address: this.address,
        lng: data.location.lng,
        lat: data.location.lat,
        effective_range: this.effective_range
      })
      
      marker.on('dragend', this.markerClick)
    }
  }
}
</script>

<style lang="scss" scoped>
/* 地图容器样式 */
#all {
  height: 100%;
  position: relative;
}

/* 地图元素样式 */
#allmap {
  width: 100%;
  height: 100%;
  font-family: '微软雅黑';
}

/* 搜索框样式 */
.posInput {
  position: absolute;
  z-index: 1;
  width: 80%;
  margin-top: 20px;
  margin-left: 10%;
}

/* 搜索结果列表样式 */
.map_search_result {
  cursor: pointer;
  height: 300px;
  width: 400px;
  background-color: #f5f5f5;
  opacity: 0.6;
  overflow-y: scroll;
  
  ul {
    padding-left: 0;
  }
  
  li {
    line-height: 35px;
    color: #000;
    height: 35px;
    padding-left: 15px;
    border-bottom: 1px dashed #909199;
    
    &:hover {
      color: blue;
      font-weight: bolder;
    }
  }
}

/* Element UI 样式覆盖 */
::v-deep {
  .el-form-item {
    margin-bottom: 0 !important;
  }
  
  .el-input__suffix {
    position: absolute;
    top: 10px;
  }
  
  .el-input-group__append {
    top: 0;
    
    button.el-button {
      background-color: #1890ff;
      color: #fff;
      border-radius: 0 4px 4px 0;
    }
  }
}
</style>

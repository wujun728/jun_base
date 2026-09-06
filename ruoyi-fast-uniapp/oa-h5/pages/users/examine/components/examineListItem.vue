<template>
  <view>
    <uni-row class="item-list-content" v-for="(item,index) in content" :key="'itemlist'+index">
      <uni-col :span="6" class="left line1">{{item.label}}</uni-col>
      <uni-col v-if="!Array.isArray(item.value)" :span="18">{{item.value||'--'}}</uni-col>
      <uni-col v-else :span="18" class="examine-from-right">
        <upload-from-list v-if="item.value && item.value.length > 0" :upload-from-data="item.value"></upload-from-list>
        <text v-else>--</text>
      </uni-col>
    </uni-row>
  </view>
</template>

<script setup>
  import { reactive, toRefs, watch } from "vue"
  import moment from 'moment'
  import uploadFromList from './uploadFromList.vue'
  moment.suppressDeprecationWarnings = true
  const props = defineProps({
    content: {
      type: Object,
      default: () => {
        return []
      }
    }
  })
  const { content } = toRefs(props)
  const data = reactive({
    listData: []
  })


  const getContentValue = (content) => {
    let arr = []
    let len = 3
    // let index = content.findIndex( item => item.content.type === 'timeFrom' )
    // if ( index > -1 ) {
    // 	const res = content[ index ]
    // 	let time1 = res.value.dateStart
    // 	let time2 = res.value.dateEnd
    // 	if ( res.value.timeType === 'day' ) {
    // 		time1 = moment( time1 ).format( 'YYYY/MM/DD' ) + ( res.value.timeStart == 0 ? ' 下午' : ' 上午' )
    // 		time2 = moment( time2 ).format( 'YYYY/MM/DD' ) + ( res.value.timeEnd == 0 ? ' 下午' : ' 上午' )
    // 	} else {
    // 		time1 = moment( time1 ).format( 'YYYY/MM/DD HH:mm' )
    // 		time2 = moment( time2 ).format( 'YYYY/MM/DD HH:mm' )
    // 	}
    // 	arr = [
    // 		{ title: '开始时间', content: time1 },
    // 		{ title: '结束时间', content: time2 },
    // 		{ title: `${res.content.props.titleIpt}(${res.value.timeType === 'day' ? '天' : '小时'})`, content: res.value.duration },
    // 	]
    // 	for ( let i = 0; i < content.length; i++ ) {
    // 		let contents = content[ i ]
    // 		if ( contents.content.type === 'radio' || contents.content.type === 'select' ) {
    // 			arr.push( {
    // 				title: contents.content.title,
    // 				content: (contents.value && contents.value.abnormal_id) ? contents.value.abnormal_id.label : (contents.value && contents.value.record_id) ? contents.value.record_id.label : (contents.value && contents.value.holiday_id) ? contents.value.holiday_id.label : getRadioValue( contents.value, contents.content.options )
    // 			} )
    // 		}
    // 	}
    // } else {
    // 	for ( let i = 0; i < content.length; i++ ) {
    // 		let contents = content[ i ]
    // 		if ( contents.content.type === 'input' || contents.content.type === 'inputNumber' || contents.content.type === 'moneyFrom' ) {
    // 			arr.push( {
    // 				title: contents.content.title,
    // 				content: contents.value
    // 			} )
    // 		} else if ( contents.content.type === 'radio' || contents.content.type === 'select' ) {
    // 			arr.push( {
    // 				title: contents.content.title,
    // 				content: (contents.value && contents.value.abnormal_id) ? contents.value.abnormal_id.label : (contents.value && contents.value.record_id) ? contents.value.record_id.label : (contents.value && contents.value.holiday_id) ? contents.value.holiday_id.label : getRadioValue( contents.value, contents.content.options )
    // 			} )
    // 		} else if ( contents.content.type === 'checkbox' ) {
    // 			arr.push( {
    // 				title: contents.content.title,
    // 				content: contents.value.join('，')
    // 			} )
    // 		}

    // 		if ( arr.length >= len ) {
    // 			break;
    // 		}
    // 	}
    // }
    data.listData = arr
  }

  const getRadioValue = (value, option) => {
    let str = '';
    if (option) {
      for (let i = 0; i < option.length; i++) {
        if (value == option[i].value) {
          str = option[i].label;
          break;
        }
      }
      return str;
    }
  }

  watch(
    () => content,
    (newvalue, oldalue) => {
      getContentValue(newvalue.value)
    }, { deep: true, immediate: true },
  )
</script>

<style scoped lang="scss">
  .item-list-content {
    padding-bottom: 14rpx;
    font-size: 28rpx;
    color: $uni-text-color;
    font-weight: 400;
    display: flex;
    align-items: center;

    .left {
      color: $nui-text-color-four;
    }
  }

  image {
    width: 100rpx;
    height: 100rpx;
  }
</style>
<!-- 视频播放器组件：https://h5player.bytedance.com/-->
<template>
  <div :id="playerId" />
</template>

<script setup lang="ts">
  import Player from 'xgplayer'
  import FlvPlugin from 'xgplayer-flv'
  import 'xgplayer/dist/index.min.css'

  defineOptions({ name: 'ArtVideoPlayer' })

  interface Props {
    /** 播放器容器 ID */
    playerId: string
    /** 视频源URL */
    videoUrl: string
    /** 视频封面图URL */
    posterUrl: string
    /** 是否自动播放 */
    autoplay?: boolean
    /** 音量大小(0-1) */
    volume?: number
    /** 可选的播放速率 */
    playbackRates?: number[]
    /** 是否循环播放 */
    loop?: boolean
    /** 是否静音 */
    muted?: boolean
    /** 视频类型：mp4 | flv */
    videoType?: 'mp4' | 'flv'
    /** 是否为直播流（FLV 直播模式） */
    isLive?: boolean
    commonStyle?: VideoPlayerStyle
  }

  const props = withDefaults(defineProps<Props>(), {
    playerId: '',
    videoUrl: '',
    posterUrl: '',
    autoplay: false,
    volume: 1,
    loop: false,
    muted: false,
    videoType: 'mp4',
    isLive: false
  })

  // 播放器实例引用
  const playerInstance = ref<Player | null>(null)

  // 播放器样式接口定义
  interface VideoPlayerStyle {
    progressColor?: string // 进度条背景色
    playedColor?: string // 已播放部分颜色
    cachedColor?: string // 缓存部分颜色
    sliderBtnStyle?: Record<string, string> // 滑块按钮样式
    volumeColor?: string // 音量控制器颜色
  }

  // 默认样式配置
  const defaultStyle: VideoPlayerStyle = {
    progressColor: 'rgba(255, 255, 255, 0.3)',
    playedColor: '#00AEED',
    cachedColor: 'rgba(255, 255, 255, 0.6)',
    sliderBtnStyle: {
      width: '10px',
      height: '10px',
      backgroundColor: '#00AEED'
    },
    volumeColor: '#00AEED'
  }

  // 组件挂载时初始化播放器
  onMounted(() => {
    const isFlv = props.videoType === 'flv'

    playerInstance.value = new Player({
      id: props.playerId,
      lang: 'zh',
      volume: props.volume,
      autoplay: props.autoplay,
      screenShot: true,
      url: props.videoUrl,
      poster: props.posterUrl,
      fluid: true,
      playbackRate: props.playbackRates,
      loop: props.loop,
      muted: props.muted,
      isLive: isFlv ? props.isLive : undefined,
      plugins: isFlv ? [FlvPlugin] : [],
      flv: isFlv
        ? {
            loadTimeout: 10000,
            preloadTime: 4,
            retryCount: 3,
            retryDelay: 1000
          }
        : undefined,
      commonStyle: {
        ...defaultStyle,
        ...props.commonStyle
      }
    })

    playerInstance.value.on('play', () => {
      console.log('Video is playing')
    })

    playerInstance.value.on('pause', () => {
      console.log('Video is paused')
    })

    playerInstance.value.on('error', (error) => {
      console.error('Error occurred:', error)
    })
  })

  // 组件卸载前清理播放器实例
  onBeforeUnmount(() => {
    if (playerInstance.value) {
      playerInstance.value.destroy()
    }
  })
</script>

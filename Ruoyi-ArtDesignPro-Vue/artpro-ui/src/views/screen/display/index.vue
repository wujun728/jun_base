<template>
  <main class="screen-viewport">
    <section class="dashboard-board" :class="{ 'warning-pulse': warningPulse }" :data-theme="theme" :style="boardStyle">
      <div class="board-scan"></div>
      <div v-if="warningPulse" class="siri-flash" aria-hidden="true"></div>
      <header class="board-header">
        <div class="header-company">
          <span>云南锡业股份有限公司大屯锡矿</span>
        </div>
        <h1>溜井数字化监控管理平台</h1>
        <div class="header-tools">
          <div class="header-status">
            <span>{{ dateText }}</span>
            <b>{{ timeText }}</b>
            <em>{{ weekText }}</em>
            <i class="weather" title="个旧市大屯镇天气">{{ weatherText }}</i>
          </div>
          <div ref="themeSelectRef" class="theme-select" :class="{ open: themeMenuOpen }" @keydown.esc.stop="themeMenuOpen = false">
            <button
              class="theme-select-trigger"
              type="button"
              data-testid="scheme-select"
              aria-haspopup="listbox"
              :aria-expanded="themeMenuOpen ? 'true' : 'false'"
              title="主题切换"
              @click="themeMenuOpen = !themeMenuOpen"
            >
              <b>{{ themeLabel }}</b>
              <i></i>
            </button>
            <div v-if="themeMenuOpen" class="theme-options" role="listbox">
              <button
                v-for="option in themeOptions"
                :key="option.value"
                class="theme-option"
                :class="{ active: option.value === theme }"
                type="button"
                role="option"
                :aria-selected="option.value === theme ? 'true' : 'false'"
                @click="chooseTheme(option.value)"
              >
                <span>{{ option.label }}</span>
                <em>{{ option.value === theme ? '当前' : '切换' }}</em>
              </button>
            </div>
          </div>
        </div>
      </header>

      <div class="board-body">
        <aside class="left-stack">
          <section class="panel alarm-panel">
            <PanelTitle title="报警统计" :extra="`${state.warningSummary.total} 次`" />
            <div class="alarm-summary">
              <button class="alarm-badge" @click="handleWarningBadge('全部')">
                <span class="alarm-icon"><i></i><b>警</b></span>
                <em>今日报警</em>
                <strong>{{ state.warningSummary.total }}</strong>
                <small>次</small>
              </button>
              <button class="mini-ring warn" @click="handleWarningBadge('未处理')">
                <i :style="ringStyle(state.warningSummary.wcl, 'var(--warn)')"></i>
                <span>未处理</span>
                <b>{{ state.warningSummary.wcl }}</b>
              </button>
              <button class="mini-ring ok" @click="handleWarningBadge('已处理')">
                <i :style="ringStyle(state.warningSummary.cl, 'var(--ok)')"></i>
                <span>已处理</span>
                <b>{{ state.warningSummary.cl }}</b>
              </button>
            </div>

            <div class="alarm-table">
              <div class="alarm-table-head">
                <span>报警类型</span>
                <span>位置</span>
                <span>报警时间</span>
                <span>处理</span>
              </div>
              <div ref="alarmTableBodyRef" class="alarm-table-body" @mouseenter="alarmScrollPaused = true" @mouseleave="alarmScrollPaused = false">
                <button
                  v-for="item in visibleWarnings"
                  :key="`${item.type}-${item.name}-${item.warning_time}`"
                  class="alarm-row"
                  @click="openWarningDetail(item)"
                >
                  <span>{{ warningTypeText(item) }}</span>
                  <span>{{ item.name }}</span>
                  <span>{{ shortDateTime(item.warning_time) }}</span>
                  <b :class="isWarningHandled(item) ? 'done' : 'pending'">
                    {{ isWarningHandled(item) ? '已处理' : '去处理' }}
                  </b>
                </button>
              </div>
            </div>
          </section>

          <section class="panel device-panel">
            <PanelTitle title="设备运行状态" :extra="`设备在线率 ${deviceOnlineRate}`" />
            <div class="device-list">
              <button v-for="device in state.devices" :key="device.name" class="device-line" @click="openDeviceDetail(device)">
                <span class="device-emblem">
                  <ArtSvgIcon v-if="device.name.includes('摄像')" icon="ri:video-on-line" />
                  <ArtSvgIcon v-else-if="device.name.includes('RFID')" icon="ri:radio-2-line" />
                  <ArtSvgIcon v-else-if="device.name.includes('报警')" icon="ri:alarm-warning-line" />
                  <ArtSvgIcon v-else-if="device.name.includes('硬盘') || device.name.includes('录像')" icon="ri:hard-drive-3-line" />
                </span>
                <strong>{{ device.name }}</strong>
                <em>总数</em>
                <b>{{ device.total }}</b>
                <em>正常</em>
                <b class="ok-text">{{ device.online }}</b>
                <em>故障</em>
                <b class="danger-text">{{ device.offline }}</b>
              </button>
            </div>
          </section>

          <section class="panel car-panel">
            <PanelTitle title="矿车统计" :extra="`车辆工作率 ${carWorkRate}`" />
            <div class="car-dashboard">
              <div class="car-stats-row">
                <button class="car-stat-card running-card" @click="openCarDetail('运输中', state.cars.online)">
                  <strong>{{ state.cars.online }}</strong>
                  <span>运输中（辆）</span>
                </button>
                <div class="car-total-ring" @click="openCarDetail('总数', state.cars.total)">
                  <div class="ring-decor ring-left"></div>
                  <div class="ring-decor ring-right"></div>
                  <div class="ring-content">
                    <strong>{{ state.cars.total }}</strong>
                    <span>总数（辆）</span>
                  </div>
                </div>
                <button class="car-stat-card idle-card" @click="openCarDetail('空闲中', state.cars.offline)">
                  <strong>{{ state.cars.offline }}</strong>
                  <span>空闲中（辆）</span>
                </button>
              </div>
            </div>
          </section>
        </aside>

        <section class="center-stack">
          <section class="panel video-panel">
            <div class="panel-title video-panel-title">
              <div class="title-controls-left">
                <span>实时视频监控</span>
                <div class="video-group-tabs">
                  <button
                    v-for="group in videoGroups"
                    :key="group.label"
                    :class="{ active: activeCameraGroupLabel === group.label }"
                    @click="selectCameraGroup(group.label)"
                  >
                    {{ group.label }}
                  </button>
                </div>
              </div>
              <div class="title-controls-right">
                <button
                  class="video-cycle-toggle video-mode-toggle"
                  :class="{ active: videoDisplayMode === 'focus' }"
                  @click="toggleVideoDisplayMode"
                  :aria-pressed="videoDisplayMode === 'focus' ? 'true' : 'false'"
                  :title="videoDisplayMode === 'focus' ? '切换为四宫格模式' : '切换为主屏小屏模式'"
                >
                  <span class="video-cycle-text">{{ videoDisplayMode === 'focus' ? '主屏' : '四宫格' }}</span>
                  <span class="video-mode-mark" aria-hidden="true"></span>
                </button>
                <strong>{{ currentCamera?.label || '未选择' }}</strong>
              </div>
            </div>
            <div v-if="!useFocusLayout" class="video-content">
      <button
        v-if="videoPageTotal > 1"
        class="video-page-button prev"
        :disabled="videoPage <= 1"
        @click="changeVideoPage(videoPage - 1)"
      >
        ‹
      </button>
      <div class="video-grid" :class="`count-${videoGridCount}`">
        <div
          v-for="camera in gridCameras"
          :key="camera.label"
          class="video-tile"
          :class="{ active: camera.label === activeCameraLabel }"
          role="button"
          tabindex="0"
          @click="selectCamera(camera.label)"
          @dblclick="openVideoDialog(camera)"
          @keydown.enter.prevent="selectCamera(camera.label)"
          @keydown.space.prevent="selectCamera(camera.label)"
        >
          <iframe
            v-if="isPageVisible && camera.url && camera.url !== 'about:blank'"
            :src="camera.url"
            loading="lazy"
            referrerpolicy="no-referrer"
          ></iframe>
          <div v-else class="mine-video-fallback">
            <div class="fallback-time">{{ dateText }} {{ timeText }}</div>
            <div class="rail-scene">
              <span class="rail-one"></span>
              <span class="rail-two"></span>
              <span class="ore-car"></span>
            </div>
            <strong>{{ camera.label }}</strong>
          </div>
          <div class="video-caption">
            <button
              class="siren-talk-button thumb-talk-button"
              :disabled="!sirenIpForLabel(camera.label)"
              :title="sirenIpForLabel(camera.label) ? `喊话设备：${sirenIpForLabel(camera.label)}` : '当前画面未配置喊话设备'"
              @click.stop="openSirenDialog(camera)"
            >
              <i></i>
              <em>喊话</em>
            </button>
          </div>
        </div>
      </div>
      <button
        v-if="videoPageTotal > 1"
        class="video-page-button next"
        :disabled="videoPage >= videoPageTotal"
        @click="changeVideoPage(videoPage + 1)"
      >
        ›
      </button>
      <div v-if="videoPageTotal > 1" class="video-page-dots">
        <button
          v-for="page in videoPageTotal"
          :key="page"
          :class="{ active: page === videoPage }"
          @click="changeVideoPage(page)"
        ></button>
      </div>
    </div>
            <div v-else class="video-content video-focus-layout">
              <div
                class="main-video"
                role="button"
                tabindex="0"
                @click="selectCamera(currentCamera.label)"
                @dblclick="openVideoDialog(currentCamera)"
                @keydown.enter.prevent="selectCamera(currentCamera.label)"
                @keydown.space.prevent="selectCamera(currentCamera.label)"
              >
                <iframe
                  v-if="isPageVisible && currentCamera?.url && currentCamera.url !== 'about:blank'"
                  :src="currentCamera.url"
                  loading="lazy"
                  referrerpolicy="no-referrer"
                ></iframe>
                <div v-else class="mine-video-fallback">
                  <div class="fallback-time">{{ dateText }} {{ timeText }}</div>
                  <div class="rail-scene">
                    <span class="rail-one"></span>
                    <span class="rail-two"></span>
                    <span class="ore-car"></span>
                  </div>
                  <strong>{{ currentCamera?.label || '未选择' }}</strong>
                </div>
                <div class="video-caption">
                  <button
                    class="siren-talk-button"
                    :disabled="!sirenIpForLabel(currentCamera?.label || '')"
                    :title="sirenIpForLabel(currentCamera?.label || '') ? `喊话设备：${sirenIpForLabel(currentCamera?.label || '')}` : '当前画面未配置喊话设备'"
                    @click.stop="openSirenDialog(currentCamera)"
                  >
                    <i></i>
                    <em>喊话</em>
                  </button>
                </div>
              </div>
              <div ref="videoThumbsRef" class="video-thumbs">
                <div
                  v-for="camera in cameraThumbs"
                  :key="camera.label"
                  :ref="element => setThumbRef(camera.label, element)"
                  class="video-thumb"
                  :class="{ active: camera.label === activeCameraLabel }"
                  role="button"
                  tabindex="0"
                  @click="selectCamera(camera.label)"
                  @dblclick="selectCamera(camera.label)"
                  @keydown.enter.prevent="selectCamera(camera.label)"
                  @keydown.space.prevent="selectCamera(camera.label)"
                >
                  <iframe
                    v-if="shouldPlayThumb(camera)"
                    :src="camera.url"
                    loading="lazy"
                    referrerpolicy="no-referrer"
                  ></iframe>
                  <div v-else class="mine-video-fallback">
                    <strong>{{ camera.label }}</strong>
                  </div>
                  <div class="video-caption">
                    <button
                      class="siren-talk-button thumb-talk-button"
                      :disabled="!sirenIpForLabel(camera.label)"
                      :title="sirenIpForLabel(camera.label) ? `喊话设备：${sirenIpForLabel(camera.label)}` : '当前画面未配置喊话设备'"
                      @click.stop="openSirenDialog(camera)"
                    >
                      <i></i>
                      <em>喊话</em>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <section class="panel material-panel">
            <div class="panel-title material-panel-title">
              <div class="title-controls-left">
                <span>溜井料位监测</span>
                <div class="segmented">
                  <button :class="{ active: materialType === 'ai' }" @click="setMaterialType('ai')">AI统计</button>
                  <button :class="{ active: materialType === 'ld' }" @click="setMaterialType('ld')">雷达统计</button>
                </div>
              </div>
              <div class="title-controls-right">
                <div
                  ref="materialDeptSelectRef"
                  class="dept-select"
                  :class="{ open: materialDeptMenuOpen }"
                  @keydown.esc.stop="materialDeptMenuOpen = false"
                >
                  <span class="select-label">{{ selectedDeptLabel }}</span>
                  <span class="select-arrow"></span>
                  <button class="select-hit" type="button" title="部门筛选" @click="materialDeptMenuOpen = !materialDeptMenuOpen"></button>
                  <div v-if="materialDeptMenuOpen" class="select-options" role="listbox">
                    <button
                      v-for="option in materialDeptOptions"
                      :key="option.value"
                      class="select-option"
                      :class="{ active: option.value === selectedDept }"
                      type="button"
                      role="option"
                      :aria-selected="option.value === selectedDept ? 'true' : 'false'"
                      @click="chooseMaterialDept(option.value)"
                    >
                      <span>{{ option.label }}</span>
                      <em>{{ option.value === selectedDept ? '当前' : '筛选' }}</em>
                    </button>
                  </div>
                </div>
                <strong>数量：{{ filteredMaterials.length }}</strong>
              </div>
            </div>
            <div class="silo-scroll-container" v-if="filteredMaterials.length > 5">
              <div class="silo-scroll-wrapper" ref="siloScrollRef">
                <div class="silo-row">
                  <button
                    v-for="item in filteredMaterials"
                    :key="`${item.lname}-${item.shitype}`"
                    class="silo-card"
                  :class="{ offline: String(item.status) !== '0', 'radar-mode': materialType === 'ld' }"
                    @click="openMaterialDetail(item)"
                  >
                    <span class="ore-ton">矿石：{{ numberText(item.ton) }}吨</span>
                    <div class="silo-scale">
                      <em v-for="scale in getScales(item.all_quantity)" :key="scale">{{ scale }}</em>
                    </div>
                    <div
                      class="silo-tube"
                      :class="{ filled: hasOre(item), dumping: shouldDumpOre(item) }"
                      :style="{ '--level': `${levelPercent(item)}%` }"
                    >
                      <span class="ore-fall" aria-hidden="true"></span>
                      <span class="ore-particles" aria-hidden="true">
                        <em v-for="dot in 7" :key="dot"></em>
                      </span>
                      <span class="ore-impact" aria-hidden="true"></span>
                      <i :style="{ height: `${levelPercent(item)}%` }"></i>
                      <b>{{ numberText(item.now_quantity) }}米</b>
                    </div>
                    <strong>{{ item.lname }}</strong>
                    <small>{{ item.shitype }}</small>
                  </button>
                </div>
              </div>
            </div>
            <div class="silo-row" v-else>
              <button
                v-for="item in filteredMaterials"
                :key="`${item.lname}-${item.shitype}`"
                class="silo-card"
                :class="{ offline: String(item.status) !== '0', 'radar-mode': materialType === 'ld' }"
                @click="openMaterialDetail(item)"
              >
                <span class="ore-ton">矿石：{{ numberText(item.ton) }}吨</span>
                <div class="silo-scale">
                  <em v-for="scale in getScales(item.all_quantity)" :key="scale">{{ scale }}</em>
                </div>
                <div
                  class="silo-tube"
                  :class="{ filled: hasOre(item), dumping: shouldDumpOre(item) }"
                  :style="{ '--level': `${levelPercent(item)}%` }"
                >
                  <span class="ore-fall" aria-hidden="true"></span>
                  <span class="ore-particles" aria-hidden="true">
                    <em v-for="dot in 7" :key="dot"></em>
                  </span>
                  <span class="ore-impact" aria-hidden="true"></span>
                  <i :style="{ height: `${levelPercent(item)}%` }"></i>
                  <b>{{ numberText(item.now_quantity) }}米</b>
                </div>
                <strong>{{ item.lname }}</strong>
                <small>{{ item.shitype }}</small>
              </button>
            </div>
          </section>
        </section>

        <aside class="right-stack">
          <section class="panel chart-panel">
            <PanelTitle title="年度出矿量（吨）" :extra="`累计 ${numberText(state.bar.total)}`" />
            <div ref="barChartRef" class="bar-chart"></div>
          </section>

          <section class="panel io-panel">
            <PanelTitle title="今日出入料情况（吨）" :extra="lastUpdated" />
            <div class="io-cards">
              <button @click="openIoDialog('入料')">
                <span>入料</span>
                <em>总计</em>
                <strong>{{ numberText(state.logs.total.rTotal) }}</strong>
                <b>车次 {{ state.logs.total.rCars }}</b>
              </button>
              <button @click="openIoDialog('出料')">
                <span>出料</span>
                <em>总计</em>
                <strong>{{ numberText(state.logs.total.cTotal) }}</strong>
                <b>车次 {{ state.logs.total.cCars }}</b>
              </button>
            </div>
            <div class="well-list">
              <button v-for="well in visibleWells" :key="well.name" @click="openWellDetail(well)">
                <strong>{{ well.name }}</strong>
                <span>入料(吨)<b>{{ numberText(well.rTotal) }}</b></span>
                <span>出料(吨)<b>{{ numberText(well.cTotal) }}</b></span>
              </button>
            </div>
          </section>
        </aside>
      </div>

      <transition name="drawer">
        <aside v-if="detail" class="detail-drawer" data-testid="detail-drawer">
          <div class="detail-head">
            <span>{{ detail.subtitle }}</span>
            <button @click="detail = null">关闭</button>
          </div>
          <h2>{{ detail.title }}</h2>
          <div v-if="detail.showImage" class="detail-image">
            <img v-if="detail.imageUrl" :src="detail.imageUrl" :alt="detail.imageLabel || detail.title">
            <div v-else class="detail-image-placeholder">
              <span>IMG</span>
              <strong>{{ detail.imageLabel || '报警图片' }}</strong>
              <em>暂无抓拍图片</em>
            </div>
          </div>
          <div class="detail-kv">
            <div v-for="row in detail.rows" :key="row.label">
              <span>{{ row.label }}</span>
              <strong>{{ row.value }}</strong>
            </div>
          </div>
          <div class="detail-actions">
            <button @click="detail = null">返回大屏</button>
            <button
              v-if="detail.warning"
              class="handle-button"
              :disabled="isWarningHandled(detail.warning)"
              @click="openHandleDialog(detail.warning)"
            >
              {{ isWarningHandled(detail.warning) ? '已处理' : '去处理' }}
            </button>
            <button v-if="detail.action" @click="detail.action()">{{ detail.actionText || '下钻' }}</button>
          </div>
        </aside>
      </transition>

      <transition name="handle-modal">
        <div v-if="handleDialog" class="handle-mask">
          <section class="handle-window">
            <div class="handle-window-head">
              <span>报警处理</span>
              <button @click="closeHandleDialog">关闭</button>
            </div>
            <h2>{{ warningTypeText(handleDialog.warning) }}</h2>
            <div class="handle-image">
              <img
                v-if="warningImageUrl(handleDialog.warning)"
                :src="warningImageUrl(handleDialog.warning)"
                :alt="warningTypeText(handleDialog.warning)"
              >
              <div v-else class="handle-image-placeholder">
                <span>IMG</span>
                <strong>报警图片</strong>
                <em>暂无抓拍图片</em>
              </div>
            </div>
            <div class="handle-form">
              <label>
                <span>处理内容</span>
                <textarea v-model="handleDialog.handleText" required @input="refreshHandlePayload"></textarea>
              </label>
              <div>
                <span>报警位置</span>
                <strong>{{ handleDialog.warning.name || '-' }}</strong>
              </div>
              <div>
                <span>处理时间</span>
                <strong>{{ handleDialog.handleTime }}</strong>
              </div>
            </div>
            <p v-if="handleDialog.error" class="handle-error">{{ handleDialog.error }}</p>
            <div class="handle-actions">
              <button @click="closeHandleDialog">取消</button>
              <button :disabled="handleDialog.loading" @click="submitHandleDialog">
                {{ handleDialog.loading ? '发送中...' : '确认发送' }}
              </button>
            </div>
          </section>
        </div>
      </transition>

      <transition name="handle-modal">
        <div v-if="checkHeightDialog" class="handle-mask">
          <section class="handle-window">
            <div class="handle-window-head">
              <span>校验高度</span>
              <button @click="closeCheckHeightDialog">关闭</button>
            </div>
            <h2>{{ checkHeightDialog.lname }}</h2>
            <div class="handle-form">
              <label>
                <span>容量上限</span>
                <strong>{{ checkHeightDialog.currentHeight }} 米</strong>
              </label>
              <label>
                <span>新高度</span>
                <input
                  type="number"
                  step="0.01"
                  v-model.number="checkHeightDialog.height"
                  placeholder="请输入新高度"
                  required
                >
              </label>
            </div>
            <p v-if="checkHeightDialog.error" class="handle-error">{{ checkHeightDialog.error }}</p>
            <p v-if="checkHeightDialog.message" class="handle-message">{{ checkHeightDialog.message }}</p>
            <div class="handle-actions">
              <button @click="closeCheckHeightDialog">取消</button>
              <button :disabled="checkHeightDialog.loading" @click="submitCheckHeight">
                {{ checkHeightDialog.loading ? '提交中...' : '确认提交' }}
              </button>
            </div>
          </section>
        </div>
      </transition>

      <transition name="handle-modal">
        <div v-if="ioDialog" class="handle-mask">
          <section class="io-window">
            <div class="io-window-head">
              <div>
                <!-- <span>出入料明细</span> -->
                <h2>{{ ioDialog.type }}记录</h2>
              </div>
              <button @click="closeIoDialog">关闭</button>
            </div>

            <div class="io-table" :class="ioDialog.type === '出料' ? 'out-table' : 'in-table'">
              <!-- 出料记录表格 -->
              <template v-if="ioDialog.type === '出料'">
                <div class="io-table-head">
                  <span>班次</span>
                  <span>溜井点</span>
                  <span>矿种</span>
                  <span>班组</span>
                  <span>车号</span>
                  <span>运输工</span>
                  <span>车数</span>
                  <span>放矿工</span>
                  <span>放矿时间</span>
                  <span>合计吨数</span>
                  <span>称重时间</span>
                </div>
                <div class="io-table-body">
                  <div v-for="record in ioDialog.records" :key="record.id" class="io-table-row">
                    <strong>{{ record.loadRate }}</strong>
                    <span>{{ record.wellName }}</span>
                    <span>{{ record.oreType }}</span>
                    <span>{{ record.section }}</span>
                    <span>{{ record.carNo }}</span>
                    <span>{{ record.transitSeconds }}</span>
                    <span>{{ record.carCount }}</span>
                    <span>{{ record.oreName }}</span>
                    <span>{{ record.bigs }}</span>
                    <b>{{ numberText(record.ton) }}</b>
                    <span>{{ record.unloadTime }}</span>
                  </div>
                  <div v-if="!ioDialog.records.length" class="io-empty">暂无数据</div>
                </div>
              </template>
              <!-- 入料记录表格 -->
              <template v-else>
                <div class="io-table-head">
                  <span>溜井名称</span>
                  <span>所属中段</span>
                  <span>矿种名称</span>
                  <span>运输车辆</span>
                  <span>大块</span>
                  <span>异物</span>
                  <span>实际入料重量（吨）</span>
                  <span>过程视频</span>
                  <span>装载图片</span>
                  <span>装载率（%）</span>
                  <span>运图时间（秒）</span>
                  <span>卸矿时间</span>
                </div>
                <div class="io-table-body">
                  <div v-for="record in ioDialog.records" :key="record.id" class="io-table-row">
                    <strong>{{ record.wellName }}</strong>
                    <span>{{ record.section }}</span>
                    <span>{{ record.oreName }}</span>
                    <span>{{ record.carNo }}</span>
                    <span>{{ record.bigs }}</span>
                    <span>{{ record.foreignMatter }}</span>
                    <b>{{ numberText(record.ton) }}</b>
                    <button
                      class="io-media-button video"
                      :disabled="!record.videoPath"
                      @click="openIoMedia(record, 'video')"
                    >
                      <i></i>
                      <em>查看</em>
                    </button>
                    <button
                      class="io-media-button image"
                      :disabled="!record.imagePath"
                      @click="openIoMedia(record, 'image')"
                    >
                      <i></i>
                      <em>查看</em>
                    </button>
                    <span>{{ record.loadRate }}</span>
                    <span>{{ record.transitSeconds }}</span>
                    <span>{{ record.unloadTime }}</span>
                  </div>
                  <div v-if="!ioDialog.records.length" class="io-empty">暂无数据</div>
                </div>
              </template>
            </div>

            <div class="io-pagination">
              <span>共 {{ ioDialog.total }} 条</span>
              <button :disabled="ioDialog.page <= 1" @click="changeIoPage(ioDialog.page - 1)">上一页</button>
              <b>{{ ioDialog.page }} / {{ ioTotalPages }}</b>
              <button :disabled="ioDialog.page >= ioTotalPages" @click="changeIoPage(ioDialog.page + 1)">下一页</button>
            </div>
          </section>
        </div>
      </transition>

      <transition name="handle-modal">
        <div v-if="ioMediaDialog" class="handle-mask" @click="closeIoMedia">
          <section class="io-media-window" @click.stop>
            <div class="io-window-head">
              <div>
                <span>{{ ioMediaDialog.kind === 'video' ? '过程视频' : '装载图片' }}</span>
                <h2>{{ ioMediaDialog.title }}</h2>
              </div>
              <button @click="closeIoMedia">关闭</button>
            </div>
            <div class="io-media-body" :class="ioMediaDialog.kind">
              <video
                v-if="ioMediaDialog.kind === 'video'"
                :src="ioMediaDialog.url"
                controls
                autoplay
                muted
                playsinline
              ></video>
              <img v-else :src="ioMediaDialog.url" :alt="ioMediaDialog.title" />
            </div>
            <div class="io-media-meta">
              <span>{{ ioMediaDialog.record.wellName }}</span>
              <b>{{ ioMediaDialog.record.carNo }}</b>
              <em>{{ ioMediaDialog.record.unloadTime }}</em>
            </div>
          </section>
        </div>
      </transition>

      <transition name="handle-modal">
        <div v-if="videoDialog" class="handle-mask" @click="closeVideoDialog">
          <section class="video-window" @click.stop>
            <div class="video-window-head">
              <strong>{{ videoDialog.label }}</strong>
              <button
                class="siren-talk-button video-dialog-siren"
                :disabled="!sirenIpForLabel(videoDialog.label)"
                :title="sirenIpForLabel(videoDialog.label) ? `喊话设备：${sirenIpForLabel(videoDialog.label)}` : '当前画面未配置喊话设备'"
                @click.stop="openSirenDialog(videoDialog)"
              >
                <i></i>
                <em>喊话</em>
              </button>
              <button @click="closeVideoDialog">关闭</button>
            </div>
            <div class="video-window-body">
              <iframe
                v-if="isPageVisible && videoDialog.url && videoDialog.url !== 'about:blank'"
                :src="videoDialog.url"
                loading="lazy"
                referrerpolicy="no-referrer"
              ></iframe>
              <div v-else class="mine-video-fallback">
                <div class="fallback-time">{{ dateText }} {{ timeText }}</div>
                <div class="rail-scene">
                  <span class="rail-one"></span>
                  <span class="rail-two"></span>
                  <span class="ore-car"></span>
                </div>
                <strong>{{ videoDialog.label }}</strong>
              </div>
            </div>
          </section>
        </div>
      </transition>

      <transition name="handle-modal">
        <div v-if="sirenDialog" class="handle-mask" @click="closeSirenDialog">
          <section class="siren-window" @click.stop>
            <div class="handle-window-head">
              <span>声光喊话</span>
              <button @click="closeSirenDialog">关闭</button>
            </div>
            <div class="siren-target">
              <strong>{{ sirenDialog.label }}</strong>
              <span>{{ sirenDialog.ip }}</span>
            </div>
            <label class="siren-field">
              <span>喊话内容</span>
              <textarea v-model="sirenDialog.text" maxlength="120" placeholder="请输入喊话内容"></textarea>
            </label>
            <label class="siren-field siren-num-field">
              <span>播放次数</span>
              <input v-model.number="sirenDialog.num" type="number" min="1" max="10">
            </label>
            <p v-if="sirenDialog.message" class="siren-message">{{ sirenDialog.message }}</p>
            <p v-if="sirenDialog.error" class="handle-error">{{ sirenDialog.error }}</p>
            <div class="handle-actions">
              <button @click="closeSirenDialog">取消</button>
              <button :disabled="sirenDialog.loading" @click="submitSirenDialog">
                {{ sirenDialog.loading ? '发送中...' : '发送喊话' }}
              </button>
            </div>
          </section>
        </div>
      </transition>

      <transition name="warning-toast">
        <aside v-if="warningToast" :key="warningKey(warningToast)" class="warning-toast" @click="openWarningToastDetail">
          <div class="warning-toast-head">
            <span>新报警</span>
            <button @click.stop="dismissWarningToast">关闭</button>
          </div>
          <strong>{{ warningTypeText(warningToast) }}</strong>
          <div class="warning-toast-row">
            <span>位置</span>
            <b>{{ warningToast.name || '-' }}</b>
          </div>
          <div class="warning-toast-row">
            <span>时间</span>
            <b>{{ shortDateTime(warningToast.warning_time) }}</b>
          </div>
          <em>点击查看详情</em>
        </aside>
      </transition>

      <transition name="warning-toast">
        <aside v-if="deviceToast" :key="`device-${deviceToast.name}-${deviceToast.offline}`" class="warning-toast device-toast" @click="openDeviceToastDetail">
          <div class="warning-toast-head">
            <span>设备离线</span>
            <button @click.stop="dismissDeviceToast">关闭</button>
          </div>
          <strong>{{ deviceToast.name }}</strong>
          <div class="warning-toast-row">
            <span>正常</span>
            <b>{{ deviceToast.online }}</b>
          </div>
          <div class="warning-toast-row">
            <span>故障</span>
            <b>{{ deviceToast.offline }}</b>
          </div>
          <em>点击查看详情</em>
        </aside>
      </transition>
    </section>
  </main>
</template>

<script setup lang="ts">
import { computed, defineComponent, h, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import { useDpData } from './composables/useDpData'
import { dpApi, putJson } from './services/api'
import type { CameraItem, DeviceStatus, MaterialLevel, OutLogItem, WarningItem, WellLog } from './services/types'
import ArtSvgIcon from '@/components/core/base/art-svg-icon/index.vue'

type ThemeName = 'command' | 'hologram' | 'green-mine'
type VideoDisplayMode = 'grid' | 'focus'
type EChartsInstance = ReturnType<typeof echarts.init>
type DetailRow = { label: string; value: string | number }
type IoType = '入料' | '出料'
type IoRecord = {
  id: string
  wellName: string
  section: string
  oreType: string
  oreName: string
  carNo: string
  bigs: string
  foreignMatter: string
  carCount: string
  ton: number
  videoPath: string
  imagePath: string
  loadRate: string
  transitSeconds: string
  unloadTime: string
}
type IoDialogState = {
  type: IoType
  page: number
  pageSize: number
  total: number
  records: IoRecord[]
  loading: boolean
}
type IoMediaDialogState = {
  kind: 'video' | 'image'
  title: string
  url: string
  record: IoRecord
}
type DetailState = {
  title: string
  subtitle: string
  rows: DetailRow[]
  warning?: WarningItem
  showImage?: boolean
  imageUrl?: string
  imageLabel?: string
  actionText?: string
  action?: () => void
}
type HandleDialogState = {
  warning: WarningItem
  route: string
  url: string
  payload: Record<string, unknown>
  handleText: string
  handleTime: string
  loading: boolean
  message: string
  error: string
}
type SirenDialogState = {
  label: string
  ip: string
  text: string
  num: number
  loading: boolean
  message: string
  error: string
}
type CheckHeightDialogState = {
  id: string | number
  lname: string
  currentHeight: number
  height: string
  loading: boolean
  message: string
  error: string
}

declare global {
  interface Window {
    __dpTestWarning?: () => void
    __dpTestDeviceOffline?: () => void
  }
}

const PanelTitle = defineComponent({
  props: {
    title: { type: String, required: true },
    extra: { type: String, default: '' },
    unit: { type: String, default: '' }
  },
  setup(props) {
    return () => h('div', { class: 'panel-title' }, [
      h('span', props.title),
      props.unit ? h('em', props.unit) : null,
      props.extra ? h('strong', props.extra) : null
    ])
  }
})

const {
  loading,
  lastUpdated,
  warningType,
  materialType,
  selectedDept,
  selectedDeptLabel,
  wsTick,
  state,
  cameras,
  refreshAll,
  refreshAlertState,
  refreshWarnings,
  refreshMaterials,
  openWarningStream
} = useDpData()

const theme = ref<ThemeName>('command')
const themeNames: Record<ThemeName, string> = {
  command: '矿脉钛金',
  hologram: '琉璃晶蓝',
  'green-mine': '氧铜石绿'
}
const themeOptions = Object.entries(themeNames).map(([value, label]) => ({ value: value as ThemeName, label }))
const themeMenuOpen = ref(false)
const themeSelectRef = ref<HTMLDivElement | null>(null)
const materialDeptMenuOpen = ref(false)
const materialDeptSelectRef = ref<HTMLDivElement | null>(null)
const activeCameraLabel = ref('')
const activeCameraGroupLabel = ref('三坑')
const autoCycleEnabled = ref(true)
const videoDisplayMode = ref<VideoDisplayMode>('grid')
const videoPage = ref(1)
const now = ref(new Date())
const isPageVisible = ref(document.visibilityState !== 'hidden')
const barChartRef = ref<HTMLDivElement | null>(null)
const alarmTableBodyRef = ref<HTMLDivElement | null>(null)
const videoThumbsRef = ref<HTMLDivElement | null>(null)
const siloScrollRef = ref<HTMLDivElement | null>(null)
const boardScaleX = ref(1)
const boardScaleY = ref(1)
const detail = ref<DetailState | null>(null)
const handleDialog = ref<HandleDialogState | null>(null)
const ioDialog = ref<IoDialogState | null>(null)
const ioMediaDialog = ref<IoMediaDialogState | null>(null)
const videoDialog = ref<CameraItem | null>(null)
const sirenDialog = ref<SirenDialogState | null>(null)
const checkHeightDialog = ref<CheckHeightDialogState | null>(null)
const warningPulse = ref(false)
const warningToast = ref<WarningItem | null>(null)
const deviceToast = ref<DeviceStatus | null>(null)
const weatherText = ref('☁ 18~24°C')
const alarmScrollPaused = ref(false)
const visibleThumbLabels = ref(new Set<string>())
const knownWarningKeys = new Set<string>()
const knownDeviceOffline = new Map<string, number>()
const thumbElements = new Map<string, HTMLElement>()
let barChart: EChartsInstance | null = null
let thumbObserver: IntersectionObserver | null = null
let clockTimer = 0
let refreshTimer = 0
let alertPollTimer = 0
let noticeRepeatTimer = 0
let alarmScrollLastTime = 0
let alarmScrollHoldUntil = 0
let alarmScrollPosition = 0
let weatherTimer = 0
let groupCycleTimer = 0
let alarmScrollDirection = 1
let warningPulseTimer = 0
let warningToastTimer = 0
let deviceToastTimer = 0
let warningMonitorReady = false
let deviceMonitorReady = false
let knownWarningTotal = 0
let knownWarningPending = 0
let lastWarningNoticeKey = ''
let lastWarningNoticeAt = 0
let lastDeviceNoticeKey = ''
let lastDeviceNoticeAt = 0
let userSelectedCameraGroup = false
let groupCycleGroupIndex = 0
let renderBarChartTimer = 0
let lastBarChartData = ''
let lastTheme = ''
let resizeTimer = 0
const HANDLE_API_BASE = (import.meta.env.VITE_DP_HANDLE_API_BASE_URL || import.meta.env.VITE_HANDLE_API_BASE_URL || 'http://192.168.246.136').replace(/\/$/, '')
const MEDIA_BASE = (import.meta.env.VITE_DP_MEDIA_BASE_URL || import.meta.env.VITE_MEDIA_BASE_URL || import.meta.env.VITE_DP_API_TARGET || import.meta.env.VITE_API_TARGET || HANDLE_API_BASE || '').replace(/\/$/, '')
const DEFAULT_CAMERA_GROUP_LABEL = '三坑'
const DATUN_WEATHER_LATITUDE = 23.44
const DATUN_WEATHER_LONGITUDE = 103.2
const FULL_REFRESH_INTERVAL = Number(import.meta.env.VITE_FULL_REFRESH_INTERVAL || 60000)
const ALERT_POLL_INTERVAL = Number(import.meta.env.VITE_ALERT_POLL_INTERVAL || 45000)
const NOTICE_REPEAT_INTERVAL = Number(import.meta.env.VITE_NOTICE_REPEAT_INTERVAL || 45000)
const NOTICE_TOAST_DURATION = 3 * 60 * 1000
const ALARM_SCROLL_SPEED = Number(import.meta.env.VITE_ALARM_SCROLL_SPEED || 18)
const VIDEO_GROUP_CYCLE_INTERVAL = Number(import.meta.env.VITE_VIDEO_GROUP_CYCLE_INTERVAL || 300000)
const BAR_VISIBLE_MONTHS = Number(import.meta.env.VITE_BAR_VISIBLE_MONTHS || 6)
const IO_PAGE_SIZE = Number(import.meta.env.VITE_IO_PAGE_SIZE || 8)
const MAX_WARNING_DISPLAY = 30
const SIREN_IP_RULES = [
  { keywords: ['1920', '挂车'], ip: '192.168.18.124' },
  { keywords: ['61号', '61#', '61'], ip: '192.168.18.118' },
  { keywords: ['54号', '54#', '54'], ip: '192.168.18.117' },
  { keywords: ['53号', '53#', '53'], ip: '192.168.18.116' },
  { keywords: ['4号', '4#'], ip: '192.168.18.115' },
  { keywords: ['5号', '5#'], ip: '192.168.18.114' }
]

const boardStyle = computed(() => ({
  transform: `scale(${boardScaleX.value}, ${boardScaleY.value})`
}))

const fallbackCamera = { label: '61号溜井入料口', url: 'about:blank' }

const deviceOnlineRate = computed(() => {
  const totals = state.devices.reduce((acc, item) => {
    acc.total += Number(item.total || 0)
    acc.online += Number(item.online || 0)
    return acc
  }, { total: 0, online: 0 })
  return formatRate(totals.online, totals.total)
})

const carWorkRate = computed(() => formatRate(state.cars.online, state.cars.total))
const visibleWarnings = computed(() => state.warnings.slice(0, MAX_WARNING_DISPLAY))

const videoGroups = computed(() => {
  const groups = state.live.filter(group => (group.children || []).length)
  return groups.length ? groups : [{ label: '一坑', children: cameras.value.length ? cameras.value : [fallbackCamera] }]
})
const themeLabel = computed(() => themeNames[theme.value])
const materialDeptOptions = computed(() => [
  { value: '0', label: '总览' },
  ...state.selectOptions.map(item => ({ value: item.value, label: item.lable }))
])
const activeCameraGroup = computed(() => videoGroups.value.find(group => group.label === activeCameraGroupLabel.value) || videoGroups.value[0])
const currentGroupCameras = computed(() => activeCameraGroup.value?.children?.length ? activeCameraGroup.value.children : [fallbackCamera])
const currentCamera = computed(() => currentGroupCameras.value.find(camera => camera.label === activeCameraLabel.value) || currentGroupCameras.value[0])
const videoPageTotal = computed(() => Math.max(1, Math.ceil((currentGroupCameras.value.length || 1) / 4)))
const currentVideoPageCameras = computed(() => {
  const list = currentGroupCameras.value.length ? currentGroupCameras.value : [fallbackCamera]
  const safePage = Math.min(Math.max(videoPage.value, 1), videoPageTotal.value)
  const start = (safePage - 1) * 4
  return list.slice(start, start + 4)
})
const videoGridCount = computed(() => Math.min(Math.max(currentVideoPageCameras.value.length || 1, 1), 4))
const gridCameras = computed(() => {
  const list = currentVideoPageCameras.value.length ? currentVideoPageCameras.value : [fallbackCamera]
  const visibleCount = videoGridCount.value
  const visibleList = list.slice(0, visibleCount)
  const placeholders = Array.from({ length: Math.max(0, visibleCount - visibleList.length) }, (_, index) => ({
    label: `备用画面${index + 1}`,
    url: 'about:blank'
  }))
  return [...visibleList, ...placeholders]
})
const cameraThumbs = computed(() => {
  const list = currentGroupCameras.value.length ? currentGroupCameras.value : [fallbackCamera]
  const active = currentCamera.value?.label
  const inactive = list.filter(item => item.label !== active)
  return inactive.length ? inactive : list
})
const cycleVideoGroups = computed(() => {
  const preferredOrder = ['一坑', '二坑', '三坑']
  const available = videoGroups.value.filter(group => (group.children || []).length)
  const ordered = preferredOrder
    .map(label => available.find(group => group.label === label))
    .filter((group): group is typeof available[number] => Boolean(group))
  return ordered.length ? ordered : available
})
const dateText = computed(() => formatDate(now.value))
const timeText = computed(() => now.value.toLocaleTimeString('zh-CN', { hour12: false }))
const weekText = computed(() => now.value.toLocaleDateString('zh-CN', { weekday: 'long' }))
const warningTypeLabel = computed(() => warningType.value === '全部' ? '今日报警' : `${warningType.value}`)
const filteredMaterials = computed(() => {
  let result = state.materials.filter(item => !String(item.lname || '').includes('34号'))
  if (selectedDept.value !== '0') {
    result = result.filter(item => String(item.dept || '') === selectedDept.value)
  }
  return result
})
const visibleWells = computed(() => state.logs.detail.filter(item => !String(item.name || '').includes('34号')))
const ioTotalPages = computed(() => Math.max(1, Math.ceil((ioDialog.value?.total || 0) / (ioDialog.value?.pageSize || IO_PAGE_SIZE))))
const useFocusLayout = computed(() => videoDisplayMode.value === 'focus' && currentGroupCameras.value.length >= 4)

function formatDate(value: Date) {
  const year = value.getFullYear()
  const month = `${value.getMonth() + 1}`.padStart(2, '0')
  const day = `${value.getDate()}`.padStart(2, '0')
  return `${year}年${month}月${day}日`
}

function numberText(value: number | string | undefined) {
  const numberValue = Number(value || 0)
  return Number.isFinite(numberValue) ? numberValue.toFixed(2).replace(/\.00$/, '') : '0'
}

function shortDateTime(value: string) {
  return String(value || '').replace(/^\d{4}-/, '').replace(' ', '\n')
}

function weatherCodeText(code: number) {
  if ([0, 1].includes(code)) return '晴'
  if ([2, 3].includes(code)) return '多云'
  if ([45, 48].includes(code)) return '雾'
  if ([51, 53, 55, 56, 57].includes(code)) return '小雨'
  if ([61, 63, 65, 66, 67, 80, 81, 82].includes(code)) return '雨'
  if ([71, 73, 75, 77, 85, 86].includes(code)) return '雪'
  if ([95, 96, 99].includes(code)) return '雷雨'
  return '天气'
}

async function fetchJsonWithTimeout<T>(url: string, timeout = 7000): Promise<T> {
  const controller = new AbortController()
  const timer = window.setTimeout(() => controller.abort(), timeout)

  try {
    const response = await fetch(url, { signal: controller.signal })
    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    return await response.json() as T
  } finally {
    window.clearTimeout(timer)
  }
}

async function refreshDatunWeather() {
  try {
    const weather = await fetchJsonWithTimeout<{
      current?: { temperature_2m?: number; weather_code?: number }
      daily?: { temperature_2m_min?: number[]; temperature_2m_max?: number[] }
    }>(
      `https://api.open-meteo.com/v1/forecast?latitude=${DATUN_WEATHER_LATITUDE}&longitude=${DATUN_WEATHER_LONGITUDE}&current=temperature_2m,weather_code&daily=temperature_2m_min,temperature_2m_max&forecast_days=1&timezone=Asia%2FShanghai`
    )

    const currentTemp = Number(weather.current?.temperature_2m || 0)
    const min = Number(weather.daily?.temperature_2m_min?.[0] ?? currentTemp)
    const max = Number(weather.daily?.temperature_2m_max?.[0] ?? currentTemp)
    const code = Number(weather.current?.weather_code ?? -1)
    weatherText.value = `☁ ${weatherCodeText(code)} ${Math.round(min)}~${Math.round(max)}°C`
  } catch {
    weatherText.value = '☁ 18~24°C'
  }
}

function warningTypeText(item: WarningItem) {
  return String(item.type || '').trim() || '车辆长时间滞留'
}

function warningImageUrl(item: WarningItem) {
  return String(
    item.imageUrl ||
    item.image_url ||
    item.img ||
    item.imgUrl ||
    item.img_url ||
    item.image ||
    item.pic ||
    item.pic_url ||
    item.picture ||
    item.picture_url ||
    item.warning_img ||
    item.warningImg ||
    item.snapshot ||
    ''
  ).trim()
}

function warningKey(item: WarningItem) {
  return [item.id || '', warningTypeText(item), item.name || '', item.warning_time || '', item.roter || ''].join('|')
}

function isWarningHandled(item: WarningItem) {
  const stateText = String(item.state ?? '').trim()
  const statusText = String(item.status ?? '').trim()
  const handleText = String(item.handle || '').trim()
  const hasHandleResult = Boolean(handleText) && !['-', '未处理', '待处理', 'null', 'undefined'].includes(handleText)
  const stateHandled = Boolean(stateText) && !['0', '未处理', '待处理'].includes(stateText)
  return stateHandled || ['1', '已处理'].includes(statusText) || hasHandleResult
}

function warningHandleResult(item: WarningItem) {
  const handleText = String(item.handle || '').trim()
  return handleText && !['null', 'undefined'].includes(handleText) ? handleText : (isWarningHandled(item) ? '已处理' : '未处理')
}

function rememberCurrentWarnings() {
  state.warnings.forEach(item => knownWarningKeys.add(warningKey(item)))
}

function rememberWarningSummary() {
  knownWarningTotal = Number(state.warningSummary.total || 0)
  knownWarningPending = Number(state.warningSummary.wcl || 0)
}

function rememberCurrentDevices() {
  state.devices.forEach(item => knownDeviceOffline.set(item.name, Number(item.offline || 0)))
}

function triggerScreenPulse() {
  warningPulse.value = false
  window.clearTimeout(warningPulseTimer)

  window.requestAnimationFrame(() => {
    warningPulse.value = true
  })

  warningPulseTimer = window.setTimeout(() => {
    warningPulse.value = false
  }, 6800)
}

function triggerWarningNotice(item: WarningItem) {
  const key = warningKey(item)
  const nowTime = Date.now()
  if (key === lastWarningNoticeKey && nowTime - lastWarningNoticeAt < 1200) return
  lastWarningNoticeKey = key
  lastWarningNoticeAt = nowTime
  deviceToast.value = null
  window.clearTimeout(deviceToastTimer)
  warningToast.value = item
  window.clearTimeout(warningToastTimer)
  triggerScreenPulse()

  warningToastTimer = window.setTimeout(() => {
    warningToast.value = null
  }, NOTICE_TOAST_DURATION)
}

function triggerDeviceNotice(item: DeviceStatus) {
  if (warningToast.value) return
  const key = `${item.name}|${item.offline}`
  const nowTime = Date.now()
  if (key === lastDeviceNoticeKey && nowTime - lastDeviceNoticeAt < 1200) return
  lastDeviceNoticeKey = key
  lastDeviceNoticeAt = nowTime
  warningToast.value = null
  window.clearTimeout(warningToastTimer)
  deviceToast.value = item
  window.clearTimeout(deviceToastTimer)
  triggerScreenPulse()

  deviceToastTimer = window.setTimeout(() => {
    deviceToast.value = null
  }, NOTICE_TOAST_DURATION)
}

function dismissWarningToast() {
  warningToast.value = null
  window.clearTimeout(warningToastTimer)
}

function dismissDeviceToast() {
  deviceToast.value = null
  window.clearTimeout(deviceToastTimer)
}

function openWarningToastDetail() {
  if (!warningToast.value) return
  const item = warningToast.value
  dismissWarningToast()
  openWarningDetail(item)
}

function openDeviceToastDetail() {
  if (!deviceToast.value) return
  const item = deviceToast.value
  dismissDeviceToast()
  openDeviceDetail(item)
}

function formatDateTime(value = new Date()) {
  const year = value.getFullYear()
  const month = `${value.getMonth() + 1}`.padStart(2, '0')
  const day = `${value.getDate()}`.padStart(2, '0')
  const hour = `${value.getHours()}`.padStart(2, '0')
  const minute = `${value.getMinutes()}`.padStart(2, '0')
  const second = `${value.getSeconds()}`.padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

function warningRoute(item: WarningItem) {
  const route = String(item.roter || item.router || item.route || '1')
  return ['1', '2', '3'].includes(route) ? route : '1'
}

function warningHandleUrl(route: string) {
  if (route === '2') return `${HANDLE_API_BASE}/api/ynUser`
  if (route === '3') return `${HANDLE_API_BASE}/api/ynUserXj`
  return `${HANDLE_API_BASE}/api/alert`
}

function warningHandlePayload(item: WarningItem, route: string, handle: string, handleTime: string) {
  if (route === '1') {
    return {
      id: item.id || '',
      state: 1,
      handleTime,
      handle
    }
  }

  return {
    id: item.id || '',
    status: '1',
    handle,
    handleTime
  }
}

function buildHandleDialogState(item: WarningItem, handleText = '已处理', handleTime = formatDateTime()) {
  const route = warningRoute(item)
  const url = warningHandleUrl(route)
  return {
    warning: item,
    route,
    url,
    payload: warningHandlePayload(item, route, handleText, handleTime),
    handleText,
    handleTime,
    loading: false,
    message: '',
    error: ''
  }
}

function openHandleDialog(item: WarningItem) {
  handleDialog.value = buildHandleDialogState(item)
}

function refreshHandlePayload() {
  if (!handleDialog.value) return
  const current = handleDialog.value
  const next = buildHandleDialogState(current.warning, current.handleText, current.handleTime)
  handleDialog.value = {
    ...current,
    route: next.route,
    url: next.url,
    payload: next.payload
  }
}

function closeHandleDialog() {
  handleDialog.value = null
}

async function submitHandleDialog() {
  if (!handleDialog.value) return
  const current = handleDialog.value
  const handleText = current.handleText.trim()
  if (!handleText) {
    handleDialog.value = { ...current, error: '处理内容不能为空' }
    return
  }

  const handleTime = formatDateTime()
  const next = buildHandleDialogState(current.warning, handleText, handleTime)
  handleDialog.value = { ...next, loading: true, message: '', error: '' }

  try {
    await putJson(next.url, next.payload)
    current.warning.state = 1
    current.warning.status = next.route === '1' ? 1 : '1'
    current.warning.handle = next.handleText
    current.warning.handleTime = handleTime
    handleDialog.value = null
    void refreshAlertState()
  } catch (error) {
    handleDialog.value = {
      ...next,
      loading: false,
      message: '',
      error: `处理失败：${error instanceof Error ? error.message : String(error)}`
    }
  }
}

function latestWarningNotice() {
  return state.warnings[0] || {
    type: '新报警',
    name: '-',
    warning_time: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    state: 0,
    roter: `notice-${Date.now()}`
  }
}

function checkWarningSummaryNotice() {
  const total = Number(state.warningSummary.total || 0)
  const pending = Number(state.warningSummary.wcl || 0)
  const increased = total > knownWarningTotal || pending > knownWarningPending
  rememberWarningSummary()
  if (warningMonitorReady && increased) triggerWarningNotice(latestWarningNotice())
}

function hasPendingWarning() {
  return Number(state.warningSummary.wcl || 0) > 0 || state.warnings.some(item => String(item.state) === '0')
}

function deviceIconClass(name: string) {
  if (name.includes('摄像')) return 'camera'
  if (name.includes('RFID')) return 'rfid'
  if (name.includes('报警')) return 'speaker'
  if (name.includes('硬盘') || name.includes('录像')) return 'disk'
  return 'device'
}

function firstOfflineDevice() {
  return state.devices.find(item => Number(item.offline || 0) > 0)
}

function repeatActiveNotices() {
  if (!isPageVisible.value) return
  const offlineDevice = firstOfflineDevice()
  if (deviceMonitorReady && offlineDevice) {
    triggerDeviceNotice(offlineDevice)
  }
}

function runAlarmAutoScroll() {
  if (!isPageVisible.value || alarmScrollPaused.value) return

  const element = alarmTableBodyRef.value
  if (!element) return

  const scrollHeight = element.scrollHeight
  const clientHeight = element.clientHeight

  if (scrollHeight <= clientHeight + 2) {
    alarmScrollPosition = 0
    return
  }

  const now = Date.now()
  if (now < alarmScrollHoldUntil) return

  const maxScrollTop = scrollHeight - clientHeight

  // 确保位置在有效范围内
  if (alarmScrollPosition >= maxScrollTop) {
    alarmScrollDirection = -1
    alarmScrollPosition = maxScrollTop
    element.scrollTop = alarmScrollPosition
    alarmScrollHoldUntil = now + 2500
    accumulatedScroll = 0
  } else if (alarmScrollPosition <= 0) {
    alarmScrollDirection = 1
    alarmScrollPosition = 0
    element.scrollTop = alarmScrollPosition
    alarmScrollHoldUntil = now + 2500
    accumulatedScroll = 0
  } else {
    // 直接应用计算好的位置
    element.scrollTop = alarmScrollPosition
  }
}

let alarmScrollRafId = 0
let lastScrollUpdateTime = 0
const SCROLL_UPDATE_INTERVAL = 16 // 约60fps，流畅滚动
let accumulatedScroll = 0

function startAlarmScroll() {
  if (alarmScrollRafId) return

  function tick(timestamp: number) {
    if (!lastScrollUpdateTime) lastScrollUpdateTime = timestamp
    const deltaTime = timestamp - lastScrollUpdateTime

    // 根据时间差计算应该滚动的距离，确保不同设备上速度一致
    if (deltaTime >= SCROLL_UPDATE_INTERVAL) {
      // 约 30 像素/秒的滚动速度
      accumulatedScroll += (deltaTime * 0.03)

      // 当积累的滚动量 >= 1 时才实际更新滚动位置
      if (accumulatedScroll >= 1) {
        const scrollAmount = Math.floor(accumulatedScroll)
        alarmScrollPosition += scrollAmount * alarmScrollDirection
        accumulatedScroll -= scrollAmount

        // 确保只在需要时才调用 runAlarmAutoScroll
        runAlarmAutoScroll()
      }

      lastScrollUpdateTime = timestamp
    }

    alarmScrollRafId = requestAnimationFrame(tick)
  }

  alarmScrollRafId = requestAnimationFrame(tick)
}

function stopAlarmScroll() {
  if (alarmScrollRafId) {
    cancelAnimationFrame(alarmScrollRafId)
    alarmScrollRafId = 0
  }
  lastScrollUpdateTime = 0
  accumulatedScroll = 0
}

async function pollAlertState() {
  if (!isPageVisible.value) return
  await refreshAlertState()
  lastUpdated.value = new Date().toLocaleTimeString('zh-CN', { hour12: false })
}

function pushTestWarning() {
  const warning: WarningItem = {
    type: '车辆长时间滞留',
    name: '高峰山4号',
    warning_time: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-'),
    state: 0,
    roter: `test-${Date.now()}`
  }

  state.warnings = [warning, ...state.warnings]
  state.warningSummary.total += 1
  state.warningSummary.wcl += 1
}

function pushTestDeviceOffline() {
  const fallback: DeviceStatus = { name: '摄像头', total: 1, online: 1, offline: 0 }
  const index = state.devices.findIndex(item => item.name === '摄像头')
  const current = index >= 0 ? state.devices[index] : state.devices[0] || fallback
  const next: DeviceStatus = {
    ...current,
    online: Math.max(0, Number(current.online || 0) - 1),
    offline: Number(current.offline || 0) + 1
  }

  if (index >= 0) {
    state.devices.splice(index, 1, next)
  } else if (state.devices.length) {
    state.devices.splice(0, 1, next)
  } else {
    state.devices = [next]
  }
}

function switchTheme(nextTheme: ThemeName) {
  theme.value = nextTheme
  detail.value = null
  lastBarChartData = ''
  nextTick(renderBarChart)
}

function chooseTheme(nextTheme: ThemeName) {
  themeMenuOpen.value = false
  switchTheme(nextTheme)
}

function chooseMaterialDept(nextDept: string) {
  materialDeptMenuOpen.value = false
  selectedDept.value = nextDept
  void refreshMaterials()
}

function handleHeaderSelectDocumentClick(event: MouseEvent) {
  const target = event.target
  if (!(target instanceof Node)) {
    themeMenuOpen.value = false
    materialDeptMenuOpen.value = false
    return
  }
  if (!themeSelectRef.value?.contains(target)) themeMenuOpen.value = false
  if (!materialDeptSelectRef.value?.contains(target)) materialDeptMenuOpen.value = false
}

function setWarningType(type: string) {
  warningType.value = type
  void refreshWarnings()
}

function handleWarningBadge(type: string) {
  setWarningType(type)
  detail.value = {
    title: type === '全部' ? '今日报警总数' : `报警筛选：${type}`,
    subtitle: '报警统计下钻',
    rows: [
      { label: '今日报警总数', value: `${state.warningSummary.total} 次` },
      { label: '未处理', value: state.warningSummary.wcl },
      { label: '已处理', value: state.warningSummary.cl }
    ]
  }
}

function setMaterialType(type: 'ai' | 'ld') {
  materialType.value = type
  void refreshMaterials()
}

function applyCameraGroup(label: string, markUser = false) {
  if (markUser) userSelectedCameraGroup = true
  activeCameraGroupLabel.value = label
  const group = videoGroups.value.find(item => item.label === label)
  if (!group?.children?.length) return
  const hasActive = group.children.some(camera => camera.label === activeCameraLabel.value)
  if (!hasActive) activeCameraLabel.value = group.children[0].label
}

function resetGroupCycleCursorFromCurrent() {
  const groups = cycleVideoGroups.value
  if (!groups.length) {
    groupCycleGroupIndex = 0
    return
  }

  const groupIndex = groups.findIndex(group => group.label === activeCameraGroupLabel.value)
  groupCycleGroupIndex = groupIndex >= 0 ? (groupIndex + 1) % groups.length : 0
}

function stepGroupCycle() {
  const groups = cycleVideoGroups.value
  if (!groups.length) return

  if (groupCycleGroupIndex >= groups.length || groupCycleGroupIndex < 0) groupCycleGroupIndex = 0
  const group = groups[groupCycleGroupIndex]
  applyCameraGroup(group.label, false)
  groupCycleGroupIndex = (groupCycleGroupIndex + 1) % groups.length
}

function stopAutoCycle() {
  window.clearInterval(groupCycleTimer)
  groupCycleTimer = 0
}

function startAutoCycle() {
  stopAutoCycle()
  if (!autoCycleEnabled.value || !isPageVisible.value) return
  resetGroupCycleCursorFromCurrent()
  groupCycleTimer = window.setInterval(() => {
    if (!isPageVisible.value) return
    stepGroupCycle()
  }, VIDEO_GROUP_CYCLE_INTERVAL)
}

function toggleAutoCycle() {
  autoCycleEnabled.value = !autoCycleEnabled.value
  if (autoCycleEnabled.value) {
    startAutoCycle()
  } else {
    stopAutoCycle()
  }
}

function toggleVideoDisplayMode() {
  videoDisplayMode.value = videoDisplayMode.value === 'grid' ? 'focus' : 'grid'
  if (videoDisplayMode.value === 'focus') {
    videoPage.value = 1
    nextTick(initThumbObserver)
  } else {
    visibleThumbLabels.value = new Set()
  }
}

function toggleToFocusMode() {
  if (videoDisplayMode.value === 'grid') {
    videoDisplayMode.value = 'focus'
    videoPage.value = 1
    nextTick(initThumbObserver)
  }
}

function toggleToGridMode() {
  if (videoDisplayMode.value === 'focus') {
    videoDisplayMode.value = 'grid'
    visibleThumbLabels.value = new Set()
  }
}

function selectCamera(label: string) {
  activeCameraLabel.value = label
  if (autoCycleEnabled.value) resetGroupCycleCursorFromCurrent()
}

function sirenIpForLabel(label: string) {
  const normalized = String(label || '')
  return SIREN_IP_RULES.find(rule => rule.keywords.some(keyword => normalized.includes(keyword)))?.ip || ''
}

function openSirenDialog(camera?: CameraItem) {
  const label = camera?.label || currentCamera.value?.label || activeCameraLabel.value || '当前画面'
  const ip = sirenIpForLabel(label)
  if (!ip) return
  sirenDialog.value = {
    label,
    ip,
    text: '你好',
    num: 1,
    loading: false,
    message: '',
    error: ''
  }
}

function closeSirenDialog() {
  sirenDialog.value = null
}

async function submitSirenDialog() {
  if (!sirenDialog.value) return
  const current = sirenDialog.value
  const text = current.text.trim()
  const num = Math.max(1, Math.min(10, Math.round(Number(current.num || 1))))

  if (!text) {
    sirenDialog.value = { ...current, error: '喊话内容不能为空', message: '' }
    return
  }

  sirenDialog.value = { ...current, text, num, loading: true, error: '', message: '' }

  try {
    await dpApi.sirenMessage({ ip: current.ip, text, num })
    sirenDialog.value = {
      ...current,
      text,
      num,
      loading: false,
      error: '',
      message: '喊话指令已发送'
    }
  } catch (error) {
    sirenDialog.value = {
      ...current,
      text,
      num,
      loading: false,
      message: '',
      error: `发送失败：${error instanceof Error ? error.message : String(error)}`
    }
  }
}

function openVideoDialog(camera: CameraItem) {
  activeCameraLabel.value = camera.label
  videoDialog.value = camera
  if (autoCycleEnabled.value) resetGroupCycleCursorFromCurrent()
}

function closeVideoDialog() {
  videoDialog.value = null
}

function changeVideoPage(page: number) {
  videoPage.value = Math.max(1, Math.min(page, videoPageTotal.value))
  const firstCamera = currentVideoPageCameras.value[0]
  if (firstCamera) activeCameraLabel.value = firstCamera.label
  if (autoCycleEnabled.value) resetGroupCycleCursorFromCurrent()
}

function selectCameraGroup(label: string) {
  applyCameraGroup(label, true)
  videoPage.value = 1
  if (autoCycleEnabled.value) resetGroupCycleCursorFromCurrent()
}

function playPrevCamera() {
  const list = currentGroupCameras.value
  if (!list.length) return
  const index = list.findIndex(item => item.label === activeCameraLabel.value)
  const nextIndex = index <= 0 ? list.length - 1 : index - 1
  activeCameraLabel.value = list[nextIndex].label
}

function playNextCamera() {
  const list = currentGroupCameras.value
  if (!list.length) return
  const index = list.findIndex(item => item.label === activeCameraLabel.value)
  const nextIndex = index < 0 ? 0 : (index + 1) % list.length
  activeCameraLabel.value = list[nextIndex].label
}

function ringStyle(value: number, color: string) {
  const normalized = Math.max(0, Math.min(Number(value || 0), 100))
  return { background: `conic-gradient(${color} ${normalized * 3.6}deg, rgba(255,255,255,.12) 0deg)` }
}

function levelPercent(item: MaterialLevel) {
  if (!Number(item.all_quantity)) return 0
  return Math.max(0, Math.min((Number(item.now_quantity) / Number(item.all_quantity)) * 100, 100))
}

function hasOre(item: MaterialLevel) {
  return Number(item.now_quantity || 0) > 0
}

function shouldDumpOre(item: MaterialLevel) {
  return hasOre(item) && Number(item.now_quantity || 0) <= 20
}

function shouldPlayThumb(camera: CameraItem) {
  return isPageVisible.value && videoDisplayMode.value === 'focus' && visibleThumbLabels.value.has(camera.label) && Boolean(camera.url && camera.url !== 'about:blank')
}

function setThumbRef(label: string, element: unknown) {
  const previous = thumbElements.get(label)
  if (previous && previous !== element) {
    thumbObserver?.unobserve(previous)
    thumbElements.delete(label)
  }

  if (element instanceof HTMLElement) {
    element.dataset.thumbLabel = label
    thumbElements.set(label, element)
    thumbObserver?.observe(element)
  } else {
    const next = new Set(visibleThumbLabels.value)
    next.delete(label)
    visibleThumbLabels.value = next
  }
}

function initThumbObserver() {
  thumbObserver?.disconnect()

  if (!videoThumbsRef.value || !('IntersectionObserver' in window)) {
    visibleThumbLabels.value = new Set(cameraThumbs.value.map(item => item.label))
    return
  }

  thumbObserver = new IntersectionObserver(entries => {
    const next = new Set(visibleThumbLabels.value)
    entries.forEach(entry => {
      const label = (entry.target as HTMLElement).dataset.thumbLabel
      if (!label) return
      if (entry.isIntersecting) {
        next.add(label)
      } else {
        next.delete(label)
      }
    })
    visibleThumbLabels.value = next
  }, {
    root: videoThumbsRef.value,
    threshold: 0.2
  })

  thumbElements.forEach((element, label) => {
    element.dataset.thumbLabel = label
    thumbObserver?.observe(element)
  })
}

function resetThumbPlayback() {
  visibleThumbLabels.value = new Set()
  nextTick(initThumbObserver)
}

function getScales(capacity: number) {
  if (!capacity || capacity <= 0) return [0]
  const steps = capacity <= 30 ? 4 : 5
  const interval = capacity / steps
  const scales: number[] = []
  for (let index = 0; index <= steps; index += 1) {
    scales.push(Math.round(capacity - interval * index))
  }
  return scales
}

function openWarningDetail(item: WarningItem) {
  const imageUrl = warningImageUrl(item)

  detail.value = {
    title: warningTypeText(item),
    subtitle: '报警详情',
    warning: item,
    showImage: true,
    imageUrl,
    imageLabel: '报警抓拍',
    rows: [
      { label: '报警位置', value: item.name },
      { label: '报警时间', value: item.warning_time },
      { label: '处理状态', value: isWarningHandled(item) ? '已处理' : '未处理' },
      { label: '路由标识', value: item.roter || '-' },
      { label: '处理结果', value: warningHandleResult(item) }
    ],
    actionText: '定位摄像头',
    action: () => focusCameraByName(item.name)
  }
}

function openDeviceDetail(device: DeviceStatus) {
  detail.value = {
    title: device.name,
    subtitle: '设备状态下钻',
    rows: [
      { label: '设备总数', value: device.total },
      { label: '正常在线', value: device.online },
      { label: '故障离线', value: device.offline },
      { label: '在线率', value: `${device.total ? Math.round((device.online / device.total) * 100) : 0}%` }
    ]
  }
}

function openCarDetail(label: string, value: number) {
  detail.value = {
    title: `矿车${label}`,
    subtitle: '矿车统计下钻',
    rows: [
      { label: '当前数量', value },
      { label: '运输中', value: state.cars.online },
      { label: '空闲中', value: state.cars.offline },
      { label: '矿车总数', value: state.cars.total }
    ]
  }
}

function formatRate(value: number, total: number) {
  const safeTotal = Number(total || 0)
  if (safeTotal <= 0) return '0%'
  const ratio = Math.max(0, Math.min(100, Math.round((Number(value || 0) / safeTotal) * 100)))
  return `${ratio}%`
}

function carRatio(value: number) {
  return formatRate(value, state.cars.total)
}

function splitCount(value: number, index: 0 | 1) {
  const count = Math.max(0, Number(value || 0))
  return index === 0 ? Math.ceil(count / 2) : Math.floor(count / 2)
}

function openMaterialDetail(item: MaterialLevel) {
  detail.value = {
    title: item.lname,
    subtitle: `${materialType.value === 'ai' ? 'AI' : '雷达'}料位下钻`,
    rows: [
      { label: 'ID', value: item.id || '-' },
      { label: '物料类型', value: item.shitype },
      { label: '矿石吨数', value: `${numberText(item.ton)} 吨` },
      { label: '当前料位', value: `${numberText(item.now_quantity)} 米` },
      { label: '容量上限', value: `${numberText(item.all_quantity)} 米` },
      { label: '占比', value: `${Math.round(levelPercent(item))}%` },
      { label: '设备状态', value: String(item.status) === '0' ? '正常' : '异常' }
    ],
    actionText: '校验高度',
    action: () => openCheckHeightDialog(item)
  }
}

function openCheckHeightDialog(item: MaterialLevel) {
  checkHeightDialog.value = {
    id: item.id || '',
    lname: item.lname,
    currentHeight: item.all_quantity,
    height: String(item.all_quantity),
    loading: false,
    message: '',
    error: ''
  }
}

async function submitCheckHeight() {
  if (!checkHeightDialog.value) return
  const dialog = checkHeightDialog.value
  // 如果用户没有输入，使用接口返回的默认值
  const heightValue = dialog.height !== '' && dialog.height !== null && dialog.height !== undefined
    ? dialog.height
    : dialog.currentHeight
  dialog.loading = true
  dialog.error = ''
  dialog.message = ''
  try {
    await dpApi.checkHeight({
      id: dialog.id,
      nowQuantity: heightValue
    })
    dialog.message = '校验成功'
    // 刷新料位数据
    void refreshMaterials()
  } catch (err) {
    dialog.error = '校验失败，请重试'
    console.error('Check height error:', err)
  } finally {
    dialog.loading = false
  }
}

function closeCheckHeightDialog() {
  checkHeightDialog.value = null
}

function openWellDetail(well: WellLog) {
  detail.value = {
    title: well.name,
    subtitle: '出入料下钻',
    rows: [
      { label: '今日入料', value: `${numberText(well.rTotal)} 吨` },
      { label: '今日出料', value: `${numberText(well.cTotal)} 吨` },
      { label: '入出差值', value: `${numberText(Number(well.rTotal) - Number(well.cTotal))} 吨` }
    ]
  }
}

function openIoDetail(label: string, total: number, cars: number) {
  detail.value = {
    title: `今日${label}`,
    subtitle: '出入料总览下钻',
    rows: [
      { label: '吨数', value: `${numberText(total)} 吨` },
      { label: '车次', value: cars },
      { label: '最近同步', value: lastUpdated.value || '-' }
    ]
  }
}

async function loadIoRecords() {
  if (!ioDialog.value) return
  const current = ioDialog.value
  ioDialog.value = { ...current, loading: true }

  const result = await fetchIoRecords({
    type: current.type,
    page: current.page,
    pageSize: current.pageSize
  })

  if (!ioDialog.value) return
  ioDialog.value = {
    ...ioDialog.value,
    records: result.records,
    total: result.total,
    loading: false
  }
}

async function fetchIoRecords(params: { type: IoType; page: number; pageSize: number }) {
  let response
  if (params.type === '出料') {
    response = await dpApi.outLogs({
      page: params.page - 1,
      size: params.pageSize,
      sort: 'weighing_time,desc'
    })
  } else {
    response = await dpApi.carsLogs({
      page: params.page - 1,
      size: params.pageSize
    })
  }

  return {
    total: Number(response.totalElements || 0),
    records: (response.content || []).map((item: any) => toIoRecord(item, params.type))
  }
}

function toIoRecord(item: any, type: IoType): IoRecord {
  if (type === '出料') {
    return {
      id: String(item.ydNo || `${item.wellingPoint}-${item.weighingTime}-${item.plateNumber}`),
      wellName: item.wellingPoint || '-',
      section: item.instruction || '-',
      oreType: item.mineralType || '-',
      oreName: item.miners || '-',
      carNo: item.plateNumber || '-',
      bigs: item.releaseDate || '-',
      foreignMatter: item.ydNo || '-',
      carCount: item.discharge || '-',
      ton: Number(item.totalTonnage || 0),
      videoPath: '',
      imagePath: '',
      loadRate: item.schedule || '-',
      transitSeconds: item.vehicles || '-',
      unloadTime: item.weighingTime || '-'
    }
  } else {
    return {
      id: String(item.id || `${item.lname}-${item.createTime}-${item.car || item.cars}`),
      wellName: item.lname || '-',
      section: item.company || '-',
      oreType: shitypeText(item.shitype),
      oreName: item.persons || '-',
      carNo: item.car || item.cars || '-',
      bigs: item.bigs || '-',
      foreignMatter: item.yiwu || '-',
      carCount: item.name || '-',
      ton: Number(item.fulls || 0),
      videoPath: item.videopath || '',
      imagePath: item.imgpath || '',
      loadRate: String(item.rate ?? '-'),
      transitSeconds: String(item.timecha ?? '-'),
      unloadTime: item.createTime || '-'
    }
  }
}

function shitypeText(value: string | number | undefined) {
  const text = String(value ?? '')
  if (text === '1') return '矿石'
  if (text === '2') return '废石'
  return text || '-'
}

function openIoDialog(type: IoType) {
  ioDialog.value = {
    type,
    page: 1,
    pageSize: IO_PAGE_SIZE,
    total: 0,
    records: [],
    loading: false
  }
  void loadIoRecords()
}

function closeIoDialog() {
  ioDialog.value = null
  ioMediaDialog.value = null
}

function changeIoPage(page: number) {
  if (!ioDialog.value) return
  ioDialog.value = {
    ...ioDialog.value,
    page: Math.max(1, Math.min(page, ioTotalPages.value))
  }
  void loadIoRecords()
}

function openIoMedia(record: IoRecord, kind: 'video' | 'image') {
  const source = kind === 'video' ? record.videoPath : record.imagePath
  if (!source) return
  ioMediaDialog.value = {
    kind,
    title: `${record.wellName} ${kind === 'video' ? '过程视频' : '装载图片'}`,
    url: resolveMediaUrl(source),
    record
  }
}

function closeIoMedia() {
  ioMediaDialog.value = null
}

function resolveMediaUrl(path: string) {
  if (/^https?:\/\//i.test(path)) return path
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  return new URL(normalizedPath, MEDIA_BASE || window.location.origin).toString()
}

function openCameraDetail(label: string) {
  detail.value = {
    title: label,
    subtitle: '视频监控下钻',
    rows: [
      { label: '当前摄像头', value: label },
      { label: '视频地址', value: currentCamera.value?.url && currentCamera.value.url !== 'about:blank' ? currentCamera.value.url : '等待视频流地址' },
      { label: '在线状态', value: loading.value ? '同步中' : '监控中' }
    ]
  }
}

function openCameraListDetail() {
  detail.value = {
    title: '摄像头列表',
    subtitle: '视频源下钻',
    rows: cameras.value.map((item: CameraItem) => ({ label: item.label, value: item.url && item.url !== 'about:blank' ? '已配置' : '未配置' }))
  }
}

function focusCameraByName(name: string) {
  const matched = cameras.value.find(item => item.label.includes(name) || name.includes(item.label.replace('入料口', '')))
  if (matched) activeCameraLabel.value = matched.label
  detail.value = null
}

function drillMaterial(item: MaterialLevel) {
  const option = state.selectOptions.find(entry => entry.lable.includes(item.lname) || item.lname.includes(entry.lable))
  if (option) {
    selectedDept.value = option.value
    void refreshMaterials()
  }
  focusCameraByName(item.lname)
}

function openBarDetail(month: string, value: number) {
  detail.value = {
    title: `${month}出矿量`,
    subtitle: '年度柱状图下钻',
    rows: [
      { label: '月份', value: month },
      { label: '出矿量', value: `${numberText(value)} 吨` },
      { label: '年度累计', value: `${numberText(state.bar.total)} 吨` }
    ]
  }
}

function renderBarChart() {
  if (!barChartRef.value) return
  if (!isPageVisible.value) return

  const barData = barChartData()
  const currentData = `${barData.xA.join('|')}|${barData.yA.join('|')}|${theme.value}`

  if (currentData === lastBarChartData) return
  lastBarChartData = currentData

  window.clearTimeout(renderBarChartTimer)
  renderBarChartTimer = window.setTimeout(() => {
    if (!isPageVisible.value) return

    if (!barChart) {
      barChart = echarts.init(barChartRef.value)
      barChart.on('click', params => {
        const name = String(params.name || '')
        const value = Number(params.value || 0)
        openBarDetail(name, value)
      })
    }

    const isCommand = theme.value === 'command'
    const isGreenMine = theme.value === 'green-mine'
    const primary = isCommand ? '#ff9c25' : (isGreenMine ? '#3dd158' : '#16d9ff')
    const secondary = isCommand ? '#8b3b16' : (isGreenMine ? '#1a9f46' : '#265dff')
    const axis = isCommand ? 'rgba(255, 216, 156, .72)' : (isGreenMine ? 'rgba(197, 255, 210, .72)' : 'rgba(197, 239, 255, .7)')
    const grid = isCommand ? 'rgba(255, 146, 43, .2)' : (isGreenMine ? 'rgba(61, 209, 88, .22)' : 'rgba(45, 176, 255, .22)')

    barChart.setOption({
      grid: { left: 70, right: 14, top: 44, bottom: barData.zoomEnabled ? 82 : 64 },
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(5,7,12,.94)', borderColor: primary, textStyle: { color: '#fff' } },
      xAxis: {
        type: 'category',
        data: barData.xA,
        axisLine: { show: false },
        axisTick: { show: false },
        axisLabel: { color: axis, rotate: 45, fontSize: 15, margin: 14, interval: 0 }
      },
      yAxis: {
        type: 'value',
        splitLine: { lineStyle: { color: grid, type: 'dashed' } },
        axisLabel: { color: axis, fontSize: 15 }
      },
      series: [
        {
          name: '出矿量',
          type: 'bar',
          data: barData.yA,
          barWidth: 24,
          barMinHeight: 3,
          itemStyle: {
            borderRadius: [8, 8, 0, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: primary },
              { offset: 1, color: secondary }
            ])
          }
        }
      ],
      dataZoom: barData.zoomEnabled
        ? [
            {
              type: 'slider',
              xAxisIndex: 0,
              startValue: barData.startValue,
              endValue: barData.endValue,
              height: 14,
              bottom: 18,
              borderColor: grid,
              fillerColor: isCommand ? 'rgba(255, 156, 37, .16)' : (isGreenMine ? 'rgba(61, 209, 88, .16)' : 'rgba(32, 215, 255, .16)'),
              handleStyle: { color: primary },
              textStyle: { color: axis },
              brushSelect: false,
              showDetail: false
            },
            {
              type: 'inside',
              xAxisIndex: 0,
              startValue: barData.startValue,
              endValue: barData.endValue,
              zoomOnMouseWheel: false,
              moveOnMouseMove: true,
              moveOnMouseWheel: true
            }
          ]
        : []
    }, { notMerge: false, lazyUpdate: true, silent: true })
  }, 200)
}

function barChartData() {
  const xA = state.bar.xA || []
  const sourceY = state.bar.yA || []
  const yA = xA.map((_, index) => Number(sourceY[index] || 0))
  const visibleCount = Math.max(1, BAR_VISIBLE_MONTHS)
  const currentMonthIndex = currentBarMonthIndex(xA)
  const endValue = currentMonthIndex >= 0 ? currentMonthIndex : Math.max(0, xA.length - 1)
  const startValue = Math.max(0, endValue - visibleCount + 1)

  return {
    xA,
    yA,
    startValue,
    endValue,
    zoomEnabled: xA.length > visibleCount
  }
}

function currentBarMonthIndex(months: string[]) {
  const month = `${now.value.getMonth() + 1}`.padStart(2, '0')
  const normalizedMonth = `${Number(month)}月`
  const currentMonth = `${month}月`
  for (let index = months.length - 1; index >= 0; index -= 1) {
    const label = String(months[index] || '').trim()
    if (label === currentMonth || label === normalizedMonth) return index
  }
  return -1
}

function updateBoardScale() {
  window.clearTimeout(resizeTimer)
  resizeTimer = window.setTimeout(() => {
    const width = window.innerWidth
    const height = window.innerHeight
    boardScaleX.value = width / 1920
    boardScaleY.value = height / 1080
    barChart?.resize()
  }, 100)
}

function handleVisibilityChange() {
  isPageVisible.value = document.visibilityState !== 'hidden'
  if (isPageVisible.value) {
    now.value = new Date()
    void pollAlertState()
    nextTick(renderBarChart)
    resetThumbPlayback()
    startAlarmScroll()
    if (autoCycleEnabled.value) startAutoCycle()
  } else {
    visibleThumbLabels.value = new Set()
    stopAlarmScroll()
    stopAutoCycle()
  }
}

watch(cameras, value => {
  if (!activeCameraLabel.value && value[0]) activeCameraLabel.value = value[0].label
}, { immediate: true })

watch(videoGroups, groups => {
  if (!groups.length) return
  const preferredGroup = groups.find(group => group.label === DEFAULT_CAMERA_GROUP_LABEL)
  const currentGroup = groups.find(group => group.label === activeCameraGroupLabel.value)
  const group = !userSelectedCameraGroup && preferredGroup ? preferredGroup : currentGroup || groups[0]
  if (activeCameraGroupLabel.value !== group.label) activeCameraGroupLabel.value = group.label
  const hasActiveCamera = group.children?.some(camera => camera.label === activeCameraLabel.value)
  if (!hasActiveCamera && group.children?.[0]) activeCameraLabel.value = group.children[0].label
  if (autoCycleEnabled.value) startAutoCycle()
}, { immediate: true })

watch(() => activeCameraGroupLabel.value, () => {
  videoPage.value = 1
})

watch(videoPageTotal, total => {
  if (videoPage.value > total) videoPage.value = total
})

watch(() => cameraThumbs.value.map(item => item.label).join('\u0001'), () => {
  resetThumbPlayback()
})

watch(autoCycleEnabled, enabled => {
  if (enabled) {
    startAutoCycle()
  } else {
    stopAutoCycle()
  }
})

watch(() => state.warnings.map(item => warningKey(item)).join('\u0001'), (newVal, oldVal) => {
  if (newVal === oldVal || !isPageVisible.value) return
  const freshWarning = state.warnings.find(item => !knownWarningKeys.has(warningKey(item)))
  rememberCurrentWarnings()
  if (warningMonitorReady && freshWarning) triggerWarningNotice(freshWarning)
}, { flush: 'post' })

watch(() => `${state.warningSummary.total}|${state.warningSummary.wcl}`, (newVal, oldVal) => {
  if (newVal === oldVal || !isPageVisible.value) return
  checkWarningSummaryNotice()
}, { flush: 'post' })

watch(wsTick, (value, oldValue) => {
  if (!warningMonitorReady || value === oldValue || !isPageVisible.value) return
  rememberCurrentWarnings()
  rememberWarningSummary()
  triggerWarningNotice(latestWarningNotice())
}, { flush: 'post' })

watch(() => state.devices.map(item => `${item.name}|${item.total}|${item.online}|${item.offline}`).join('\u0001'), (newVal, oldVal) => {
  if (newVal === oldVal || !isPageVisible.value) {
    rememberCurrentDevices()
    return
  }
  const offlineDevice = state.devices.find(item => {
    const offline = Number(item.offline || 0)
    const previous = knownDeviceOffline.get(item.name)
    return previous === undefined ? offline > 0 : offline > previous
  })
  rememberCurrentDevices()
  if (deviceMonitorReady && offlineDevice) triggerDeviceNotice(offlineDevice)
}, { flush: 'post' })

watch(() => `${state.bar.total}|${state.bar.xA.join(',')}|${state.bar.yA.join(',')}`, (newVal, oldVal) => {
  if (newVal === oldVal || !isPageVisible.value) return
  nextTick(renderBarChart)
}, { flush: 'post' })
watch(theme, (newVal, oldVal) => {
  if (newVal === oldVal) return
  nextTick(renderBarChart)
}, { flush: 'post' })

onMounted(async () => {
  updateBoardScale()
  await refreshAll()
  rememberCurrentWarnings()
  rememberWarningSummary()
  rememberCurrentDevices()
  warningMonitorReady = true
  deviceMonitorReady = true
  if (import.meta.env.DEV || new URLSearchParams(window.location.search).has('debug')) {
    window.__dpTestWarning = pushTestWarning
    window.__dpTestDeviceOffline = pushTestDeviceOffline
  }
  openWarningStream()
  void refreshDatunWeather()
  await nextTick()
  renderBarChart()
  initThumbObserver()

  clockTimer = window.setInterval(() => {
    if (!isPageVisible.value) return
    now.value = new Date()
  }, 1000)

  refreshTimer = window.setInterval(() => {
    if (!isPageVisible.value) return
    void refreshAll()
  }, FULL_REFRESH_INTERVAL)

  // alertPollTimer = window.setInterval(() => {
  //   void pollAlertState()
  // }, ALERT_POLL_INTERVAL)

  noticeRepeatTimer = window.setInterval(repeatActiveNotices, NOTICE_REPEAT_INTERVAL)

  startAlarmScroll()

  weatherTimer = window.setInterval(() => {
    if (!isPageVisible.value) return
    void refreshDatunWeather()
  }, 30 * 60 * 1000)

  document.addEventListener('visibilitychange', handleVisibilityChange)
  document.addEventListener('click', handleHeaderSelectDocumentClick)
  window.addEventListener('resize', updateBoardScale)
})

onBeforeUnmount(() => {
  window.clearInterval(clockTimer)
  window.clearInterval(refreshTimer)
  window.clearInterval(alertPollTimer)
  window.clearInterval(noticeRepeatTimer)
  window.clearInterval(weatherTimer)
  window.clearInterval(groupCycleTimer)
  window.clearTimeout(warningPulseTimer)
  window.clearTimeout(warningToastTimer)
  window.clearTimeout(deviceToastTimer)
  window.clearTimeout(renderBarChartTimer)
  window.clearTimeout(resizeTimer)
  stopAlarmScroll()
  thumbObserver?.disconnect()
  if (window.__dpTestWarning === pushTestWarning) delete window.__dpTestWarning
  if (window.__dpTestDeviceOffline === pushTestDeviceOffline) delete window.__dpTestDeviceOffline
  document.removeEventListener('visibilitychange', handleVisibilityChange)
  document.removeEventListener('click', handleHeaderSelectDocumentClick)
  window.removeEventListener('resize', updateBoardScale)
  barChart?.dispose()
})
</script>

<style src="./styles/base.css"></style>

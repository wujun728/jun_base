﻿<!-- 项目-我的项目-任务列表页面 -->
<template>
  <div class="divBox bill-type" :class="isDrawer ? 'mt0' : ''" @click.stop="hideMembersPicker()">
    <el-card :class="isDrawer ? 'no-border' : 'normal-page'" class="employees-card">
      <oaFromBox
        :treeData="treeData"
        :treeDefault="treeDefault"
        :search="search"
        :viewSearch="viewSearch"
        :dropdownList="dropdownList"
        :total="total"
        :title="$route.meta.title"
        btnText="新建任务"
        @treeChange="treeChange"
        @addDataFn="addProgram"
        @dropdownFn="dropdownFn"
        @confirmData="confirmData"
      ></oaFromBox>

      <div>
        <div class="table mt10" @mouseleave="mouseLeave">
          <div v-show="headerShow" class="table-header">
            <div class="header-right">
              <span class="header-left fz13" v-show="headerShow">已选择{{ ids.length }}个工作项</span>
              <span>
                <span class="fz13">批量设置：</span>

                <el-select
                  v-model="formData.program_id"
                  size="small"
                  clearable
                  @clear="setValueNull"
                  placeholder="项目"
                  @change="handleContract"
                  style="width: 100px; margin-right: 10px"
                >
                  <el-option v-for="item in projectList" :key="item.value" :label="item.label" :value="item.value">
                    <div>
                      <span>{{ item.label }}</span>
                      <span
                        v-if="item.status == 1 || item.status == 2"
                        :class="item.status == 1 ? 'program-stop' : 'program-close'"
                        >{{ item.status == 1 ? '已暂停' : '已关闭' }}</span
                      >
                    </div>
                  </el-option>
                </el-select>

                <el-select
                  v-model="formData.version_id"
                  size="small"
                  filterable
                  clearable
                  :disabled="!formData.program_id"
                  collapse-tags
                  placeholder="版本"
                  style="width: 100px; margin-right: 10px"
                >
                  <el-option v-for="item in programVersionList" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>

                <el-select
                  v-model="formData.uid"
                  size="small"
                  filterable
                  clearable
                  placeholder="负责人"
                  :disabled="!formData.program_id"
                  style="width: 100px; margin-right: 10px"
                >
                  <el-option v-for="item in programMemberOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>

                <el-select
                  v-model="formData.status"
                  size="small"
                  filterable
                  clearable
                  collapse-tags
                  placeholder="状态"
                  style="width: 100px; margin-right: 10px"
                >
                  <el-option
                    v-for="item in selectData.statusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>

                <el-cascader
                  size="small"
                  v-model="formData.path"
                  :options="programOptions"
                  :props="{ checkStrictly: true }"
                  collapse-tags
                  clearable
                  @change="handleRemove"
                  placeholder="父工作项"
                  :disabled="!formData.program_id"
                  style="width: 100px; margin-right: 10px"
                >
                </el-cascader>
                <el-date-picker
                  v-model="formData.start_date"
                  size="small"
                  type="date"
                  clearable
                  :format="'yyyy-MM-dd'"
                  :value-format="'yyyy-MM-dd'"
                  placeholder="开始时间"
                  style="width: 130px; margin-right: 10px"
                ></el-date-picker>
                <el-date-picker
                  v-model="formData.end_date"
                  size="small"
                  type="date"
                  clearable
                  :format="'yyyy-MM-dd'"
                  :value-format="'yyyy-MM-dd'"
                  placeholder="结束时间"
                  :picker-options="{ disabledDate: disabledEndDate }"
                  style="width: 130px; margin-right: 10px"
                ></el-date-picker>

                <el-button type="primary" size="small" @click="submitEvt"> 确定 </el-button>
                <el-button size="small" @click="deleteEvt"> 删除 </el-button>
                <span class="cancel" @click="cancelEvt">取消</span>
              </span>
            </div>
          </div>
          <!-- :height="isDrawer ? 'calc(100vh - 340px)' : tableHeight" -->
          <!-- default-expand-all -->
          <el-table
            :data="tableData"
            ref="tableData"
            style="width: 100%"
            v-loading="loading"
            row-key="id"
            cell-class-name="handle"
            @selection-change="handleSelectionChange"
            class="draggable-table"
            :class="treePaddingClass"
            :header-cell-style="{ background: '#f7fbff' }"
            @cell-mouse-enter="handleMouseEnter"
            @cell-mouse-leave="handleMouseLeave"
          >
            <el-table-column type="" width="20" fixed>
              <template slot-scope="scope">
                <i v-if="hoveredRow === scope.$index" class="handle sort-handle iconfont icontuodong pointer"></i>
              </template>
            </el-table-column>
            <el-table-column v-if="headerShow" type="selection" width="55" fixed></el-table-column>
            <el-table-column prop="ident" label="任务编号" type="" width="120" fixed>
              <template slot-scope="scope">
                <span
                  class="ident copy pointer handle"
                  @click="copy"
                  title="点击复制工作项地址"
                  :data-clipboard-text="identAddress(scope.row)"
                  >#{{ scope.row.ident }}</span
                >
              </template>
            </el-table-column>
            <el-table-column prop="name" label="任务名称" min-width="320" fixed show-overflow-tooltip>
              <template slot-scope="scope">
                <!-- 新建 -->
                <div v-if="scope.row.add">
                  <el-input
                    :ref="`addInput-${scope.row.id}`"
                    v-model="addName"
                    placeholder="请输入子任务名称(Enter保存)"
                    @blur="handleBlur(scope.row)"
                    @keyup.native.enter="handleEnter(scope.row, 1)"
                    style="width: 260px"
                  ></el-input>
                </div>
                <div
                  v-if="!scope.row.add"
                  class="program-name"
                  @mouseenter="showIcon(scope.$index)"
                  @mouseleave="hideIcon(scope.$index)"
                >
                  <el-tooltip v-if="!scope.row.edit" class="item" disabled>
                    <div @click="editProgram(scope.row)" class="pointer flex align-center">
                      <i class="iconfont icontask1"> </i>
                      <div class="task-name line1">{{ scope.row.name }}</div>
                    </div>
                    <span slot="content">
                      <span>{{ scope.row.name }}</span>
                    </span>
                  </el-tooltip>
                  <!-- 编辑 -->
                  <el-input
                    v-if="scope.row.edit"
                    v-model="scope.row.name"
                    @blur="putName(scope.row, 0)"
                    @keyup.native.enter="putName(scope.row, 1)"
                  ></el-input>
                  <div class="icon-btn" v-if="!scope.row.edit">
                    <el-tooltip
                      v-if="iconVisible[scope.$index] && scope.row.level !== 4"
                      class="item"
                      effect="dark"
                      content="新建子工作项"
                      placement="top"
                      :open-delay="500"
                    >
                      <i class="iconfont icona-ziji1x pointer" @click.stop="addChild(scope.row)"></i>
                    </el-tooltip>
                    <el-tooltip
                      v-if="iconVisible[scope.$index] && scope.row.operate"
                      class="item"
                      effect="dark"
                      content="编辑"
                      placement="top"
                      :open-delay="500"
                    >
                      <i class="iconfont iconbianji pointer" @click="editChild(scope.row.id)"></i>
                    </el-tooltip>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="100">
              <template slot-scope="scope">
                <el-dropdown
                  trigger="click"
                  placement="bottom"
                  @command="putProgramTask('priority', $event, scope.row.id)"
                >
                  <span class="el-dropdown-link pointer dack" @click="isOperate(scope.row)">
                    <el-tag v-if="scope.row.priority == 1" type="danger" effect="dark" size="mini">紧急</el-tag>
                    <el-tag v-else-if="scope.row.priority == 2" type="warning" effect="dark" size="mini">高</el-tag>
                    <el-tag v-else-if="scope.row.priority == 3" effect="dark" size="mini">中</el-tag>
                    <el-tag v-else-if="scope.row.priority == 4" type="success" effect="dark" size="mini">低</el-tag>
                    <el-tag v-else type="info" effect="dark" size="mini">无优先级</el-tag>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <div v-if="scope.row.operate" class="fixed-height">
                      <el-dropdown-item
                        :command="item.value"
                        v-for="(item, index) in selectData.priorityOptions"
                        :key="index"
                      >
                        <el-tag
                          effect="dark"
                          size="mini"
                          :type="
                            item.value === 1
                              ? 'danger'
                              : item.value === 2
                              ? 'warning'
                              : item.value === 3
                              ? ''
                              : item.value === 4
                              ? 'success'
                              : 'info'
                          "
                          >{{ item.label }}</el-tag
                        >
                      </el-dropdown-item>
                    </div>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-dropdown
                  trigger="click"
                  placement="bottom"
                  @command="putProgramTask('status', $event, scope.row.id)"
                >
                  <span class="el-dropdown-link pointer status" @click="isOperate(scope.row)">
                    <el-tag v-if="scope.row.status == 0" type="warning" effect="plain" size="mini">未处理</el-tag>
                    <el-tag v-if="scope.row.status == 1" effect="plain" size="mini">进行中</el-tag>
                    <el-tag
                      v-if="scope.row.status == 2"
                      type="info"
                      style="color: #f95c96; border: 1px solid #f95c96"
                      effect="plain"
                      size="mini"
                      >已解决</el-tag
                    >
                    <el-tag v-if="scope.row.status == 3" type="success" effect="plain" size="mini">已验收</el-tag>
                    <el-tag v-if="scope.row.status == 4" type="danger" effect="plain" size="mini">已拒绝</el-tag>
                    <el-tag v-if="scope.row.status == 5" type="info" effect="plain" size="mini">已关闭</el-tag>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <div v-if="scope.row.operate" class="fixed-height">
                      <el-dropdown-item
                        :command="item.value"
                        v-for="(item, index) in selectData.statusOptions"
                        :key="index"
                      >
                        <el-tag
                          effect="plain"
                          size="mini"
                          :style="{
                            color: item.value === 2 ? '#f95c96' : '',
                            border: item.value === 2 ? ' 1px solid #f95c96' : '',
                            background: item.value === 2 ? '#fff' : ''
                          }"
                          :type="
                            item.value === 4
                              ? 'danger'
                              : item.value === 0
                              ? 'warning'
                              : item.value === 1
                              ? ''
                              : item.value === 3
                              ? 'success'
                              : 'info'
                          "
                          >{{ item.label }}</el-tag
                        >
                      </el-dropdown-item>
                    </div>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
            <el-table-column prop="admins" label="负责人" width="120">
              <template slot-scope="scope">
                <div class="pointer" @click="showAdmins(scope.row, scope.$index)" v-if="!adminsVisible[scope.$index]">
                  <img
                    v-if="scope.row.admins && scope.row.admins[0]"
                    class="img"
                    :src="scope.row.admins[0] ? scope.row.admins[0].avatar : ''"
                    alt=""
                  />
                  <span v-if="scope.row.admins">{{ scope.row.admins[0] ? scope.row.admins[0].name : '- -' }}</span>
                </div>
                <el-select
                  v-else
                  v-model="adminsUid"
                  size="small"
                  clearable
                  placeholder="负责人"
                  @click.native="changeAdmins(scope.row.program_id, scope.$index)"
                  @change="chooseAdmins(scope.row, scope.$index)"
                >
                  <el-option v-for="item in programMemberOptions" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="members" label="协作者" min-width="180" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-tooltip
                  v-if="!membersVisible[scope.$index]"
                  placement="top"
                  :disabled="scope.row.members && scope.row.members.length > 0 ? false : true"
                  :open-delay="500"
                >
                  <template>
                    <div @click="showMembers(scope.row, scope.$index)" class="pointer line1 members-line">
                      <span v-for="(item, index) in scope.row.members" :key="index">
                        <img class="img" :src="item.avatar" />
                      </span>
                      <span v-if="scope.row.members && scope.row.members.length == 0">- -</span>
                    </div>
                  </template>
                  <div slot="content">
                    <div class="pointer tooltip">
                      <span v-for="(item, index) in scope.row.members" :key="index">
                        <img class="img" :src="item.avatar" />
                        {{ item.name || '- -' }}
                        <span v-if="scope.row.members && scope.row.members.length - 1 > index">、</span>
                      </span>
                      <span v-if="scope.row.members && scope.row.members.length == 0">- -</span>
                    </div>
                  </div>
                </el-tooltip>
                <el-select
                  v-else
                  v-model="membersId"
                  size="small"
                  multiple
                  filterable
                  collapse-tags
                  placeholder="指派协作者"
                  @change="changeMembers(scope.row, scope.$index)"
                >
                  <el-option v-for="item in programMemberOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="program" label="关联项目" min-width="180" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-dropdown
                  trigger="click"
                  placement="bottom"
                  @command="putProgramTask('program_id', $event, scope.row.id)"
                >
                  <div class="pointer" @click="isOperate(scope.row)">
                    <el-tooltip class="item" effect="dark" placement="top" :open-delay="500" disabled>
                      <div class="line1 members-line">
                        {{ scope.row.program ? scope.row.program.name : '- -' }}
                      </div>
                      <div slot="content" style="max-width: 180px">
                        {{ scope.row.program ? scope.row.program.name : '- -' }}
                      </div>
                    </el-tooltip>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <div v-if="scope.row.operate" class="fixed-height">
                      <el-dropdown-item :command="item.value" v-for="(item, index) in projectList" :key="index">
                        <div>
                          <span>{{ item.label }}</span>
                          <span
                            v-if="item.status == 1 || item.status == 2"
                            :class="item.status == 1 ? 'program-stop' : 'program-close'"
                            >{{ item.status == 1 ? '已暂停' : '已关闭' }}</span
                          >
                        </div>
                      </el-dropdown-item>
                    </div>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
            <el-table-column prop="version" label="关联版本" width="100">
              <template slot-scope="scope">
                <div
                  @click="showVersion(scope.row, scope.$index, 1)"
                  v-if="!versionVisible[scope.$index]"
                  class="pointer"
                >
                  <span class="infos">{{ scope.row.version ? scope.row.version.name : '- -' }}</span>
                </div>
                <el-select
                  v-else
                  v-model="versionId"
                  size="small"
                  placeholder="请选择关联版本"
                  @click.native="changeVersion(scope.row.program_id, scope.$index)"
                  @change="chooseVersion(scope.row, scope.$index)"
                >
                  <el-option v-for="item in programVersionList" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="plan_start" label="计划开始" width="160" header-align="left" show-overflow-tooltip>
              <template slot-scope="scope">
                <div
                  class="pointer width100"
                  v-if="!startShowStates[scope.row.id]"
                  @click="handleStartClick(scope.row)"
                >
                  <span>{{ scope.row.plan_start || '- -' }}</span>
                </div>
                <el-date-picker
                  v-else
                  v-model="scope.row.plan_start"
                  size="small"
                  type="date"
                  clearable
                  :format="'yyyy-MM-dd'"
                  :value-format="'yyyy-MM-dd'"
                  placeholder="请选择项目计划开始日期"
                  @change="putProgramTask('plan_start', $event, scope.row.id)"
                  @blur="hideStartPicker(scope.row.id)"
                ></el-date-picker>
              </template>
            </el-table-column>
            <el-table-column prop="plan_end" label="计划结束" width="160" header-align="left" show-overflow-tooltip>
              <template slot-scope="scope">
                <div class="pointer width100" v-if="!endShowStates[scope.row.id]" @click="handleEndClick(scope.row)">
                  <span>{{ scope.row.plan_end || '- -' }}</span>
                </div>
                <el-date-picker
                  v-else
                  v-model="scope.row.plan_end"
                  size="small"
                  type="date"
                  clearable
                  :format="'yyyy-MM-dd'"
                  :value-format="'yyyy-MM-dd'"
                  placeholder="请选择项目计划开始日期"
                  @change="putProgramTask('plan_end', $event, scope.row.id)"
                  @blur="hideEndPicker(scope.row.id)"
                ></el-date-picker>
              </template>
            </el-table-column>
            <el-table-column prop="creator" label="创建人" width="120">
              <template slot-scope="scope">
                <div class="pointer">
                  <img
                    v-if="scope.row.creator && scope.row.creator[0]"
                    class="img"
                    :src="scope.row.creator[0] ? scope.row.creator[0].avatar : ''"
                  />
                  <span v-if="scope.row.creator">{{ scope.row.creator[0] ? scope.row.creator[0].name : '- -' }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="plan_start" label="创建时间" width="160">
              <template slot-scope="scope">
                <span>{{ scope.row.created_at || '- -' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="plan_end" label="更新时间" width="160">
              <template slot-scope="scope">
                <span>{{ scope.row.updated_at || '- -' }}</span>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="showDiv" :style="divStyle" class="follow-div">移动一个工作项</div>
        </div>
        <!-- <div class="page-fixed"> -->
        <el-pagination
          :page-size="tableFrom.limit"
          :current-page="tableFrom.page"
          :page-sizes="[15, 20, 30]"
          layout="total, sizes,prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
        <!-- </div> -->
      </div>
    </el-card>

    <!-- 新增弹窗表单 -->
    <addTask
      ref="addTask"
      :projectList="projectList"
      :programOptions="programOptions"
      :programMemberOptions="programMemberOptions"
      :programVersionList="programVersionList"
      @getMemberList="getMemberList"
      @getProgramVersionList="getProgramVersionList"
      @getProgramSelectList="getProgramSelectList"
      @getTableData="getTableData"
    />
    <!-- 编辑弹窗表单 -->
    <editTask
      ref="editTask"
      :projectList="projectList"
      :programOptions="programOptions"
      :programMemberOptions="programMemberOptions"
      :programVersionList="programVersionList"
      @getMemberList="getMemberList"
      @getProgramVersionList="getProgramVersionList"
      @getProgramSelectList="getProgramSelectList"
      @getTableData="getTableData"
      @openTask="openTask"
    />
  </div>
</template>

<script>
import {
  getProgramSelectApi,
  getProgramTaskApi,
  getProgramVersionSelectApi,
  getProgramTaskSelectApi,
  getProgramMemberApi,
  putProgramTaskBatchApi,
  deleteProgramTaskBatchApi,
  putProgramTaskApi,
  putTaskSortApi,
  saveSubordinateApi
} from '@/api/program'
import SettingMer from '@/libs/settingMer'
import editTask from './components/editTask'
import Sortable from 'sortablejs'
import ClipboardJS from 'clipboard'

export default {
  name: 'programTask',
  components: {
    oaFromBox: () => import('@/components/common/oaFromBox'),
    addTask: () => import('./components/addTask'),
    editTask
  },
  props: {
    programId: {
      type: Number,
      default: 0
    },
    isDrawer: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      tableData: [],

      showDiv: false,
      divStyle: {
        position: 'absolute',
        padding: '10px 5px',
        display: 'none', // 初始不显示
        left: '0',
        top: '0',
        background: '#f5f6f8',
        opacity: '0.8'
      },
      loading: false,
      activeIds: [],
      childrenLevel: 1,
      childrenOldPid: 0,
      startShowStates: {},
      endShowStates: {},
      membersShowStates: {},
      adminsShowStates: {},
      versionId: '',
      adminsUid: null,
      tableFrom: {
        page: 1,
        limit: 15,
        types: 0,
        name: '',
        pid: 0,
        program_id: this.programId || '',
        version_id: [],
        time_field: '',
        time: '',
        status: [],
        priority: [],
        scope_frame: 'all',
        scope_normal: 0,
        members: [],
        admins: []
      },
      total: 0,
      addName: '',
      parentId: 0,
      addRow: {},
      projectList: [],
      programVersionList: [],
      programOptions: [],
      programMemberOptions: [],
      headerShow: false,
      ids: [],
      checkedId: [],
      formData: {
        data: [],
        program_id: this.programId || null
      },
      versionVisible: {},
      adminsVisible: {},
      membersVisible: {},
      userList: [],
      selectData: {
        priorityOptions: [
          {
            value: 1,
            label: '紧急'
          },
          {
            value: 2,
            label: '高'
          },
          {
            value: 3,
            label: '中'
          },
          {
            value: 4,
            label: '低'
          },
          {
            value: 0,
            label: '无优先级'
          }
        ],
        statusOptions: [
          {
            value: 0,
            label: '未处理'
          },
          {
            value: 1,
            label: '进行中'
          },
          {
            value: 2,
            label: '已解决'
          },
          {
            value: 3,
            label: '已验收'
          },
          {
            value: 4,
            label: '已拒绝'
          },
          {
            value: 5,
            label: '已关闭'
          }
        ]
      },
      iconVisible: {},
      preventBlur: false,
      membersId: [],
      row: {},
      adminIndex: '',
      versionIndex: '',
      membersIndex: '',
      parentRoleId: '',
      currentRoleId: '',
      maps: new Map(),
      rowDown: {},
      rowUp: {},
      activeAdd: false,
      activeEdit: false,
      dragging: false,
      startX: 0,
      startY: 0,
      hoveredRow: null, // 用于追踪悬停的行索引
      sortableFix: undefined,
      sortable: undefined,
      activeRows: [],
      treeData: [
        {
          options: [
            {
              value: 0,
              label: '全部任务'
            },
            {
              value: 1,
              label: '我负责的'
            },
            {
              value: 2,
              label: '我参与的'
            },
            {
              value: 3,
              label: '我创建的'
            }
          ]
        }
      ],
      search: [
        {
          field_name: '任务名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '优先级',
          field_name_en: 'priority',
          form_value: 'checkbox',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: [
            {
              value: 1,
              label: '紧急'
            },
            {
              value: 2,
              label: '高'
            },
            {
              value: 3,
              label: '中'
            },
            {
              value: 4,
              label: '低'
            },
            {
              value: '0',
              label: '无优先级'
            }
          ]
        },
        {
          field_name: '状态',
          field_name_en: 'status',
          form_value: 'checkbox',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: [
            {
              value: '0',
              name: '未处理'
            },
            {
              value: 1,
              name: '进行中'
            },
            {
              value: 2,
              name: '已解决'
            },
            {
              value: 3,
              name: '已验收'
            },
            {
              value: 4,
              name: '已拒绝'
            },
            {
              value: 5,
              name: '已关闭'
            }
          ]
        },
        {
          field_name: '项目',
          field_name_en: 'program_id',
          form_value: 'select',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: []
        },
        {
          field_name: '版本',
          field_name_en: 'version_id',
          form_value: 'checkbox',
          multiple: true,
          props: {
            collapseTags: true
          },
          data_dict: []
        }
      ],
      viewSearch: [
        {
          title: '负责人',
          field: 'user_id'
        },
        {
          title: '协作者',
          field: 'update_user_id'
        },
        {
          title: '计划开始',
          field: 'plan_start',
          type: 'date_picker'
        },
        {
          title: '计划结束',
          field: 'plan_end',
          type: 'date_picker'
        },
        {
          title: '创建时间',
          field: 'created_at',
          type: 'date_picker'
        }
      ],
      dropdownList: [{ label: '批量操作', value: 1 }],
      treeDefault: 0
    }
  },
  computed: {
    treePaddingClass() {
      return this.headerShow ? 'header-show-padding' : 'normal-padding'
    }
  },
  watch: {
    programId(newVal) {
      this.$set(this.tableFrom, 'program_id', newVal)
      this.$set(this.formData, 'program_id', newVal)
      this.getProgramMember(newVal)
      this.programId = newVal
      this.getTableData()
      this.getProgramVersion(newVal)
    },
    // 监听 program_id 变化 选择项目自动获取版本
    'tableFrom.program_id'(val) {
      this.getProgramVersion(val)
    }
  },
  created() {
    this.tableFrom.program_id = this.programId ? this.programId : ''
    this.getProgram()
    // 判断是否为抽屉模式 不需要项目搜索框
    if (this.isDrawer && this.programId !== 0) {
      // 然后，从 this.search 中移除 field_name_en 为 'program_id' 的对象
      this.search = this.search.filter((item) => {
        return item.field_name_en !== 'program_id'
      })
      this.getTableData()
    }
    // 判断是否为抽屉模式 不是
    if (!this.isDrawer) {
      this.getTableData()
    }
    // 监听 program_id 变化 选择项目自动获取版本
    if (this.programId) {
      this.getProgramVersion(this.programId)
      this.getProgramMember(this.programId)
      let row = {
        program_id: this.programId
      }
      this.getProgramSelect(row)
    } else {
      this.getProgramVersion('')
    }
  },

  methods: {
    /**
     * 设置表格排序功能
     * 注意：确保表格主体元素的选择器正确，并且表格主体元素存在于DOM中。
     */
    setSort() {
        // 获取正确的表格主体元素
        const wrapper = this.$refs.tableData.$el.querySelector('.el-table__body-wrapper tbody')
       console.log(wrapper,9999)
        if (!wrapper) return

        // 销毁旧的Sortable实例
        this.sortable && this.sortable.destroy()
        this.sortableFix && this.sortableFix.destroy()

        // 配置项统一管理
        const sortableConfig = {
            animation: 150,
            ghostClass: 'sortable-ghost',
            handle: '.handle',  // 统一使用.sort-handle选择器
            onEnd: async (evt) => {
                try {
                    this.dragLoading = true
                    const oldIndex = evt.oldIndex
                    const newIndex = evt.newIndex
                    const [rowDown, rowUp] = [this.activeRows[oldIndex], this.activeRows[newIndex]]

                    // 阻止跨级拖动
                    if (rowDown.pid !== rowUp.pid) {
                        evt.item.parentNode.insertBefore(evt.item, [...evt.item.parentNode.children][oldIndex])
                        return this.$message.warning('不允许跨级拖动!')
                    }

                    // 收集可排序ID（优化为递归处理）
                    const collectIds = (items) => {
                        let ids = []
                        items.forEach(item => {
                            ids.push(item.id)
                            if (item.children) ids.push(...collectIds(item.children))
                        })
                        return ids
                    }

                    // 提交排序请求
                    await putTaskSortApi({ 
                        current: rowDown.id,
                        target: rowUp.id 
                    })
                    
                    // 刷新数据
                    await this.getTableData()
                } catch (e) {
                    console.error('排序失败:', e)
                    this.$message.error('排序操作失败')
                } finally {
                    this.dragLoading = false
                }
            }
        }

        // 初始化排序实例
        this.sortable = Sortable.create(wrapper, sortableConfig)
        this.sortableFix = Sortable.create(
            this.$refs.tableData.$el.querySelector('.el-table__fixed-body-wrapper tbody'), 
            sortableConfig
        )
    },

    /**
     * 递归处理表格数据，将其转换为可排序的结构
     * @param {Array} data - 原始表格数据
     * @returns {Array} - 处理后的可排序数据
     */
    flattenTreeData(treeData) {
      const result = []
      function flatten(node) {
        result.push(node)
        if (node.children && node.children.length > 0) {
          node.children.forEach((child) => flatten(child))
        }
      }
      treeData.forEach((node) => flatten(node))
      return result
    },


    /**
     * 处理鼠标进入事件
     * @param {Object} row - 鼠标进入的行数据
     */
    handleMouseEnter(row, column, cell, event) {
      let data = this.flattenTreeData(this.tableData)
      const rowIndex = data.indexOf(row)
      this.hoveredRow = rowIndex
    },


    /**
     * 处理鼠标离开事件
     * @returns 无返回值
     */
    handleMouseLeave() {
      this.hoveredRow = null
    },


    /**
     * 将指定元素移动到另一个元素后
     * @param data 数据数组
     * @param startId 起始元素ID
     * @param endId 目标元素ID
     * @returns 无返回值
     */
    moveElementAfter(data, startId, endId) {
      const startIndex = data.indexOf(startId)
      const endIndex = data.indexOf(endId)
      if (startIndex === -1 || endIndex === -1) {
        return
      }
      const [item] = data.splice(startIndex, 1)
      const newEndIndex = endIndex - (startIndex < endIndex ? 1 : 0)
      let datas = {
        current: startId,
        target: endId
      }
      putTaskSortApi(datas).then(() => {
        this.tableFrom.pid = 0
        this.getTableData()
        this.activeRows = []
        this.activeIds = []
        this.tableData = []
      })
    },


    /**
     * 处理鼠标移动事件
     * @param event 鼠标移动事件对象
     * @returns 无返回值
     */
    handleMouseMove(event) {
      const moveX = event.clientX - this.startX
      const moveY = event.clientY - this.startY
      if (Math.abs(moveX) > 5 || Math.abs(moveY) > 5) {
        // 仅当移动超过5像素时认为是拖拽
        this.dragging = true
        this.showDiv = true
      } else {
        this.dragging = false
        this.showDiv = false
      }
      this.updateDivPosition(event.pageX, event.pageY)
    },


    /**
     * 鼠标离开事件处理函数
     * @description 当鼠标离开时，将显示 div 的变量 showDiv 设置为 false，并将 div 的 display 样式设置为 'none'
     */
    mouseLeave() {
      this.showDiv = false
      this.divStyle.display = 'none'
    },


    /**
     * 更新 div 的位置
     * @param x 横向位置
     * @param y 纵向位置
     * @returns 无返回值
     */
    updateDivPosition(x, y) {
      const offsetX = -260 // 调整横向位置
      const offsetY = -200 // 调整纵向位置
      this.divStyle.left = x + offsetX + 'px'
      this.divStyle.top = y + offsetY + 'px'
      if (!this.showDiv) {
        // 只有在 showDiv 为 false 时才设置为显示状态
        this.divStyle.display = 'block'
      }
    },


    /**
     * 复制文本到剪贴板
     * @returns 无返回值
     */
    copy() {
      this.$nextTick(() => {
        const clipboard = new ClipboardJS('.copy')
        clipboard.on('success', () => {
          this.$message.success(this.$t('setting.copytitle'))
          clipboard.destroy()
        })
        clipboard.on('error', () => {})
        clipboard.onClick(event)
        this.$store.commit('app/SET_CLICK_TAB', false)
      })
    },


    /**
     * 处理鼠标按下事件
     * @param event 鼠标按下事件对象
     * @returns 无返回值
     */ 
    identAddress(row) {
      let type = 'type',
        relation_id = 'relation_id',
        program_id = 'program_id'
      return `${SettingMer.httpUrl}${window.location.pathname}${window.location.search}?&${type}=2&${relation_id}=${row.id}&${program_id}=${row.program_id}`
    },


   /**
     * 获取表格数据
     * @param tableFrom 表格参数对象，包含页码、每页数量等信息
     * @returns 无返回值
     */
    getTableData(tableFrom) {
      // 如果传入了 tableFrom，则更新当前的页码
      if (tableFrom) {
        this.tableFrom.page = tableFrom.page;
      }
      // 开启加载状态
      this.loading = true;
      // 确定请求数据时使用的参数
      const data = tableFrom || this.tableFrom;
      data.program_id = this.programId || this.tableFrom.program_id;

      // 懒加载刷新新父级
      if (!this.parentRoleId) {
        data.pid = 0;
        getProgramTaskApi(data)
          .then((res) => {
            // 更新表格数据
            this.tableData = res.data.list;
            // 扁平化树形数据
            this.activeRows = this.treeToTile(res.data.list);
            // 如果有 pid，更新子级表格数据
            if (data.pid) {
              this.tableChildrenData = res.data.list;
            }
            // 更新总记录数
            this.total = res.data.count;
            // 关闭加载状态
            this.loading = false;
            // 确保 DOM 更新后设置排序
            this.$nextTick(() => {
              this.setSort();
            });
          })
          .catch((error) => {
            // 出错时关闭加载状态
            this.loading = false;
            console.error('获取表格数据出错:', error);
          });
      } 
      // 懒加载刷新当前父级
      else if (this.parentRoleId && this.maps.get(this.parentRoleId)) {
        // 关闭加载状态
        this.loading = false;
        const { tree, treeNode, resolve } = this.maps.get(this.parentRoleId);
        // 加载数据
        this.load(tree, treeNode, resolve);
      } 
      // 懒加载刷新当前级
      else if (this.currentRoleId && this.maps.get(this.currentRoleId)) {
        // 关闭加载状态
        this.loading = false;
        const { tree, treeNode, resolve } = this.maps.get(this.currentRoleId);
        // 加载数据
        this.load(tree, treeNode, resolve);
      }
    },


    /**
     * 将树形数据转换为平铺数组
     * @param treeData 树形数据
     * @param childKey 子节点键名，默认为 'children'
     * @returns 平铺后的数组
     */
    treeToTile(treeData, childKey = 'children') {
      const arr = []
      const expanded = (data) => {
        if (data && data.length > 0) {
          data
            .filter((d) => d)
            .forEach((e) => {
              arr.push(e)
              expanded(e[childKey] || [])
            })
        }
      }
      expanded(treeData)
      return arr
    },


    /**
     * 添加任务
     * @returns 无返回值
     */
    addProgram() {
      this.getProgram()
      if (this.programId) {
        this.formData.program_id = Number(this.programId)
        this.formData.pid = 0
        let data = {
          program_id: Number(this.programId),
          pid: 0,
          describe: ''
        }
        this.$refs.addTask.openBox(0, data)
      } else {
        this.$refs.addTask.reset()
        this.$refs.addTask.drawer = true
      }
    },


    /**
     * 添加按钮后面更多下拉框选项点击事件处理函数
     * @param item 下拉框选项对象
     * @returns 无返回值
     */
    dropdownFn(item) {
      switch (item.value) {
        case 1:
          this.batchOperation()
          break
      }
    },


    /**
     * 打开任务
     * @param data 任务数据
     * @param type 任务类型，1为项目任务，0为其他任务
     */
    openTask(data, type) {
      let id = type == 1 ? data.pid : 0
      this.$refs.addTask.openBox(id, data)
      this.$refs.editTask.drawer = false
    },


    /**
     * 处理表格行点击事件
     * @param row 点击的行数据
     * @param column 点击的列数据
     */
    async getProgramMember(program_id, index, type) {
      const result = await getProgramMemberApi({ program_id })
      this.programMemberOptions = result.data
      if (this.programMemberOptions.length) {
        if (type == 1) {
          this.$set(this.adminsVisible, index, true)
        }
        if (type == 2) {
          this.$set(this.membersVisible, index, true)
        }
      }
    },


    /**
     * 获取选择项目的任务列表
     * @param row 行数据
     * @returns 无返回值
     */
    getProgramSelectList(row) {
      this.getProgramSelect(row)
    },


   /**
     * 获取选择项目的任务列表
     * @param row 行数据
     * @returns 无返回值
     */
    async getProgramSelect(row) {
      let data = {
        program_id: row.program_id
      }
      const result = await getProgramTaskSelectApi(data)
      result.data.map((item) => {
        if (row.id === item.value) {
          this.$set(item, 'disabled', true)
        }
      })
      this.programOptions = result.data
    },


   /**
     * 获取项目列表
     * @returns 无返回值
     */
    async getProgram() {
      const result = await getProgramSelectApi()
      this.projectList = result.data
      this.setDataDict('program_id', result.data)
    },


    /**
     * 获取成员列表
     * @param program_id 节目ID
     * @returns 无返回值
     */
    getMemberList(program_id) {
      this.getProgramMember(program_id)
    },


    /**
     * 获取项目版本列表
     * @param program_id 程序ID
     * @returns 无返回值，通过调用 this.getProgramVersion(program_id) 获取程序版本信息
     */
    getProgramVersionList(program_id) {
      this.getProgramVersion(program_id)
    },


    /**
     * 获取项目版本
     * @param program_id 程序ID
     * @returns 无返回值，通过调用 this.getProgramVersion(program_id) 获取程序版本信息
     */
    async getProgramVersion(program_id, index, type) {
      const result = await getProgramVersionSelectApi({ program_id })
      this.programVersionList = result.data
      this.setDataDict('version_id', result.data)
      if (this.programVersionList.length) {
        this.$set(this.versionVisible, index, true)
      } else if (type == 1) {
        this.$message('请到任务详情设置版本')
      }
    },


    /**
     * 编辑任务
     * @param row 任务数据
     * @returns 无返回值
     */
    editProgram(row) {
      this.$refs.editTask.openBox(row.id)
      this.getProgramVersion(row.program_id)
      this.getMemberList(row.program_id)
      this.getProgramSelect(row)
    },


    /**
     * 批量操作
     * @returns 无返回值
     */
    batchOperation() {
      this.headerShow = !this.headerShow
    },


    /**
     * 处理列表复选框选择项变化事件
     * @param val 选择项数组
     * @returns 无返回值
     */
    handleSelectionChange(val) {
      this.checkedId = val.map((item) => item.id)
      this.ids = val
    },


    /**
     * 将表单数据中的 version_id、uid 和 path 设置为空字符串
     */
    setValueNull() {
      this.formData.version_id = ''
      this.formData.uid = ''
      this.formData.path = ''
    },

    /**
     * 处理合同选择事件
     * @param eid 合同ID
     * @returns 无返回值
     */
    handleContract(eid) {
      let row = {
        program_id: eid
      }
      this.getProgramVersionList(eid)
      this.getMemberList(eid)
      this.getProgramSelectList(row)
    },


    handleRemove(e) {
      this.formData.path = e
      this.formData.pid = e[e.length - 1]
    },


    // 批量提交
    submitEvt() {
      if (this.ids.length == 0) {
        return this.$message.error('请至少选择一项')
      }
      this.ids.forEach((item) => {
        this.formData.data.push(item.id)
      })
      putProgramTaskBatchApi(this.formData).then((res) => {
        this.headerShow = false
        this.tableFrom.pid = ''
        this.getTableData()
        this.cancelEvt()
      })
    },


    // 批量删除
    deleteEvt() {
      if (this.ids.length == 0) {
        return this.$message.error('请至少选择一项')
      }
      this.ids.forEach((item) => {
        this.formData.data.push(item.id)
      })
      this.$modalSure('删除任务，关联的子级任务均会被删除').then(() => {
        deleteProgramTaskBatchApi({ data: this.formData.data }).then(() => {
          this.headerShow = false
          this.tableFrom.pid = ''
          this.getTableData()
        })
      })
    },

    // 批量取消
    cancelEvt() {
      this.formData = {
        program_id: this.programId || null,
        version_id: null,
        uid: null,
        pid: null,
        path: [],
        plan_end: '',
        status: null,
        data: []
      }
      this.ids = []
      this.userList = []
      this.headerShow = false
      this.$refs.tableData.clearSelection()
    },


    /**
     * 切换页码
     * @param page 页码
     */
    pageChange(page) {
      this.tableFrom.page = page
      this.getTableData()
    },


    /**
     * 处理表格每页显示条数改变的事件
     *
     * @param val 新的每页显示条数
     */
    handleSizeChange(val) {
      this.tableFrom.limit = val
      this.getTableData()
    },


    /**
     * 表单搜索
     * @param data 数据对象
     * @returns 无
     */
    confirmData(data) {
      if (data == 'reset') {
        this.treeDefault = 0
        this.tableFrom = {
          page: 1,
          limit: 15,
          types: 0,
          name: '',
          pid: 0,
          program_id: this.programId || '',
          version_id: [],
          time_field: '',
          time: '',
          status: [],
          priority: [],
          scope_frame: 'all',
          scope_normal: 0,
          members: [],
          admins: []
        }
        this.getTableData()
      } else {
        if (data.user_id) {
          data.admins = data.user_id
        }
        if (data.update_user_id) {
          data.members = data.update_user_id
        }
        Object.keys(data).forEach((key) => {
          if (key == 'user_id') {
            data.admins = data.user_id
          }
          if (key == 'update_user_id') {
            data.members = data.update_user_id
          }
          if (['plan_start', 'plan_end', 'created_at'].includes(key)) {
            data.time_field = key
            data.time = data[key]
          }
        })

        this.tableFrom = { ...this.tableFrom, ...data }
        this.tableFrom.page = 1
        this.getTableData()
      }
    },


    /**
     * 选择关联版本
     * @param id 版本ID
     * @param index 版本索引
     * @param type 版本类型
     * @returns Promise<void>
     */
    async changeVersion(id, index, type) {
      this.versionIndex = index
      await this.getProgramVersion(id, index, type)
    },


    /**
     * 修改时获取负责人
     * @param id 节目ID
     * @param index 修改位置索引
     * @param type 管理员类型
     * @returns 无返回值，异步执行
     */
    async changeAdmins(id, index, type) {
      this.adminIndex = index
      await this.getProgramMember(id, index, type)
    },


    /**
     * 更改获取协作者信息
     *
     * @param row 表格行数据
     * @param index 表格行索引
     * @param type 成员类型
     * @returns Promise
     */
    async changeMembers(row, index, type) {
      this.row = row
      await this.getProgramMember(row.program_id, index, type)
    },

    
    /**
     * 选择版本
     *
     * @param row 版本行数据
     * @param index 版本索引
     */
    chooseVersion(row, index) {
      this.putProgramTask('version_id', this.versionId, row.id)
      this.hideVersion(index)
    },
    /**
     * 选择负责任
     *
     * @param row 管理员对象
     * @param index 管理员列表索引
     */
    chooseAdmins(row, index) {
      this.putProgramTask('uid', this.adminsUid, row.id)
      this.hideAdmins(index)
    },
    // 修改单个任务信息
    putProgramTask(field, data, id) {
      let datas = {}
      datas['field'] = field
      if (!data) {
        data = ''
      }
      datas[field] = data
      if (field == 'pid') {
        datas['path'] = this.formData.path
      }
      putProgramTaskApi(id, datas).then(() => {
        this.activeEdit = false
        if (this.tableFrom.page) {
          this.getTableData(this.tableFrom)
        } else {
          this.getTableData()
        }
        this.$set(this.membersVisible, this.membersIndex, false)
      })
    },
    /**
     * 判断是否具有操作权限
     *
     * @param row 行数据对象
     * @returns 如果没有操作权限，则弹出提示信息；否则不返回任何值
     */
    isOperate(row) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }
    },
    /**
     * 处理开始点击事件
     *
     * @param row 表格行数据
     * @returns 无返回值
     */
    handleStartClick(row) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }
      this.$set(this.startShowStates, row.id, true)
    },
    /**
     * 隐藏开始时间选择器
     *
     * @param rowId 行ID
     * @returns 无返回值
     */
    hideStartPicker(rowId) {
      this.$set(this.startShowStates, rowId, false)
    },
    /**
     * 处理结束按钮点击事件
     *
     * @param row 当前行数据
     * @returns 无返回值
     */
    handleEndClick(row) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }
      this.$set(this.endShowStates, row.id, true)
    },
    /**
     * 隐藏结束时间选择器
     *
     * @param rowId 行ID
     */
    hideEndPicker(rowId) {
      this.$set(this.endShowStates, rowId, false)
    },
    /**
     * 隐藏成员选择器
     */
    hideMembersPicker() {
      if (this.membersVisible[this.membersIndex] == true && this.row.id) {
        this.putProgramTask('members', this.membersId, this.row.id)
      }
      this.$set(this.versionVisible, this.versionIndex, false)
      this.$set(this.adminsVisible, this.adminIndex, false)
      this.$set(this.membersVisible, this.membersIndex, false)
    },
    /**
     * 显示图标
     *
     * @param index 图标索引
     * @returns 无返回值
     */
    showIcon(index) {
      this.$set(this.iconVisible, index, true)
    },
    /**
     * 隐藏图标
     *
     * @param index 图标索引
     */
    hideIcon(index) {
      this.$set(this.iconVisible, index, false)
    },
    /**
     * 添加子节点
     *
     * @param row 要添加子节点的行数据
     * @returns 如果当前处于添加状态，则返回false，否则返回undefined
     */
    addChild(row) {
      console.log(row, this.activeAdd)
      if (this.activeAdd) {
        return false
      }
      this.addRow = row
      this.parentId = row.id
      this.addName = ''
      const newRow = {
        id: Date.now(),
        pid: row.id,
        name: '',
        children: [],
        add: true
      }
      this.activeAdd = true
      const findAndAdd = (array) => {
        array.forEach((item) => {
          if (item.id == row.id) {
            if (!item.children) {
              this.$set(item, 'children', [])
            }
            item.children.unshift(newRow)
            this.$set(item, 'has_children', true)
          } else if (item.children && item.children.length > 0) {
            findAndAdd(item.children)
          }
        })
      }
      findAndAdd(this.tableData)
    },
    // 回车提交
    handleEnter(row, type) {
      if (type === 1) {
        this.preventBlur = true
      }
      let data = {
        name: this.addName,
        pid: this.parentId
      }
      if (type === 2) {
        this.preventBlur = false
        return
      }
      if (type === 0) {
        this.preventBlur = false
      }
      saveSubordinateApi(data).then((res) => {
        this.tableFrom.pid = 0
        if (res.status == 200) {
          this.getTableData()
        }
      })
    },
    // 失去焦点
    handleBlur(row) {
      if (!this.addName.trim()) {
        this.removeRow(row)
        this.activeAdd = false
      } else {
        this.preventBlur = true
        this.activeAdd = false
      }
    },
    /**
     * 移除行
     *
     * @param row 要移除的行数据
     */
    removeRow(row) {
      let parent
      if (row.pid) {
        parent = this.findRow(this.tableData, row.pid)
      } else {
        parent = row
      }
      if (parent && parent.children.length > 0) {
        // 直接移除最后一个元素
        parent.children.pop()
      }
      // if (parent) {
      //   const index = parent.children.findIndex(child => child.id === row.id);
      //   parent.children.splice(index, 1);
      // } else {
      //   const index = this.tableData.findIndex(item => item.id === row.id);
      //   this.tableData.splice(index, 1);
      // }
    },
    /**
     * 查找指定id的节点行
     *
     * @param data 数据数组
     * @param id 目标id
     * @returns 返回找到的节点行，若未找到则返回null
     */
    findRow(data, id) {
      if (!id) {
        data.children = []
      }
      for (let i = 0; i < data.length; i++) {
        if (data[i].id === id) {
          return data[i]
        }
        if (data[i].children && data[i].children.length) {
          const found = this.findRow(data[i].children, id)
          if (found) return found
        }
      }
      return null
    },
    /**
     * 提交新增行
     *
     * @param id 行的ID
     */
    submitNewRow(id) {
      const index = this.tableData.findIndex((item) => item.id === id)
      if (index !== -1) {
        this.tableData[index].edit = false
      }
    },
    /**
     * 取消编辑
     *
     * @returns 无返回值
     */
    cancelEdit() {
      this.getTableData()
    },
    /**
     * 放置任务名称
     *
     * @param row 当前行数据
     * @param type 类型，1 表示新增，0 表示编辑
     */
    putName(row, type) {
      if (type === 1) {
        this.preventBlur = true
      }
      if (type === 0) {
        this.activeEdit = false
      }
      this.$nextTick(() => {
        if (type === 0 && this.preventBlur) {
          this.preventBlur = false
          return
        }
        if (row.name.trim()) {
          this.putProgramTask('name', row.name, row.id)
        } else {
          this.$message('任务名称不能为空')
        }
      })
    },
    /**
     * 编辑子节点
     *
     * @param currentId 当前节点ID
     * @returns 如果当前有编辑状态则返回false，否则设置编辑状态为true
     */
    editChild(currentId) {
      if (this.activeEdit) {
        return false
      }
      this.activeEdit = true
      this.tableData.forEach((item) => {
        if (item.id === currentId) {
          this.$set(item, 'edit', true)
        } else if (item.children) {
          item.children.forEach((it) => {
            if (it.id === currentId) {
              this.$set(it, 'edit', true)
            }
          })
        }
      })
    },
    /**
     * 显示编辑版本信息
     *
     * @param row 当前行数据
     * @param index 当前行索引
     * @param type 操作类型
     * @returns 无返回值
     */
    showVersion(row, index, type) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }

      this.versionId = ''
      this.$set(this.adminsVisible, this.adminIndex, false)
      this.$set(this.versionVisible, this.versionIndex, false)
      this.$set(this.membersVisible, this.membersIndex, false)
      this.versionId = row.version_id || ''
      this.changeVersion(row.program_id, index, type)
    },
    /**
     * 显示编辑负责人
     *
     * @param row 当前行数据
     * @param index 当前行索引
     * @returns 无返回值
     */
    showAdmins(row, index) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }
      this.adminsUid = null
      this.$set(this.adminsVisible, this.adminIndex, false)
      this.$set(this.versionVisible, this.versionIndex, false)
      this.$set(this.membersVisible, this.membersIndex, false)
      this.adminsUid = row.uid || null
      this.changeAdmins(row.program_id, index, 1)
    },
    /**
     * 显示编辑成员列表
     *
     * @param row 表格行数据
     * @param index 表格行索引
     * @returns 无返回值
     */
    showMembers(row, index) {
      if (!row.operate) {
        return this.$message('没有操作权限')
      }
      this.membersId = []
      this.$set(this.adminsVisible, this.adminIndex, false)
      this.$set(this.versionVisible, this.versionIndex, false)
      this.$set(this.membersVisible, this.membersIndex, false)
      this.membersIndex = index
      row.members.map((item) => {
        this.membersId.push(item.id)
      })
      this.changeAdmins(row.program_id, index, 2)
    },
    /**
     * 隐藏版本号
     *
     * @param index 版本号索引
     */
    hideVersion(index) {
      this.$set(this.versionVisible, index, false)
    },
    /**
     * 隐藏管理员
     *
     * @param index 管理员索引
     */
    hideAdmins(index) {
      this.$set(this.adminsVisible, index, false)
    },
    /**
     * 隐藏成员
     *
     * @param index 成员索引
     */
    hideMembers(index) {
      this.$set(this.membersVisible, index, false)
    },
    /**
     * 视图切换搜索
     *
     * @param data 树形控件变化后的数据
     */
    treeChange(data) {
      this.tableFrom.types = data.value
      this.getTableData(this.tableFrom)
    },
    /**
     * 设置数据字典
     *
     * @param field_name_en 字段名称英文标识
     * @param data_dict 数据字典
     */
    setDataDict(field_name_en, data_dict) {
      for (let i = 0; i < this.search.length; i++) {
        if (this.search[i].field_name_en == field_name_en) {
          this.search[i].data_dict = data_dict
          break
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-type {
  .header {
    display: flex;
    justify-content: space-between;
    span {
      font-size: 18px;
      line-height: 32px;
      color: #303133;
      font-weight: 500;
    }
    .fz30 {
      font-size: 30px;
      margin-left: 14px;
      color: #909399;
      font-weight: 400;
    }
  }
  .text-right {
    text-align: right;
  }
  .el-radio {
    margin-right: 15px;
  }
  .title {
    font-size: 15px;
    font-weight: 600;
    margin-left: 10px;
    position: relative;
    &:after {
      content: '';
      height: 100%;
      width: 3px;
      background-color: #1890ff;
      position: absolute;
      left: -10px;
      top: 0;
    }
  }
}
.status {
  ::v-deep .el-tag {
    background: #fff;
  }
}

::v-deep.add-color .el-tag.el-tag--info {
  color: #f95c96 !important;
}
.img {
  width: 22px;
  height: 22px;
  border-radius: 50%;
}
.mt0 {
  margin-top: 10px;
}
.infos {
  font-size: 12px;
  padding: 2px 4px;
  border-radius: 2px;
  background: #f0f2f5;
}
.point {
  cursor: pointer;
  &:hover {
    color: #1890ff;
  }
}
::v-deep.el-table th {
  background-color: #f7fbff;
}
.flex {
  display: flex;
  align-items: center;
}
.el-input__inner {
  height: 32px;
  line-height: 32px;
  cursor: pointer;
}
.table {
  position: relative;
}
/* 修复拖拽样式 */
.sortable-ghost {
  opacity: 0.5;
  background: #f5f7fa;
}

/* 修复操作列宽度 */
.el-table__fixed-right {
  right: 0 !important;
  width: 60px !important; /* 原20px导致操作列显示不全 */
}
.table-header {
  width: 97%;
  height: 44px;
  position: absolute;
  z-index: 10;
  left: 60px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-right: 50px;

  .header-left {
    // height: 44px;
    // line-height: 44px;
    // margin-top: 8px;
    float: left;
  }
  .header-right {
    position: absolute;
    right: 18px;
    width: 98%;
    margin-top: 6px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    text-align: right;
    background: #f7fbff;

    span {
      min-width: 70px;
    }
    .el-input__inner {
      width: 120px;
      min-width: 100px;
      margin-right: 14px;
    }
    .cancel {
      margin-left: 10px;
      color: #1890ff;
      cursor: pointer;
      margin-right: 14px;
    }
  }
}
::v-deep.el-table .cell {
  width: 100%;
  display: flex;
}
.program-name {
  width: 98%;
  display: inline-flex;
  justify-content: space-between;
  .iconbianji {
    margin-left: 6px;
  }
  .icon-btn {
    width: 50px;
  }
}
.members {
  ::v-deep.select-without-input .el-input__inner {
    cursor: pointer;
    background-color: transparent !important;
    border-color: transparent !important;
    box-shadow: none !important;
  }
  ::v-deep.select-without-input .el-input__icon {
    display: none;
  }
  ::v-deep.select-without-input .el-tag {
    display: none !important;
  }
}
.icontask1 {
  color: #1890ff;
  margin-right: 4px;
}
.fixed-height {
  max-height: 200px;
  overflow-y: auto;
}
.task-name {
  display: inline-block;
  max-width: 220px;
}
.line1 {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.members-line {
  width: 170px;
}
.tooltip {
  max-width: 180px;
}
.ident {
  font-size: 12px;
  color: #606266;
  background: #f5f5f5;
  margin-right: 20px;
  border-radius: 2px;
  padding: 3px 5px;
}
.program-stop {
  font-size: 12px;
  border: 1px solid #f90;
  color: #f90;
  padding: 0 2px;
}
.program-close {
  font-size: 12px;
  border: 1px solid #909399;
  color: #909399;
  padding: 0 2px;
}
::v-deep .el-table__placeholder {
  margin-left: 3px; //子节点无Child缩进
}
::v-deep .normal-padding .el-table__row:not([class*='el-table__row--level-']) td:nth-child(3) {
  padding-left: 15px; /* 当没有批量操作时，任务名称是第三列 */
}

::v-deep .header-show-padding .el-table__row:not([class*='el-table__row--level-']) td:nth-child(4) {
  padding-left: 15px; /* 启用批量操作时，任务名称变为第四列 */
}
.follow-div {
  position: absolute;
  display: none; /* 初始状态不显示 */
  pointer-events: none; /* 确保 div 不会拦截鼠标事件 */
  z-index: 1000; /* 确保在最上层 */
}
.btn-box {
  display: flex;
  align-items: flex-start;
}
.icontuodong {
  font-size: 13px;
}
.no-border {
  border: none;
  padding: 16px 6px;
  min-height: calc(100vh - 235px);
}
.normal-page {
  padding: 20px;
}
::v-deep.el-card__body {
  padding: 0;
}
.dack {
  ::v-deep .el-tag {
    border: none !important;
  }
}
</style>

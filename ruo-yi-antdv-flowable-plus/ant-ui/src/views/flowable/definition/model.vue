<template>
  <div>
    <bpmn-modeler
      ref="refNode"
      :xml="xml"
      :users="users"
      :groups="groups"
      :categorys="categorys"
      :is-view="false"
      @save="save"
      @showXML="showXML"
      @dataType="dataType"
    />
    <!--在线查看xml-->
    <el-dialog :title="xmlTitle" :visible.sync="xmlOpen" width="60%" append-to-body>
      <div>
        <pre v-highlight>
           <code class="xml">
                {{ xmlContent }}
           </code>
        </pre>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {readXml, roleList, saveXml, userList} from '@/api/flowable/definition'
import { Loading } from 'element-ui'
import bpmnModeler from '@/components/Process/index'
import vkbeautify from 'vkbeautify'
import Hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'

export default {
  name: 'Model',
  components: {
    bpmnModeler,
    vkbeautify
  },
  props: {
    deploymentId: {
      type: String,
      default: ''
    }
  },
  // 自定义指令
  directives: {
    highlight:(el) => {
      const blocks = el.querySelectorAll('pre code')
      blocks.forEach((block) => {
        Hljs.highlightBlock(block)
      })
    }
  },
  data() {
    return {
      xml: '', // 后端查询到的xml
      modeler:'',
      xmlOpen: false,
      xmlTitle: '',
      xmlContent: '',
      users: [],
      groups: [],
      categorys: [],

    }
  },
  created () {

    // const deployId = this.$route.query && this.$route.query.deployId
    const deployId = this.deploymentId
    //  查询流程xml
    if (deployId) {
      this.getModelDetail(deployId)
    }
    this.getDicts('sys_process_category').then(res => {
      this.categorys = res.data
    })
    this.getDataList()
  },
  methods: {
    /** xml 文件 */
    getModelDetail(deployId) {
      // 发送请求，获取xml
      readXml(deployId).then(res =>{
        this.xml = res.data
        this.modeler = res.data
      })
    },
    /** 保存xml */
    save(data) {
      const params = {
        name: data.process.name,
        category: data.process.category,
        xml: data.xml
      }
      this.showLoading()
      saveXml(params).then(res => {
        this.hideLoading()
        this.$elMsg(res.msg)
        this.$emit('ok')
        // 关闭当前标签页并返回上个页面
        // this.$store.dispatch('tagsView/delView', this.$route)
        // this.$router.go(-1)
      })
    },
    /** 指定流程办理人员列表 */
    getDataList() {
      // todo 待根据部门选择人员
      // const params = {
      //
      // }
      userList().then(res =>{
        // res.data.forEach(val =>{
        //   val.userId = val.userId.toString()
        // })
        this.users = res.data
        // const arr = {nickName: '流程发起人', userId: '${INITIATOR}'}
        // const arr = {nickName: '流程发起人', id: '${INITIATOR}'}
        // this.users.push(arr)
      })
      roleList().then(res =>{
        // res.data.forEach(val =>{
        //   val.roleId = val.roleId.toString()
        // })
        this.groups = res.data
      })
    },
    /** 展示xml */
    showXML(data){
      this.xmlTitle = 'xml查看'
      this.xmlOpen = true
      // debugger
      this.xmlContent = vkbeautify.xml(data)
    },
    /** 获取数据类型 */
    dataType(data){
      this.users = []
      this.groups = []
      // if (data) {
      //   if (data.dataType === 'dynamic') {
      //     if (data.userType === 'assignee') {
      //       // this.users = [{nickName: '${INITIATOR}', userId: '${INITIATOR}'},
      //       //               {nickName: '#{approval}', userId: '#{approval}'}
      //       //   ]
      //       this.users = [{name: '${INITIATOR}', id: '${INITIATOR}'},
      //                     {name: '#{approval}', id: '#{approval}'}
      //         ]
      //     } else if (data.userType === 'candidateUsers') {
      //       // this.users = [ {nickName: '#{approval}', userId: '#{approval}'}]
      //       this.users = [ {name: '#{approval}', id: '#{approval}'}]
      //     } else {
      //       // this.groups = [{roleName: '#{approval}', roleId: '#{approval}'}]
      //       this.groups = [{roleName: '#{approval}', id: '#{approval}'}]
      //     }
      //   } else {
          this.getDataList()
      //   }
      // }
    }
  },
}
</script>

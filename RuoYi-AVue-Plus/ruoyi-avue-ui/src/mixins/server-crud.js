import {getServer} from "@/api/system/server";
import {renderData} from "@/api/crud/crud";
import page from '@/const/page.js'

/**
 * 远程接口配置版, 在原先的基础上做了修改,支持远程的option的配置
 *
 * @param app
 * @param this.option
 * @returns {*|string|{"on-load": mixins.methods.getList, "refresh-change": mixins.methods.refreshChange, "row-update": mixins.methods.rowUpdate, "row-save": mixins.methods.rowSave, "row-del": mixins.methods.rowDel, "search-reset": mixins.methods.searchChange, "search-change": mixins.methods.searchChange}|{data: [], form: {}, api: any, page: {}, params: {}, loading: boolean, config: {}, option: {}}|{mixins}|{ref: string, data: [], tableLoading: boolean, option: {}}}
 * @author liukx
 * @date 2021年8月15日 17:10:27
 */
export default (app, clientOption = {}) => {
  let mixins = {
    async created() {
      await this.initContext();
    },
    data() {
      return {
        config: {},
        data: [],
        form: {},
        params: {},
        // api: require(`@/api/crud/crud`),
        api: require(`@/api/${clientOption.name}`),
        loading: false,
        page: {},
        option: {}
      }
    },
    computed: {
      // option () {
      //    return require(`@/option/${option.name}`).default(this)
      // },
      bindVal() {
        return {
          ref: 'crud',
          option: this.option,
          data: this.data,
          tableLoading: this.loading
        }
      },
      onEvent() {
        return {
          'on-load': this.getList,
          'row-save': this.rowSave,
          'row-update': this.rowUpdate,
          'row-del': this.rowDel,
          'refresh-change': this.refreshChange,
          'search-reset': this.searchChange,
          'search-change': this.searchChange
        }
      },
      rowKey() {
        let rowKey = this.getOption('rowKey') || 'id';
        return rowKey;
      }
    },
    methods: {
      async initContext() {
        let _self = this;
        console.log("页面初始化");
        let query = _self.$route.query;

        if (!query.server || !query.group) {
          this.msgError("参数有问题，请到菜单配置中查看是否配置server和group参数");
        } else {
          // 根据请求路径参数获取服务路由编号,根据编号获取domain
          let res = await getServer(query.server);
          // 开始渲染数据
          renderData(_self, clientOption, query, res.data);
        }
      },
      getList() {
        const callback = () => {
          let _self = this;
          this.loading = true;
          let pageParams = {}
          pageParams[this.getOption(page.pageNumber) || 'pageNumber'] = this.page.currentPage
          pageParams[this.getOption(page.pageSize) || 'pageSize'] = this.page.pageSize
          const data = Object.assign(pageParams, this.params)
          this.data = [];
          this.api[this.getOption("list") || 'list'](_self, data).then(res => {
            let _self1 = this;
            this.loading = false;
            let data = this.getRootData(res);
            // if (clientOption.res) {
            //     data = clientOption.res(res);
            // } else {
            //     data = res.data.data
            // }
            this.page.total = data[this.getPageInfo(page.pageTotal) || 'total'];
            this.data = data[this.getPageInfo(page.pageData) || 'data'];
            if (this.listAfter) {
              this.listAfter(data)
            } else {
              this.$message.success('获取成功')
            }
          })
        }
        if (this.config.domain) {
          if (this.listBefore) {
            this.listBefore()
          }
          callback();
        } else {
          console.warn(" config的配置域名还未加载完成 ... ")
        }
      },
      rowSave(row, done, loading) {
        const callback = () => {
          debugger;
          let _self = this;
          delete this.form.params;
          this.api[this.option.add || 'add'](_self, this.form).then((data) => {
            this.getList();
            if (this.addAfter) {
              this.addAfter(row, done, loading, data);
            } else {
              this.$message.success('新增成功');
              done(row, done, loading);
            }
          }).catch(() => {
            loading()
          })
        }
        if (this.addBefore) {
          this.addBefore(row, done, loading)
        }
        callback()
      },
      rowUpdate(row, index, done, loading) {
        if (!this.updateBefore(row, index, done, loading)) {
          loading();
          return;
        }
        const callback = () => {
          delete this.form.params;
          let _self = this;
          this.api[this.option.update || 'update'](_self, this.form).then((data) => {
            this.getList();
            if (this.updateAfter) {
              this.updateAfter(row, index, done, loading, data)
            } else {
              this.$message.success('更新成功');
              done()
            }
          }).catch(() => {
            loading()
          })
        }
        callback()
      },
      rowDel(row, index) {
        let _self = this;
        const callback = () => {
          this.api[this.option.del || 'del'](_self, row).then((data) => {
            this.getList();
            if (this.delAfter) {
              this.delAfter(data, row, index)
            } else {
              this.$message.success('删除成功')
            }
          })
        }
        if (this.delBefore) {
          this.delBefore(row, index)
          callback()
        } else {
          this.$confirm(`此操作将永久删除序号【${index}】的数据, 是否继续?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            callback()
          })
        }
      },
      searchChange(params, done) {
        if (done) done();
        this.params = params;
        this.page.currentPage = 1;
        this.getList();
      },
      refreshChange() {
        this.getList();
      },
      getOption(key) {
        return this.option[key] || clientOption[key];
      },
      getConfig(key, defaultValue) {
        return this.config[key] || defaultValue;
      },
      getPageInfo(key) {
        return this.page.info && this.page.info[key] || clientOption[key];
      }
      /**
       * 获取符合返回结果参数的配置
       * @param res
       * @returns {*}
       */
      , getRootData(res) {
        let root = this.getPageInfo(page.pageRoot);
        let rootResponse;
        if (root) {
          if (root.indexOf(".") > 0) {
            let fields = root.split(".");
            rootResponse = res[fields[0]];
            for (let i = 1; i < fields.length; i++) {
              rootResponse = rootResponse[fields[i]];
            }
          } else {
            rootResponse = res[root];
          }
        } else {
          rootResponse = clientOption.res(res);
        }
        return rootResponse;
      }
    }
  }
  app.mixins = app.mixins || [];
  app.mixins.push(mixins)
  return app;
}

/**
 * 原配版本 - crud 全局配置
 * 具体使用参考{@link https://avuejs.com/docs/api-crud-fun.html}
 *
 * @param app
 * @param option
 * @returns {{ref: string, data: [], tableLoading: boolean, option: *}|{"on-load": mixins.methods.getList, "refresh-change": mixins.methods.refreshChange, "row-update": mixins.methods.rowUpdate, "row-save": mixins.methods.rowSave, "row-del": mixins.methods.rowDel, "search-reset": mixins.methods.searchChange, "search-change": mixins.methods.searchChange}|{mixins}|*|string|{data: [], form: {}, api: any, page: {}, params: {}, loading: boolean}}
 */
export default (app, option = {}) => {
  let mixins = {
    data() {
      return {
        data: [],
        form: {},
        params: {},
        api: require(`@/api/${option.name}`),
        loading: false,
        page: {},
      }
    },
    computed: {
      option() {
        return require(`@/option/${option.name}`).default(this)
      },
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
        return option.rowKey || 'id'
      }
    },
    methods: {
      getList() {
        const callback = () => {
          debugger;
          this.loading = true;
          let pageParams = {}
          pageParams[option.pageNumber || 'pageNumber'] = this.page.currentPage
          pageParams[option.pageSize || 'pageSize'] = this.page.pageSize
          const data = Object.assign(pageParams, this.params)
          this.data = [];
          this.api[option.list || 'list'](data).then(res => {
            this.loading = false;
            let data;
            if (option.res) {
              data = option.res(res);
            } else {
              data = res.data
            }
            this.page.total = data[option.total || 'total'];
            const result = data[option.data || 'data'];
            this.data = result;
            if (this.listAfter) {
              this.listAfter(data)
            } else {
              this.$message.success('获取成功')
            }
          })
        }
        if (this.listBefore) {
          this.listBefore()
        }
        callback()
      },
      rowSave(row, done, loading) {
        const callback = () => {
          delete this.form.params;
          this.api[option.add || 'add'](this.form).then((data) => {
            this.getList();
            if (this.addAfter) {
              this.addAfter(data)
            } else {
              this.$message.success('新增成功')
            }
            done();
          }).catch(() => {
            loading()
          })
        }
        if (this.addBefore) {
          this.addBefore()
        }
        callback()
      },
      rowUpdate(row, index, done, loading) {
        const callback = () => {
          debugger;
          delete this.form.params;
          this.api[option.update || 'update'](this.form).then((data) => {
            this.getList();
            if (this.updateAfter) {
              this.updateAfter(data)
            } else {
              this.$message.success('更新成功')
            }
            done()
          }).catch(() => {
            loading()
          })
        }
        if (this.updateBefore) {
          this.updateBefore()
        }
        callback()
      },
      rowDel(row, index) {
        const callback = () => {
          this.api[option.del || 'del'](row[this.rowKey], row).then((data) => {
            this.getList();
            if (this.delAfter) {
              this.delAfter(data, row, index)
            } else {
              this.$message.success('删除成功')
            }
          })
        }
        if (this.delBefore) {
          this.delBefore()
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
      }
    }
  }
  app.mixins = app.mixins || [];
  app.mixins.push(mixins)
  return app;
}

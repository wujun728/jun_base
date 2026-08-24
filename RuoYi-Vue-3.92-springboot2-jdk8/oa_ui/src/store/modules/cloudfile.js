
const state = {
    show:false,
    fileList:[],
    index:0,
    openUpload:false,

}
const mutations = {
    setShow: (state, show) => {
        state.show = show
      },
    setFlieList: (state, list) => {
        state.fileList = list
      },
    setIndex: (state, index) => {
        state.index = index
      },
    setStart: (state, val) => {
   
        state.openUpload = val
      },

      
}

export default {
    namespaced: true,
    state,
    mutations,
 
  }
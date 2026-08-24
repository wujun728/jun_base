import { downloadUrlApi } from '@/api/public';
import {  getFileExtension } from '@/libs/public'
import { roterPre } from '@/settings'
const base = {};
base.install = function(Vue, options) {
  // 判断文件类型
  Vue.prototype.getFileType = function(type, fileUrl) {
    var icon = '';
    if (type == 1) {
      icon = 'iconwenjianjia color-file';
    } else {
      if(!fileUrl) return false
      var fileType = fileUrl.substr(fileUrl.lastIndexOf('.') + 1);
  
      let typeObj={
        mp3:'iconMP3geshi mp3',
        mp4:'iconMP4geshi mp4 ',
        svg:'iconSVGgeshi svg ',
        ppt:'iconPPTgeshi ppt',
        pptx:'iconPPTgeshi ppt',
        xls:'iconExcelgeshi xls',
        xlsx:'iconExcelgeshi xls',
        xlsm:'iconExcelgeshi xls',
        pdf:'iconPDFgeshi pdf',
        png:'iconPNGgeshi png',
        jpeg:'iconPDFgeshi pdf',
        jpg:'iconJPGgeshi jpg',
        PNG:'iconPDFgeshi png',
        xmind:'iconXmindgeshi pdf',
        doc:'iconwordgeshi doc',
        docx:'iconwordgeshi doc',
        ai:'iconAIgeshi ppt',
        sketch:'iconSketchgeshi tif',
        ps:'iconPSgeshi gif',
        gif:'iconGIFgeshi gif',
        tif:'iconTIFgeshi tif',
        zip:'iconZipgeshi zip',
        xd:'iconXDgeshi svg',
        md:'iconXmindgeshi tif',
        eps:'iconAIgeshi ppt',
      }
      if (typeObj[fileType]) {
        icon = typeObj[fileType]||'iconwendanggeshi word';
      }
    }
    return icon;
  };
  

 /**
  * 全局打开预览文件方法
  * @param {*} item 文件信息
  * @param {*} spaceId 云盘文件的空间id
  */

  Vue.prototype.filePreview = function(item,spaceId,type) {
    const imgType = ['png', 'jpeg', 'gif', 'jpg', 'webp','bmp', 'svg', 'mp3', 'mp4', 'rmvb', 'avi', 'mov', 'wmv']
    const wordType = ['doc', 'docx','txt','xls','xlsx','ppt','pptx','pdf','html','md', 'xmind']  
    // 云盘文件打开
    if(type==='cloud'){
      let fid = item.pid || spaceId
      const fileType = item.file_ext
      const isImage = imgType.includes(fileType)
      // 打开word类型文件
      if (wordType.includes(fileType)) {
        let url = `${roterPre}/openFile?id=${item.id}&&fid=${item.pid || this.spaceId}`
        window.open(url, '_blank')
      } else if (isImage) {
        // 打开图片,音频，视频类型文件
        this.$refs.viewFile.openFile(fid, item.id)
      } else {
        this.fileDownLoad(item)
      }
      
    } else {
      // 其他项目打开
      const fileType = getFileExtension(item.name)
      const isImage = imgType.includes(fileType)
      if (!isImage&&wordType.includes(fileType)) {
        let url = `${roterPre}/openFile?id=${item.id}`
        window.open(url, '_blank')
      } else if (isImage) {
        this.$refs.viewFile.openFile(0, item.id)
      } else {
        this.fileLinkDownLoad(item.url||item.att_dir, item.name)
      }
    }
  };
  

  // 文件下载
  Vue.prototype.fileDownLoad = function(item) {
    downloadUrlApi({
      file_id: item.file_sn,
      type: 'folder'
    })
      .then((res) => {
        window.open(res.data.download_url, '_self');
      })
      .catch((error) => {
        this.$message.error(error.message);
      });
  };

  
  // 文件下载
  Vue.prototype.fileLinkDownLoad = function(url, name) {
    var urlPath = url;
    const link = document.createElement('a');
    link.style.display = 'none';
    link.href = urlPath;
    link.download = name;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };
  

  // 去除左右空格
  Vue.prototype.trim = function(s) {
    return s.replace(/(^\s*)|(\s*$)/g, '');
  };


  // 根据排序id获取名称
  Vue.prototype.fileSortName = function(id) {
    var icon = '';
    const isIcon = {
    1:'file_name',
    2:'created_at',
    3:'updated_at',
    4:'file_size',
    5:'deleted_at',
    6:'asc',
    }
    icon = isIcon[id]||'desc'
    return icon;
  };


  // 根据类型id获取名称
  Vue.prototype.fileTypeName = function(id) {
    var icon = '';
    const isIcon = {
      1:'',
      2:'word',
      3:'ppt',
      4:'image',
      5:'excel'
      }
    icon = isIcon[id]
    
    return icon;
  };


  // 判断图片类型 返回值：true/false
  Vue.prototype.fileIsImage = function(type) {
    return ['image/png','image/jpg','image/webp','image/jpeg','image/bmp'].includes(type)
  };


  // 获取文件后缀
  Vue.prototype.getFileNameTypes = function(url) {
    let str = '';
    if (url.lastIndexOf('.') > -1) {
      str = url.substr(url.lastIndexOf('.') + 1);
    }
    return str;
  };
};

export default base;

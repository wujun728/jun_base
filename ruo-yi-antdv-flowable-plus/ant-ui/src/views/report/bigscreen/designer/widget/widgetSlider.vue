<template>
  <superslide :options="options" class="slideBox">
    <!-- slides -->
    <div class="bd">
      <ul>
        <li>
          <img src="https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=717294809,2494697366&fm=26&gp=0.jpg" alt="">
        </li>
        <li>
          <img src="https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2090090414,3038344648&fm=26&gp=0.jpg" alt="">
        </li>
      </ul>
    </div>
    <!-- Optional controls slots -->
    <!-- slot="titCell" -->
    <div slot="titCell" class="hd">
      <ul>
        <li class="on">1</li>
        <li class="">2</li>
        <li class="">3</li>
      </ul>
    </div>
    <!-- slot="prev" -->
    <a slot="prev" class="prev" href="javascript:void(0)" />
    <!-- slot="next" -->
    <a slot="next" class="next" href="javascript:void(0)" />
    <!-- slot="pageStateCell" -->
    <span slot="pageStateCell" class="pageState" />
  </superslide>
</template>
<script>
export default {
  name: 'WidgetSlider',
  components: {},
  data() {
    return {
      options: {
        mainCell: '.bd ul',
        autoPlay: true,
        effect: 'leftLoop',
      },
    }
  },
  mounted() {},
  methods: {},
}
</script>

<style scoped lang="scss">
.slideBox {
  width: 450px;
  height: 230px;
  overflow: hidden;
  position: relative;
}
.slideBox .hd {
  height: 15px;
  overflow: hidden;
  position: absolute;
  right: 5px;
  bottom: 5px;
  z-index: 1;
}
.slideBox .hd ul {
  overflow: hidden;
  zoom: 1;
  float: left;
}
.slideBox .hd ul li {
  float: left;
  margin-right: 2px;
  width: 15px;
  height: 15px;
  line-height: 14px;
  text-align: center;
  background: #fff;
  cursor: pointer;
}
.slideBox .hd ul li.on {
  background: #f00;
  color: #fff;
}
.slideBox .bd {
  position: relative;
  height: 100%;
  z-index: 0;
}
.slideBox .bd li {
  zoom: 1;
  vertical-align: middle;
}
.slideBox .bd img {
  width: 450px;
  height: 230px;
  display: block;
}
/* 下面是前/后按钮代码，如果不需要删除即可 */
.slideBox .prev,
.slideBox .next {
  position: absolute;
  left: 3%;
  top: 50%;
  margin-top: -25px;
  display: block;
  width: 32px;
  height: 40px;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOsAAAAgCAMAAADE+3+QAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MkYyNjE4RUVDRjgxMTFFMjk1ODhDQkIzNDg3MjdGNUEiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MkYyNjE4RUZDRjgxMTFFMjk1ODhDQkIzNDg3MjdGNUEiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoyRjI2MThFQ0NGODExMUUyOTU4OENCQjM0ODcyN0Y1QSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoyRjI2MThFRENGODExMUUyOTU4OENCQjM0ODcyN0Y1QSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PmLDzTcAAAAJUExURcHBwf///////wCiYf4AAAADdFJOU///ANfKDUEAAACjSURBVHja5JnRDUQhDMNM9h/6STfCgeKPsIBjhGgpnHPy7wLyYLUicC5IPJGtJfi5ngsSL1Q7AYgsW8QTV7YJJ6psFU1M2S6YiLJlLPFk21CiydaRaOT+7mKxhYOERDcuCBy+cheiJHBqHEYGqZzPug6d4aG7aajmDPUSQz3iUO8/9KYbeqsPzWCGZmtDM9OhWfjSH8fY31UuSMkT2UqET4ABAHo9NyVXKF5RAAAAAElFTkSuQmCC) -110px
    5px no-repeat;
  filter: alpha(opacity=50);
  opacity: 0.5;
}
.slideBox .next {
  left: auto;
  right: 3%;
  background-position: 8px 5px;
}
.slideBox .prev:hover,
.slideBox .next:hover {
  filter: alpha(opacity=100);
  opacity: 1;
}
.slideBox .prevStop {
  display: none;
}
.slideBox .nextStop {
  display: none;
}
</style>

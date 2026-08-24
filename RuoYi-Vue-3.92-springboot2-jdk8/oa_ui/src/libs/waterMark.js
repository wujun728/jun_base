// 全局水印画布
let watermark = {}
let setWatermark = (str1,str2,type) => {

  let id = '1.23452384164.123412416';
  if (document.getElementById(id) !== null) {
    document.body.removeChild(document.getElementById(id));
  }

  //创建一个画布
  let can = document.createElement('canvas');
  //设置画布的长宽
  can.width = 350;
  can.height = 250;
  const ctx  = can.getContext('2d');

  //旋转角度
  ctx.rotate(343.53 * Math.PI / 180);
  ctx.font = '14px Vedana'

  //设置填充绘画的颜色、渐变或者模式
  ctx.fillStyle = '#666666';
  ctx.fontWeight = '400';
  //设置文本内容的当前对齐方式
  ctx.textAlign = 'left';
  //设置在绘制文本时使用的当前文本基线
  ctx.textBaseline = 'top'
  //在画布上绘制填色的文本（输出的文本，开始绘制文本的X坐标位置，开始绘制文本的Y坐标位置）
  // ctx.fillText(str, can.width / 8, can.height / 2);

  ctx.fillText(str1, 0, 40) // 水印在画布的位置x，y轴
  ctx.fillText(str2, 120, 220)

  let div = document.createElement('div');
  div.id = id;
  div.style.pointerEvents = 'none';
  div.style.top = '66px';
  div.style.left = '240px';
  div.style.position = 'fixed';
  div.style.zIndex = '100000';
  div.style.opacity = '0.1'
  div.style.width = document.documentElement.clientWidth + 'px';
  div.style.height = document.documentElement.clientHeight + 'px';
  if(type==='close') {
    div.style.background = '';
  } else {
    div.style.background = 'url(' + can.toDataURL('image/png') + ') left  repeat';
  }

  document.body.appendChild(div);
  return id;
}

// 该方法只允许调用一次
watermark.set = (str1,  str2,type) => {
  let id = setWatermark(str1,  str2,type);
  setInterval(() => {
    if (document.getElementById(id) === null) {
      id = setWatermark(str1,  str2,type);
    }
  }, 500);
  window.onresize = () => {
    setWatermark(str1,  str2,type);
  };
}

export default watermark;

import warterMark from '@/libs/waterMark';
import router from '@/router';
import store from '@/store';



const getWaterMarkText = () => {
  const userInfo = store.getters.userInfo;
  let name = ''
  if (userInfo && userInfo.name) {
    if (userInfo.job) {
      name = userInfo.name + '' + '(' + userInfo.job.name + ')'
    } else {
      name = userInfo.name
    }
  }

  return name;
}

router.afterEach((item) => {
  const name = getWaterMarkText();
  if (item.path !== '/admin/login' && localStorage.getItem('isWebConfig') == 1) {
    warterMark.set(name, name)
  } else {
    warterMark.set(name, name, 'close')
  }
})
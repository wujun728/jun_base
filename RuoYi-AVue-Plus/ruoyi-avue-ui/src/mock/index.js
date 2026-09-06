import server from './serverApi'
import curd from './crudApi'

export default ({mock}) => {
// 开启mock数据
  console.log("是否开启MOCK : ", mock)

  server({mock});
  curd({mock});
}


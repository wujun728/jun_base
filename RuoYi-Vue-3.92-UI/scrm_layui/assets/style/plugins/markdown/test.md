## 查询用户所有空调运行数据

**接口地址** `/DKAR/api/airDataByUser`


**请求方式** `GET`


**consumes** ``


**produces** `["application/json"]`


**接口描述** ``

**请求参数**

| 参数名称      | 参数说明   |   请求类型 |  是否必须      |  数据类型   |  schema  |
| ------------ | ----------|----------|------------|--------|-----|
| customName   | 客户名称   |     query        |       true      | string   |      |
| everyDay     | 每天日期（yyyy-MM-dd）   |     query        |       false      | string   |      |
            





**响应状态**

| 状态码         | 说明                             |    schema                         |
| ------------ | -------------------------------- |---------------------- |
| 200         | OK                        |请求结果«List«空调数据集合对象»»                          |
| 401         | Unauthorized                        |                          |
| 403         | Forbidden                        |                          |
| 404         | Not Found                        |                          |




**响应参数**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| code     |错误代码，200成功，其他失败      |    string   |       |
| msg     |消息      |    string   |       |
| result     |对象      |    array   |   空调数据集合对象    |
| total     |总记录数      |    integer(int32)   |   integer(int32)    |
            



**schema属性说明**
  
**空调数据集合对象**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | ------------------|--------|----------- |
| data         |     空调数据集合      |  array   | 空调数据     |
| lcNo         |     lc No.      |  string   |      |
            

**空调数据**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | ------------------|--------|----------- |
| airName         |     名称 如 室外机1      |  string   |      |
| airnetAddress         |     AIRNET地址      |  string   |      |
| data         |     数据集合      |  array   | array     |
| deviceType         |     机型      |  string   |      |
| installAddress         |     安装地址      |  string   |      |
| machineCode         |     机器编号      |  string   |      |
            




**响应示例**


```json
{
	"code": "",
	"msg": "",
	"result": [
		{
			"data": [
				{
					"airName": "",
					"airnetAddress": "",
					"data": [],
					"deviceType": "",
					"installAddress": "",
					"machineCode": ""
				}
			],
			"lcNo": ""
		}
	],
	"total": 1
}
```



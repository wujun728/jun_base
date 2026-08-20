## ${api_title}

**接口地址**: &nbsp;&nbsp;`${api_path}`


**请求方式**: &nbsp;&nbsp;`${api_type}`


**请求数据类型**:&nbsp;&nbsp;`application/json`


**响应数据类型**:&nbsp;&nbsp;`application/json`


**接口描述**:&nbsp;&nbsp;  ${api_remark}

${paramsDemo}

**请求参数**

${api_params}

<!--
**执行SQL**

```
${api_sql}
```
-->

**响应状态**

| 状态码        | 说明                             |    schema                       |
| ------------ | -------------------------------- |---------------------- |
| 200         | OK                        |请求结果                          |
| 403         | 没有访问权限                     |                          |
| 404         | 未查到数据                        |                          |
| 500         | 服务器内部异常                      |                          |




**响应参数**

| 参数名称         | 参数说明                             |    类型 |  schema |
| ------------ | -------------------|-------|----------- |
| code     |返回代码，200成功，其他失败      |    integer(int32)   |   integer(int32)    |
| data     |响应数据      |    object   |       |
| message     |消息      |    string   |       |
| success     |	是否成功      |    boolean   |      |
| timestamp     |响应时间	      |   string   |          |


**响应示例**


```json
{
	"code": 200,
	"data": {},
	"message": "",
	"success": true,
	"timestamp": "2024-05-06 11:21:09"
}
```



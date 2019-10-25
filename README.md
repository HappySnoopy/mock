# MOCK项目
尝试mock一个系统的所有对外请求。使得一个系统（服务）可以独立部署。
- http
- dubbo/dubbox
- 数据库？
- redis？
- 其它？

## 基本思路
### http
从http请求中解析出必要的入参（uri、params、body、header等），根据这些条件从配置文件（数据库）中匹配出对应的返回结果，并发送回去。
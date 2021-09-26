# SmallQ  
## 功能介绍
模仿QQApp,自己制作出来的一个微型聊天软件


## 注意  
目前最新版本(即第13次提交)的注册功能是有bug的，(第12次及以前的提交都是能够在Android 11设备上正常运行的)在向服务端发送注册请求之后，会出现`E/libprocessgroup: set_timerslack_ns write failed: Operation not permitted`的报错，中文意思为”E/libprocessgroup:set\u Timer缺少写入失败：不允许操作“，通过搜索引擎得知这个错误常见于引入地图控件时。  
目前已知的是客户端与服务端的网络连接是正确的，但是由于服务端不是我设计的，因此不能排查服务端的错误，所以将此错误搁置，以便以后进行错误的排查和修改。

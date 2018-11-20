# Android架构重制版
随着app不断迭代，业务越多，每个模块业务代码也越来越复杂，采用传统单一架构模式，势必会影响开发和测试效率，很不方便进行多人并行开发和测试,组件化分层架构应宇而生。

### 基础层(librarys-base)
该层一般放置一些多项目通用基础库

1. 工具库common-util
2. UI库common-widget
3. 核心通用功能core-basics
4. H5容器h5-container(了解此库:<https://github.com/minyangcheng/fit-hybrid>)
5. Weex容器weex-container(了解此库:<https://github.com/minyangcheng/fit-we>)

### 业务层(librarys-business)
该层一般放置一些多项目通用业务库,能够单独打包进行测试

1. 订单order
2. 支付pay

### 主工程(app)
该层一般放置项目非通用性功能,页面流程组织代码


### 三端路由统一
路由采用开源库:<https://github.com/drakeet/Floo>
scheme://host/path?userId="121123"



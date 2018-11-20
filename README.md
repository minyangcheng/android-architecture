# Android架构重制版
随着app不断迭代，业务越多，每个模块业务代码也越来越复杂，采用传统单一架构模式，势必会影响开发和测试效率，很不方便进行多人并行开发和测试,组件化分层架构应孕而生。

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

### mvp
业务module和主工程采用mvp模式进行开发

### 三端路由统一
> 路由系统采用开源库:<https://github.com/drakeet/Floo>

定义跳转协议:scheme://host/path?params"
1. scheme采用公司名称拼音首写字母
2. host采用模块名称
3. path模块内部页面名称
4. params为传参

例如: cg://order/main?userId="123"&from="b" -> 跳转到order模块钟的main页面,参数userId="123"&from="b"

### 多人开发

1. 在多人进行具体业务开发的时候,设置gradle.properties文件中的`isBuildModule=true`,让业务层的module和主工程可以单独打包测试,这样不仅可以方便多人开发,也可以让打包速度加快.
2. 在需要测试主工程的时候,设置gradle.properties文件中的`isBuildModule=false`,即可测试整体项目.





# Android架构重制版
随着app不断迭代，业务越多，每个模块业务代码也越来越复杂，采用传统单一架构模式，势必会影响开发和测试效率，很不方便进行多人并行开发和测试,组件化分层架构应孕而生。

### 基础层(librarys-base)
该层一般放置一些多项目通用基础库

1. 工具库common-util
2. UI库common-widget
3. 核心通用功能core-basics,包含一些通用基类,数据加载类等
4. H5容器h5-container(了解此库:<https://github.com/minyangcheng/fit-hybrid>)
5. Weex容器weex-container(了解此库:<https://github.com/minyangcheng/fit-we>)

### 业务层(librarys-business)
该层一般放置一些多项目通用业务库,能够单独打包进行测试

1. 订单order库
2. 支付pay库

### 主工程(app)
该层一般放置项目非通用性功能,页面流程组织代码

### MVP
业务module和主工程采用mvp模式进行开发

### 三端路由统一

> 有些模块适合用native书写,如视频聊天界面;有些适合用weex写,如一些订单列表\发起订单界面界面;有些适合用h5书写,如一些广告\协议\推广界面

定义跳转协议:`scheme://host/path?params`
* scheme表示公司名称拼音首写字母组合

* host表示在哪一端区寻找路由对应的页面
1. native.com:打开native端路由对应的页面
2. h5.com:打开h5端对应的页面
3. weex.com:打开weex端路由对应的页面
4. auto.com:优先在weex端寻找对应页面,找不到再去native端寻找,找不到在h5端打开页面

* path模块内部页面名称,当native\h5\weex端路径相同的时候,即可动态替换界面

* params为传参

例如: 
1. `cg://native.com/order/MainPage?userId="123"&from="b"` ---> 跳转到native端order模块中的MainPage页面,参数userId="123"&from="b"
2. `cg://weex.com/main/SamplePage?userId="123"&from="b"` ---> 跳转到weex端main文件夹下的SapmlePage页面,参数userId="123"&from="b"
3. `cg://h5.com/main/SamplePage?userId="123"&from="b"&host="www.cg.com"` ---> 在h5端打开`http://www.cg.com/main/SamplePage?userId="123"&from="b"`页面
4. `cg://auto.com/main/SamplePage?userId="123"&from="b"` ---> 优先在weex端寻找对应页面,找不到再去native端寻找,找不到在h5端打开页面,参数userId="123"&from="b"

### 多人开发

1. 在多人进行具体业务开发的时候,设置gradle.properties文件中的`isBuildModule=true`,让业务层的module和主工程可以单独打包测试,这样不仅可以方便多人开发,也可以让打包速度加快.
2. 在需要测试主工程的时候,设置gradle.properties文件中的`isBuildModule=false`,即可测试整体项目.

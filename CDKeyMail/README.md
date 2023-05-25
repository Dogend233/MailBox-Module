# CDKeyMail
兑换码邮件: 用于玩家使用兑换码获取一封邮件  
  
## 介绍
邮件独有属性repeat：一个人是否可以用不同兑换码多次领取此邮件  
一次性CDK：只能被使用一次  
可复用CDK：每一个人都可以使用一次  
  
## 变量
mailbox_player_cdkey_input_day 玩家今日输错CDK的次数
server_cdkey_input_every_day 每天最多可输错CDK的次数
server_cdkey_input_every_times 两次输入CDK的间隔时间
  
### 前置模块
[公共界面API - CommonGUIAPI](./CommonGUIAPI)  
[邮件变量 - MailPlaceholder](./MailPlaceholder)  
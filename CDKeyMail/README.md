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
[单独玩家邮件API - SinglePlayerMailAPI](../SinglePlayerMailAPI)  
[公共界面API - CommonGUIAPI](../CommonGUIAPI)  
[邮件变量 - MailPlaceholder](../MailPlaceholder)  
  
### v1.1.0  
1.修复领取多个相同CDK邮件出现错误的问题，需要删除以前所有personmail表里的cdkey邮件

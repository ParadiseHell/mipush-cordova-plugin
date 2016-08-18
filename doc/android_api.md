# Android API说明

- [注册小米推送](#注册小米推送)
- [注册成功时获取的内容](#注册成功时获取的内容)
- [收到消息时获取的内容](#收到消息时获取的内容)
- [点击消息时获取的内容](#点击消息时获取的内容)
- [设置Alias](#设置Alias)
- [注销Alias](#注销Alias)
- [设置userAccount](#设置userAccount)
- [注销userAccount](#注销userAccount)
- [订阅Topic](#订阅Topic)
- [取消Topic](#取消Topic)
		
## 注册小米推送

### API - init

用于小米注册，在 **deviceready** 事件触发时调用

#### 接口定义

		window.plugins.MiPushPlugin.init()

## 注册成功时获取的内容

- regId:
	window.plugins.MiPushPlugin.receiveRegisterResult.regId

#### 获取方法

在自己的js代码中注册 **mipush.receiveRegisterResult** 事件(可以参考sample中的index.js)
	
	document.addEventListener("mipush.receiveRegisterResult", Your_Receive_Register_Function, false);

#### 自定义注册回调方法说明
	
	Your_Receive_Register_Function(data)

#### 参数说明

- data(json字符串)

		regId = data.regId

## 收到消息时获取的内容

- title(标题):
	window.plugins.MiPushPlugin.notificationMessageArrived.title
- description(消息摘要):
	window.plugins.MiPushPlugin.notificationMessageArrived.description
- your_key(附加字段，一般由后台定义)
	window.plugins.MiPushPlugin.notificationMessageArrived.your_key

#### 获取方法

在自己的js代码中注册 **mipush.notificationMessageArrived** 事件(可以参考sample中的index.js)
	
	document.addEventListener("mipush.notificationMessageArrived", Your_Notification_Message_Arrived_Function, false);

#### 自定义接受消息回调方法说明
	
	Your_Notification_Message_Arrived_Function(data)

#### 参数说明

- data(json字符串)

		title = data.title
		description = data.description
		your_key = data.your_key

## 点击消息时获取的内容

- title(标题):
	window.plugins.MiPushPlugin.notificationMessageClicked.title
- description(消息摘要):
	window.plugins.MiPushPlugin.notificationMessageClicked.description
- your_key(附加字段，一般由后台定义)
	window.plugins.MiPushPlugin.notificationMessageClicked.your_key

#### 获取方法

在自己的js代码中注册 **mipush.notificationMessageClicked** 事件(可以参考sample中的index.js)
	
	document.addEventListener("mipush.notificationMessageClicked", Your_Notification_Message_Clicked_Function, false);

#### 自定义点击消息回调方法说明
	
	Your_Notification_Message_Clicked_Function(data)

#### 参数说明

- data(json字符串)

		title = data.title
		description = data.description
		your_key = data.your_key

## 设置Alias

### API - setAlias

#### 接口定义
		
		window.plugins.MiPushPlugin.setAlias(alias)

#### 参数说明

- alias:设置Alias的值(字符串)

## 注销Alias

### API - unSetAlias

#### 接口定义
		
		window.plugins.MiPushPlugin.unSetAlias(alias)

#### 参数说明

- alias:注销Alias的值(字符串)

## 设置userAccount

### API - setUserAccount

#### 接口定义
		
		window.plugins.MiPushPlugin.setUserAccount(userAccount)

#### 参数说明

- userAccount:设置userAccount的值(字符串)

## 注销userAccount

### API - unSetAlias

#### 接口定义
		
		window.plugins.MiPushPlugin.unSetUserAccount(userAccount)

#### 参数说明

- userAccount:注销userAccount的值(字符串)

## 订阅Topic

### API - setTopic

#### 接口定义
		
		window.plugins.MiPushPlugin.setTopic(topic)

#### 参数说明

- topic:订阅Topic的值(字符串)

## 取消Topic

### API - unSetAlias

#### 接口定义
		
		window.plugins.MiPushPlugin.unSetTopic(topic)

#### 参数说明

- topic:取消Topic的值(字符串)
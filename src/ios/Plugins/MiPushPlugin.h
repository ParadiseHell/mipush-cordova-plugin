//
//  Created by wenin819 on 17-03-24.
//

#import <Cordova/CDV.h>

@interface MiPushPlugin : CDVPlugin

// 注册小米推送
-(void)init:(CDVInvokedUrlCommand*)command;
-(void)shouldInit:(CDVInvokedUrlCommand*)command;

-(void)showToast:(CDVInvokedUrlCommand*)command;

// 设置标签、别名
-(void)setAlias:(CDVInvokedUrlCommand*)command;
-(void)unSetAlias:(CDVInvokedUrlCommand*)command;

// 设置userAccount
-(void)setUserAccount:(CDVInvokedUrlCommand*)command;
-(void)unSetUserAccount:(CDVInvokedUrlCommand*)command;


// 订阅topic
-(void)setTopic:(CDVInvokedUrlCommand*)command;
-(void)unSetTopic:(CDVInvokedUrlCommand*)command;

// 接受到消息
+(void)onNotificationMessageArrivedCallBack:(NSDictionary*)data;
// 用户点击
+(void)onNotificationMessageClickedCallBack:(NSDictionary*)data;
// 小米推送注册成功
+(void)onReceiveRegisterResultCallBack:(NSString*)regId;

+ (void)callbackWithType:(NSString *)type data:(NSDictionary *)data;
@end

MiPushPlugin *SharedMiPushPlugin;
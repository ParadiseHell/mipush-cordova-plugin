//
//  Created by wenin819 on 17-03-24.
//

#import "MiPushPlugin.h"
#import "MiPushSDK.h"

@implementation MiPushPlugin

static BOOL hasInit = FALSE;
static NSMutableArray *callbackJsQueue = nil;

- (void)pluginInitialize {
    [super pluginInitialize];
    SharedMiPushPlugin = self;
}

// 注册小米推送
- (void)init:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSString *regId = [MiPushSDK getRegId];
        if(regId) {
            hasInit = TRUE;
            [MiPushPlugin onReceiveRegisterResultCallBack:regId];
            [self handleCallbackJsQueue];
            [self handleResultWithValue:regId command:command];
        }
    }];
}

- (void)shouldInit:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        [self handleResultWithValue:@0 command:command];
    }];
}

- (void)showToast:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        [self handleResultWithValue:@1 command:command];
    }];
}

//以下为js中可调用接口
//设置标签、别名
- (void)setAlias:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *alias = [command argumentAtIndex:0];
            [MiPushSDK setAlias:alias];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}

- (void)unSetAlias:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *alias = [command argumentAtIndex:0];
            [MiPushSDK unsetAlias:alias];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}

- (void)setUserAccount:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *account = [command argumentAtIndex:0];
            [MiPushSDK setAccount:account];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}

- (void)unSetUserAccount:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *account = [command argumentAtIndex:0];
            [MiPushSDK unsetAccount:account];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}


// 订阅topic
- (void)setTopic:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *topic = [command argumentAtIndex:0];
            [MiPushSDK subscribe:topic];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}

- (void)unSetTopic:(CDVInvokedUrlCommand *)command {
    [self.commandDelegate runInBackground:^{
        NSArray *args = [command arguments];
        if (args.count > 0) {
            NSString *topic = [command argumentAtIndex:0];
            [MiPushSDK unsubscribe:topic];
            [self handleResultWithValue:@1 command:command];
        }
    }];
}

// 接受到消息
+ (void)onNotificationMessageArrivedCallBack:(NSDictionary *)data {
    [self callbackWithType: @"notificationMessageArrived" data:data];
}

// 用户点击
+ (void)onNotificationMessageClickedCallBack:(NSDictionary *)data {
    [self callbackWithType: @"notificationMessageClicked" data:data];
}

// 小米推送注册成功
+ (void)onReceiveRegisterResultCallBack:(NSString *)regId {
    NSLog(@"regid = %@", regId);
    [self callbackWithType: @"receiveRegisterResult" data:@{@"regId": regId}];
}

+ (void)callbackWithType: (NSString *) type data:(NSDictionary *)data {
    NSString *json = nil;
    NSError *error = nil;

    if(data[@"aps"]) {
        NSDictionary *alertData = data[@"aps"][@"alert"];
        if(alertData) {
            NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] initWithDictionary:data];
            [dictionary addEntriesFromDictionary:alertData];
            data = dictionary;
        }
    }

    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];

    if (error != nil) {
        NSLog(@"NSDictionary JSONString error: %@", [error localizedDescription]);
    } else {
        json = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    NSString *js = [NSString stringWithFormat:@"cordova.fireDocumentEvent('mipush.%@',%@)", type, json];

    if (SharedMiPushPlugin && hasInit) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [SharedMiPushPlugin.commandDelegate evalJs:js];
        });
    } else {
        if(callbackJsQueue == nil) {
            callbackJsQueue = [[NSMutableArray alloc] init];
        }
        [callbackJsQueue addObject:js];
    }
}

- (void)handleCallbackJsQueue {
    if(!hasInit || callbackJsQueue == nil) {
                return;
            }
    for (NSString *js in callbackJsQueue) {
        NSLog(@"handleCallbackJsQueue js = %@", js);
            dispatch_async(dispatch_get_main_queue(), ^{
            NSLog(@"handleCallbackJsQueue dispatch_async js = %@", js);
            [self.commandDelegate evalJs:js];
            });
    }
    [callbackJsQueue removeAllObjects];
}

- (void)handleResultWithValue:(id)value command:(CDVInvokedUrlCommand *)command {
    CDVPluginResult *result = nil;
    CDVCommandStatus status = CDVCommandStatus_OK;

    if ([value isKindOfClass:[NSString class]]) {
        value = [value stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    } else if ([value isKindOfClass:[NSNull class]]) {
        value = nil;
    }

    if ([value isKindOfClass:[NSObject class]]) {
        result = [CDVPluginResult resultWithStatus:status messageAsString:value];//NSObject 类型都可以
    } else {
        NSLog(@"Cordova callback block returned unrecognized type: %@", NSStringFromClass([value class]));
        result = nil;
    }

    if (!result) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

@end

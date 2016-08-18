var app = {
    initialize: function() {
        this.bindEvents();
    },
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        document.addEventListener("mipush.notificationMessageArrived", this.onNotificationMessageArrived, false);
        document.addEventListener("mipush.notificationMessageClicked", this.onNotificationMessageClicked, false);
        document.addEventListener("mipush.receiveRegisterResult", this.onReceiveRegisterResult, false);
    },
    onDeviceReady: function() {
        console.log('onDeviceReady:---------------');
        app.initiateUI();
    },
    initiateUI : function() {
        try {
            console.log("initiateUI");
            window.plugins.MiPushPlugin.init();
        } catch (exception) {
            console.log("initiateUI-----exception:"+exception);
        }
    },

    onNotificationMessageClicked : function(data) {
        try {
            var title = data.title;
            var description = data.description;
            console.log("onNotificationMessageClicked---------"+"-title-"+title+"-description-"+description);
            app.displayClick(data);
        } catch (exception) {
             console.log("onNotificationMessageClicked------exception:"+exception);
        }
    },

    onNotificationMessageArrived : function(data) {
        try {
            var title = data.title;
            var description = data.description;
            console.log("onNotificationMessageArrived---------"+"-title-"+title+"-description-"+description);
            app.displayReciever(data);
        } catch (exception) {
           console.log("onNotificationMessageArrived------exception:"+exception);
        }
    },
    onReceiveRegisterResult : function(data) {
        try {
            console.log("onReceiveRegisterResult---------"+data.regId);
            app.displayRegister(data);
        } catch (exception) {
            console.log("onReceiveRegisterResult------exception:"+exception);
        }
    },
    displayRegister : function(data){
        $("#register").find('p').eq(0).html(data.regId);
    },
    displayReciever : function(data){
        $("#reciecer").find('p').eq(0).html(data.title);
        $("#reciecer").find('p').eq(1).html(data.description);
    },
    displayClick : function(data){
        $("#click").find('p').eq(0).html(data.title);
        $("#click").find('p').eq(1).html(data.description);
    }
};

app.initialize();
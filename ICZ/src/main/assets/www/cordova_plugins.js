cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.comall.nnclick/www/nnclick.js",
        "id": "com.comall.nnclick.nnclick",
        "clobbers": [
            "nnclick"
        ]
    },
    {
        "file": "plugins/com.ionic.keyboard/www/keyboard.js",
        "id": "com.ionic.keyboard.keyboard",
        "clobbers": [
            "cordova.plugins.Keyboard"
        ]
    },
    {
        "file": "plugins/org.apache.cordova.device/www/device.js",
        "id": "org.apache.cordova.device.device",
        "clobbers": [
            "device"
        ]
    },
    {
        "file": "plugins/org.apache.cordova.inappbrowser/www/inappbrowser.js",
        "id": "org.apache.cordova.inappbrowser.inappbrowser",
        "clobbers": [
            "window.open"
        ]
    },
    {
        "file": "plugins/org.apache.cordova.splashscreen/www/splashscreen.js",
        "id": "org.apache.cordova.splashscreen.SplashScreen",
        "clobbers": [
            "navigator.splashscreen"
        ]
    },
    {
        "file": "plugins/org.apache.cordova.statusbar/www/statusbar.js",
        "id": "org.apache.cordova.statusbar.statusbar",
        "clobbers": [
            "window.StatusBar"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "com.comall.nnclick": "1.0.0",
    "com.ionic.keyboard": "1.0.3",
    "org.apache.cordova.console": "0.2.12",
    "org.apache.cordova.device": "0.2.13",
    "org.apache.cordova.inappbrowser": "0.5.4",
    "org.apache.cordova.splashscreen": "0.3.5",
    "org.apache.cordova.statusbar": "0.1.9"
}
// BOTTOM OF METADATA
});
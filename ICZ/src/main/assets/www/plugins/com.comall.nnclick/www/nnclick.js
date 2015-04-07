cordova.define("com.comall.nnclick.nnclick", function(require, exports, module) { /**
 * android 99click监测插件
 */
var exec = require('cordova/exec');

var nnclick = function() {
};

/**
 * 统计应用开启
 */
nnclick.countAppOpen = function(version, channel, param) {
    exec(null, null, "NnclickPlugin", "countAppOpen", [version, channel, param || '']);
};

/**
 * 统计页面访问
 */
nnclick.countView = function(title, param) {
    exec(null, null, "NnclickPlugin", "countView", [title, param || '']);
};

/**
 * 统计应用内点击
 */
nnclick.countClick = function(title, target, param) {
    exec(null, null, "NnclickPlugin", "countClick", [title, target, param || '']);
};

module.exports = nnclick;




});

cordova.define("cordova/plugins/Pgalipay", 
  function(require, exports, module) {
    var exec = require("cordova/exec");
    var Pgalipay = function() {};
     //----------------------------//µÿ÷∑≤È—Ø---------------------------------------
	  Pgalipay.prototype.alipay = function(out_trade_no,subject,bodtxt,total_fee,url,successCallback, errorCallback) {
        if (errorCallback == null) { errorCallback = function() {}}
    
        if (typeof errorCallback != "function")  {
            console.log("pgbaidumap.scan failure: failure parameter not a function");
            return
        }
    
        if (typeof successCallback != "function") {
            console.log("pgbaidumap.scan failure: success callback parameter must be a function");
            return
        }
    
        exec(successCallback, errorCallback, 'Pgalipay', 'alipay', [out_trade_no,subject,bodtxt,total_fee,url]);
    };
	
    var Pgalipay = new Pgalipay();
    module.exports = Pgalipay;

});

  
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.Pgalipay) {
    window.plugins.Pgalipay = cordova.require("cordova/plugins/Pgalipay");
}

 
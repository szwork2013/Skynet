/**
 * 支付相关业务
 */
angular.module('app.services').factory('payService', function($q, apiService) {

    var payWap = function (url, deffered) {
        window.embed = window.open(url, '_blank', 'location=no,enableViewportScale=no,suppressesIncrementalRendering=yes,closebuttoncaption=返回,disallowoverscroll=yes');
        window.embed.addEventListener('loadstart', function(event) {
            var url = event.url;
            if (/.*\/app\/paysuccess/.test(url)) {
                window.embed.close();
                deffered.resolve();
            }
            if (/.*\/app\/payerror/.test(url)) {
                window.embed.close();
                var index = url.indexOf('?msg=');
                deffered.reject(decodeURIComponent(url.substr(index + 5)));
            }
        });
        window.embed.addEventListener('exit', function(event) {
            deffered.reject();
            window.embed = undefined;
        });
    };

    var payService = {
    
        /**
         * 支付宝wap支付
         * @param orderId {int} 订单 ID
         */
        alipayWap: function(orderId) {
        
            var deffered = $q.defer();

            apiService.get('/wappay', {
                oid: orderId
            }).success(function(data) {
                payWap(data.url, deffered);
            }).error(function(data) {
                deffered.reject(data.error.text);
            });

            return deffered.promise;
        },
        
        /**
         * 银联wap支付
         * @param orderId {int} 订单 ID
         */
        unionpayWap: function(orderId) {
        
            var deffered = $q.defer();

            apiService.get('/wappay/union', {
                oid: orderId
            }).success(function(data) {
                payWap(data.url, deffered);
            }).error(function(data) {
                deffered.reject(data.error.text);
            });
            
            return deffered.promise;
        }

    };
    return payService;
});

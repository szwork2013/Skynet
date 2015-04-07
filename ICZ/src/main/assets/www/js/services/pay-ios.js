/**
 * 支付相关业务
 */
angular.module('app.services').factory('payService', function($state, apiService, popupService) {

    var payService = {
        /**
         * 支付宝快捷支付
         * @param orderId {int} 订单 ID
         */
        alipay: function(orderId, payFinishied) {

            window.resultString = function (data) {
                var result = JSON.parse(data),
                    status = result.resultStatus,
                    message = result.memo;
                    
                if (status == 9000) {
                    popupService.alertPopup('支付成功', '')
                    .then(function () {
                        $state.go('paysuccess', {id:orderId});
                    });
                    payFinishied();
                } else {
                    popupService.alertPopup('支付失败', message);
                    payFinishied();
                }
            }

            apiService.get('/alipay/payinfo', {
                orderid: orderId
            })
            .success(function (data) {
                var out_trade_no= data.tradeNo,
                    subject = data.subject,
                    bodtxt = data.body,
                    total_fee = data.totalFee,
                    url = data.notifyUrl;
                    window.plugins.Pgalipay.alipay(
                        out_trade_no, subject,bodtxt, total_fee,url,
                        function(data) {
                            resultString(data);
                        }, function(data) {
                            resultString(data);
                        }
                    );
            })
            .error(function (data) {
                popupService.alertPopup('支付失败', data.error.text);
            });
        }
    };

    return payService;
});

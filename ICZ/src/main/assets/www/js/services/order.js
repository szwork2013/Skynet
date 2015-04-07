angular.module('app.services')

.factory('orderService',function($http, apiService){

    return {
        
        /**
         * 获取订单列表数据
         * @param orderType {Integer} 订单类型 [1待支付；2待签收；3已完成；4已取消；0全部]
         * @param pageNumber {Integer} 页码，从 0 开始
         * @param pageSize {Integer} 每页数据的数量
         */
        orderList: function(orderType, pageNumber, pageSize){
            return apiService.get('/order/list', {
                type: orderType,
                page: pageNumber,
                count: pageSize
            });
        },

        /**
         * 获取订单详细信息
         */
        orderInfo: function(orderId){
            return apiService.get('/order/info', {
                oid: orderId
            });
        },


        /**
         * 取消订单
         */
        cancelOrder: function(orderId){
            return apiService.post('/cancelorder', {
                oid: orderId
            });
        }

    };
});

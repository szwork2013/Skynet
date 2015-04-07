/**
 * 优惠券
 */
angular.module('app.services').factory('couponService', function(apiService) {
    var couponService = {
        /**
         * 激活优惠券
         * @param id {int} 优惠券ID
         */
        activate: function(id) {
            return apiService.post('/activatecoupon', {
                'id': id
            });
        },

        /**
         * 获取所有优惠券
         * @param type {Integer} 优惠券类型 [0可使用，1未到期 ,2已使用，3已过期]
         * @param pageNumber {Integer} 页码，从 0 开始
         * @param pageSize {Integer} 每页数据的数量
         */
        orderList: function(couponType, pageNumber, pageSize){
            var couponType = couponType == 0 ? 0 : couponType + 1;
            return apiService.get('/cardlist', {
                type: couponType,
                page: pageNumber,
                count: pageSize
            });
        }
    };
    return couponService;
});

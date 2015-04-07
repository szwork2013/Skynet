/**
 * 商品收藏相关业务
 */
angular.module('app.services').factory('collectService', function(apiService) {
    var collectService = {
        /**
         * 添加收藏商品
         * @param productId {int} 收藏商品的 ID
         */
        add: function(productId) {
            return apiService.post('/collect/add', {
                'pid': productId
            });
        },

        /**
         * 获取所有用户收藏的商品
         */
        getAll: function() {
            return apiService.get('/collect/list', {
                type: 1
            });
        },

        /**
         * 删除或清空收藏商品
         * @param productId {int} 所收藏的商品的 ID
         * @param isall {boolean} 是否清空
         */
        del: function(productId, isall) {
            return apiService.post('/collect/del', {
                'pid': productId,
                'isall': isall ? 1 : 0
            });
        },

        /**
         * 删除收藏商品，可指定多个
         * @param productIds {Array} 所收藏的商品的 ID
         */
        delBatch: function(productIds) {
            productIds = productIds || [];
            return apiService.post('/collect/delBatch', {
                'pids': productIds.join(',')
            });
        }
    };
    return collectService;
});

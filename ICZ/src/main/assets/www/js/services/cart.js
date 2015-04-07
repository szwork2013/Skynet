angular.module('app.services')

.factory('cartService',function($http, apiService){

	return {

        /** 获取购物车数据 */
		shoppingCart: function(){
            return apiService.post('/shoppingCart')
            .success(function (data) {
                CM.cart.total = 0;
                if (data.cartId) {
                    for (var i in data.cartNormalProduct) {
                        CM.cart.total += data.cartNormalProduct[i].count;
                    }
                }
            });
		},
        
        /**
         * 删除购物车中的某件商品
         */
        delCartItem: function(cartId, productId, amount) {
            return apiService.get('/delcartitem', {
                pid: productId,
                cartid: cartId
            }).success(function () {
                CM.cart.total -= amount;
            });
        },

        /** 将商品加入购物车 */
		addCartItem: function(productId, amount){
            return apiService.get('/addcart', {
                pid: productId,
                amount: amount
            })
            .success(function () {
                CM.cart.total += amount;
            });
		},
        
        delCart: function () {
            return apiService.get('/delcart');
        }
	};
});

angular.module('app.services')
.factory('refundService',function($q, $state, apiService){
	return {
		//查询用户申请退货列表
		findRefundlist: function(params) {
			return apiService.get('/customerservice/orderlist', params).then(
				function (response) {
					var data = response.data;
					return data;
				},
				function (response) {
					return $q.reject(response);
				}
			);
		}
	};
})
;
/**
* 客户服务 退货内容控制器
*/
angular.module('app.controllers.refund', [])

	// 退货列表控制器
	.controller('refundListCtrl', function($scope) {

	 	angular.extend($scope, {

	 		config: {
	 			type: 0
	 		},

	 		// 信息按钮组
	 		infoButtons: [
	 			{text: '申请退货', state: 'button-confirm'},
	 			{text: '退货记录', state: 'button-cancel'}
	 		],

	 		// 切换信息选项卡
	 		showInfoContent: function(type) {

	 			for (var i in $scope.infoButtons) {
	 				var button = $scope.infoButtons[i];
	 				if (i == type) {
	 					if (button.state == 'button-confirm') {
	 						return;
	 					}
	 					button.state = 'button-confirm';
	 				} else {
	 					button.state = 'button-cancel';
	 				}
	 			}

	 			$scope.config.type = type;
	 		}

	 	});
    })

    // 退货申请控制器
	.controller('refundApplyCtrl', function($scope) {

	})

	// 退货详情控制器
	.controller('refundDetailCtrl', function($scope) {

	})
;
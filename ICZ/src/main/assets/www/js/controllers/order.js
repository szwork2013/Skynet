/**
 * 订单制器
 */
angular.module('app.controllers.order', [])
    
    // 提交订单成功控制器
    .controller('orderSuccessCtrl', function($scope, $state, $stateParams, nnclickService) {

        angular.extend($scope, {
            aliasId: $stateParams.aliasId,
            price: $stateParams.price,
            goBack: function () {
                $state.go('home');
            },
            toPay: function () {
                $state.go('pay', {id:$stateParams.orderId});
            }
        });
    })


    // 会员中心 我的订单控制器
    .controller('orderListCtrl', function($scope, $state, $ionicScrollDelegate, 
                                          orderService, popupService, nnclickService) {       

        // 统计页面访问
        nnclickService.countView('订单列表页');

        angular.extend($scope, {
            config: {
                type: 0,
                page: 1,
                pageSize: 5
            },
            orderList: [],
            loading: true,
            moreData: false,
            // 信息按钮组
            infoButtons: [
                {text: '全部', state: 'button-confirm'},
                {text: '待付款', state: 'button-cancel'},
                {text: '已完成', state: 'button-cancel'},               
                {text: '已取消', state: 'button-cancel'}
            ],
            messages: [
                '您最近还没有购买过商品~',
                '无待付款订单~',
                '无已完成订单~',
                '无已取消订单~',
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
                $scope.loadOrderList(true);
            },

            loadOrderList: function (reset) {
                var config = $scope.config,
                    type = config.type;

                if (reset) {
                    $scope.loading = true;
                    config.page = 1;
                    $scope.orderList = [];
                }

                orderService.orderList(type, config.page, config.pageSize)
                .success(function (data) {
                    if (type != $scope.config.type) {
                        return;
                    }
                    $scope.orderList = $scope.orderList.concat(data.orders_list);
                    // 重置内容区高度
                    $ionicScrollDelegate.resize();
                    $scope.loading = false;
                    config.page += 1;
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    $scope.moreData = $scope.orderList.length < data.total_count;
                })
                .error(function(data) {
                    popupService.alertPopup('加载订单列表失败', data.error.text);
                    $scope.loading = false;
                });
            },

            goToPay: function (index) {
                $state.go('pay', {id:orderList[index].orderId});
            }
            
        });

        $scope.loadOrderList(true);
    })

    // 会员中心 订单详情控制器
    .controller('orderDetailCtrl', function($scope, $stateParams, orderService, 
                                            popupService, nnclickService) {       

        // 统计页面访问
        nnclickService.countView('订单详情页', 'id=' + $stateParams.id);

        angular.extend($scope, {
            order: null,
            consignee: null,
            total: null,
            freight: null,
            freightDiscount: null,
            orderDiscount: null,
            couponDiscount: null,
            result: null,
            orderId: $stateParams.id,
            cancelOrder: function () {
                popupService.confirmPopup('取消订单', '是否要取消订单？')
                .then(function (res) {
                    if(res) {
                        orderService.cancelOrder($stateParams.id)
                        .success(function (data) {
                            popupService.alertPopup('取消订单', '取消订单成功');
                            $scope.order.orderdetail_info.order_status = '已取消';
                        })
                        .error(function (data) {
                            popupService.alertPopup('取消订单失败', data.error.text);
                        });
                    }
                });
            }
        });

        var loadOrderInfo = function () {
            orderService.orderInfo($stateParams.id)
            .success(function (data) {
                $scope.order = data;
                $scope.consignee = data.orderdetail_receiveinfo;
                var valueMap = data.orderdetail_statistics;
                $scope.total = valueMap.pro_total_price.value;
                $scope.freight = valueMap.freight.value;
                $scope.freightDiscount = valueMap.preferences_freight.value;
                $scope.couponDiscount = valueMap.coupon_discount.value;
                $scope.orderDiscount = valueMap.order_privilege.value;
                $scope.result = data.orderdetail_info.order_amount;
            })
            .error(function (data) {
                popupService.alertPopup('获取订单信息失败', data.error.text)
                .then(function () {
                    $scope.$parent.goBack();
                });
            });
        };

        loadOrderInfo();

    })

    // 支付页面控制器
    .controller('payCtrl', function($scope, $state, $stateParams, payService, popupService, nnclickService) {

        // 统计页面访问
        nnclickService.countView('支付页', 'id=' + $stateParams.id);

        angular.extend($scope, {
            loading: false,
            goBack: function () {
                // var embed = plus.webview.getWebviewById('embed');
                // if (window.embed) {
                    // window.embed.close();
                    // return;
                // }
                $scope.$parent.goBack();
            },
            // 信息按钮组
            infoButtons: [
                {text: '储蓄卡', state: 'button-confirm'},
                {text: '信用卡', state: 'button-cancel'}
            ],
            // 信息选项卡状态
            infoState: 0,
            // 切换信息选项卡
            showInfoContent: function(infoState) {

                $scope.infoState = infoState;

                for (var i in $scope.infoButtons) {
                    var button = $scope.infoButtons[i];
                    if (i == infoState) {
                        button.state = 'button-confirm';
                    } else {
                        button.state = 'button-cancel';
                    }
                }

            },
            pay: function (paymode) {
                $scope.loading = true;
                payService[paymode]($stateParams.id)
                .then(
                    function () {
                        // 统计页面访问
                        nnclickService.countView('支付成功页', 'id=' + $stateParams.id);

                        $state.go('paysuccess', {id:$stateParams.id});
                    },
                    function (message) {
                        message && popupService.alertPopup('支付失败', message);
                    }
                )
                .finally(
                    function () {
                        $scope.loading = false;
                    }
                );
            }
        });
    })

    // 支付成功
    .controller('paySuccessCtrl', function($scope, $state, $stateParams, orderService, nnclickService) {

        angular.extend($scope, {
            order: null,
            orderId: $stateParams.id,
            goBack: function () {
                $state.go('home');
            }
        });

        var loadOrderInfo = function () {
            orderService.orderInfo($stateParams.id)
            .success(function (data) {
                $scope.order = data;
            });
        };

        loadOrderInfo();


    })
;

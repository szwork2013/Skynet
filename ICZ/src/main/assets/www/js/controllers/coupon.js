/**
 * 用户操作控制器
 */
angular.module('app.controllers.user')

    // 会员中心 我的优惠券控制器
    .controller('couponsCtrl', function($scope, $ionicScrollDelegate, 
                                        couponService, popupService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('优惠券页');

        angular.extend($scope, {
            data: {
                couponId: ''
            },
            config: {
                type: 0,
                page: 1,
                pageSize: 10
            },
            coupons: [],
            loading: true,
            moreData: false,
            isExpired: false,
            // 信息按钮组
            infoButtons: [
                {text: '可用优惠券', state: 'button-confirm'},
                {text: '已用优惠券', state: 'button-cancel'},               
                {text: '过期优惠券', state: 'button-cancel'}
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

                $scope.isExpired = type == 2 ? 'out-of-date-list' : '';
                $scope.config.type = type;
                $scope.loadCoupons(true);
            },

            loadCoupons: function (reset) {
                var config = $scope.config,
                    type = config.type;

                if (reset) {
                    $scope.loading = true;
                    config.page = 1;
                    $scope.coupons = [];
                }

                couponService.orderList(type, config.page, config.pageSize)
                .success(function (data) {
                    if (type != $scope.config.type) {
                        return;
                    }
                    $scope.coupons = $scope.coupons.concat(data.card_list);
                    // 重置内容区高度
                    $ionicScrollDelegate.resize();
                    $scope.loading = false;
                    config.page += 1;
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    $scope.moreData = $scope.coupons.length < data.count;
                })
                .error(function(data) {
                    popupService.alertPopup('加载优惠券信息失败', data.error.text);
                    $scope.loading = false;
                });
            },
            activateCoupon: function () {
                var couponId = $scope.data.couponId;
                if (!couponId) {
                    return;
                }
                popupService.confirmPopup('激活优惠券', '是否要激活优惠券：<br />' + couponId)
                .then(function(res) {
                    if(res) {
                        $scope.loading = true;
                        couponService.activate(couponId)
                        .success(function (data) {
                            popupService.alertPopup('优惠券激活成功');
                            $scope.loadCoupons(true);
                        })
                        .error(function (data) {
                            popupService.alertPopup('优惠券激活失败', data.error.text);
                            $scope.loading = false;
                        });
                    }
                });
            }
        });

        $scope.loadCoupons(true);

    })

;

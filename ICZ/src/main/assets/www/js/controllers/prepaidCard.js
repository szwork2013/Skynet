/**
 * 用户操作控制器
 */
angular.module('app.controllers.user')

    // 会员中心 中免卡控制器
    .controller('prepaidCardCtrl', function($scope, $ionicScrollDelegate, 
                                            couponService, popupService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('中免卡');

        angular.extend($scope, {
            data: {
                amount: '',
                cardId: '',
                password: '',
                validateCode: ''
            },
            config: {
                type: 0,
                page: 1,
                pageSize: 10
            },
            loading: true,
            moreData: false,
            // 图片验证码
            validateCodeLink: CM.info.service + '/validateCode?Appkey=' + CM.info.appkey + '&dumy=',
            dumy: Math.random(),
            // 刷新图片验证码
            refreshCode: function () {
                $scope.dumy = Math.random();
            },
            // 信息按钮组
            infoButtons: [
                {text: '购买', state: 'button-confirm'},
                {text: '购买记录', state: 'button-cancel'},               
                {text: '消费记录', state: 'button-cancel'},         
                {text: '激活记录', state: 'button-cancel'}
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
                $scope.loadCoupons(true);
            },

            // 默认充卡方式，选中第一种
            confirm : true,
            cancel : false,

            // 修改充卡方式
            changeMethod: function(i) {
                if (i == 1) {
                    $scope.confirm = true;
                    $scope.cancel = false;
                } else {
                    $scope.confirm = false;
                    $scope.cancel = true;
                }
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

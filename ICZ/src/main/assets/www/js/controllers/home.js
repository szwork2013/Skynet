/**
 * 首页控制器
 */
angular.module('app.controllers.home', [])

    // 首页控制器
    .controller('homeCtrl', function($scope, $state, $timeout, $ionicSlideBoxDelegate,
                                     apiService, userService, clientService, nnclickService) {

        // 统计页面访问
        nnclickService.countView('首页');
    
        var titles = ['中免商城', '全球热销'],
            timer = null;
        
        // 更新slide box,防止返回缓存页面时渲染错误
        $scope.$on("$ionicView.afterEnter", function() {
            $ionicSlideBoxDelegate.update();
            $timeout(function () {
                $scope.data && $ionicSlideBoxDelegate.$getByHandle('carousel').start();
                $scope.autoPlay = true;
            }, 1000);
        });
        
        // 加载数据
        $scope.$on("$ionicView.beforeEnter", function() {
            /* 检查网络状态
            if (navigator.connection && navigator.connection.type == 'Connection.NONE') {
                loadingService.showText('请接入有效网络连接');
                return;
            }*/

            if (!$scope.data) {
                $scope.update();
            }
        });
        
        // 检查更新
        if (!clientService.getCache('autoUpdated') && CM.config.notifyUpdate) {
            $timeout(function () {
                clientService.checkUpdate();
            }, 500);
        }
        clientService.addCache('autoUpdated', true);
        
        angular.extend($scope, {
            // 首页数据
            data: null,
            loading: true,
            // 首页标题
            title: titles[0],
            // 首页分页标识
            pagerIndex: 0,
            // 切换首页标题
            slideChanged: function (index) {
                $scope.title = titles[index];
                $scope.pagerIndex = index;
            },
            // 是否显示轮播图
            showCarousel: false,
            // 是否自动播放轮播图
            autoPlay: false,
            // 清除自动轮播
            clearInterval: function () {
                $scope.autoPlay = false;
                if (timer) {
                    $timeout.cancel(timer);
                }
            },
            // 添加自动轮播
            addInterval: function () {
                timer = $timeout(function () {
                    $scope.autoPlay = true;
                }, 2000);
            },
            // 页面跳转
            goTo: function (object) {

                switch (object.type) {
                    case 'category':
                        $state.go('productlist.view', {
                            type:'category',
                            id: object.value,
                            title: encodeURIComponent(object.title),
                            filter: ''
                        });
                        break;
                    case 'brand':
                        $state.go('productlist.view', {
                            type:'brand',
                            id: object.value,
                            title: encodeURIComponent(object.title),
                            filter: ''
                        });
                        break;
                    case 'productid':
                        $state.go('product.detail', {
                            id: object.value
                        });
                        break;
                    case 'subject':
                        $state.go('subject', {
                            id: object.value,
                            title: encodeURIComponent(object.title)
                        });
                        break;
                }
            },
            // 跳转会员中心,检测登录状态
            goToUser: function () {
                // 如果用户未经登录,跳转登录页
                if (!userService.hasLogined()) {
                    $state.go('login', {returnState:true});
                    return;
                }
                $state.go('user');
            },
            // 更新数据
            update: function () {
                apiService.get('/index')
                .success(function(data, status, headers, config) {
                    updateData(data);
                    clientService.addCache('home', data);
                })
                .finally(function() {
                    $scope.loading = false;
                    $scope.$broadcast('scroll.refreshComplete');
                });
            }
        });
        
        // 更新数据渲染
        var updateData = function (data) {
            for (var i in data.combination_product) {
                var product = data.combination_product[i];
                
                for (var j in product.product_dese) {
                    var property = product.product_dese[j];
                    if (property.key == '品牌') {
                        product.brand = property.value;
                        break;
                    }
                }
            }
            $scope.autoPlay = false;
            $scope.showCarousel = false;
            $scope.data = data;
            $timeout(function () {
                $scope.showCarousel = true;
                $scope.autoPlay = true;
            }, 100);
        };
    })
;
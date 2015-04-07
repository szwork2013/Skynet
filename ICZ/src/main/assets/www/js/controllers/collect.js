/**
 * 用户操作控制器
 */
angular.module('app.controllers.user')

    // 会员中心 我的订单控制器
    .controller('collectionsCtrl', function($scope, collectService, popupService, nnclickService) {

        // 统计页面访问
        nnclickService.countView('收藏列表页');

        angular.extend($scope, {
            // 收藏列表
            collections: [],
            loading: true,
            // 删除收藏
            removeCollection: function (index) {
                popupService.confirmPopup('删除收藏', '是否要删除收藏？')
                .then(function (res) {
                    if(res) {
                        $scope.loading = true;
                        collectService.del($scope.collections[index].id)
                        .success(function (data) {
                            $scope.collections.splice(index, 1);
                            $scope.loading = false;
                        })
                        .error(function(data) {
                            popupService.alertPopup('删除收藏失败', data.error.text);
                            $scope.loading = false;
                        });
                    }
                });
            },
            // 清空收藏
            clearCollections: function () {
                popupService.confirmPopup('删除收藏', '是否要删除所有收藏？')
                .then(function (res) {
                    if(res) {
                        $scope.loading = true;
                        collectService.del(null, true)
                        .success(function (data) {
                            $scope.collections = [];
                            $scope.loading = false;
                        })
                        .error(function(data) {
                            popupService.alertPopup('删除收藏失败', data.error.text);
                            $scope.loading = false;
                        });
                    }
                });
            },
            // 加载收藏列表
            loadCollections: function () {
                $scope.loading = true;
                collectService.getAll()
                .success(function (data) {
                    $scope.collections = data.collect_list;
                    $scope.loading = false;
                })
                .error(function(data) {
                    popupService.alertPopup('获取收藏失败', data.error.text);
                    $scope.loading = false;
                });
            }
        });

        $scope.loadCollections();
    })

;

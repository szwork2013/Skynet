angular.module('app.services')
    .factory('loadingService', function($ionicLoading) {

        return {
            // 显示读取图标
            show: function () {
                $ionicLoading.show({
                    template: '<div class="loading-content"><i class="loading-icon"></i></div>',
                    noBackdrop: true
                });
            },

            // 隐藏读取图标
            hide: function () {
                $ionicLoading.hide();
            },

            // 显示文本
            showText: function (text) {
                $ionicLoading.show({
                    template: text,
                    noBackdrop: true,
                    duration: 3000
                });
            }
        };
    });

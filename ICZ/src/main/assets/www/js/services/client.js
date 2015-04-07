/**
 * 系统信息控制器
 */
angular.module('app.services').factory('clientService', function($q, $ionicPopup, apiService, localStorageService, popupService) {

    // 应用缓存
    var cache = {};

    var clientService = {
        /**
         * 检查更新
         */
        checkUpdate: function (productId) {
            var _this = this;
            return apiService.post('/client/update')
                .then(
                    function (response) {
                        var data = response.data;
                        if (!data.revision || data.revision == CM.info.appversion) {
                            return $q.when(data);
                        }

                        if (data.type == 1) {
                            $ionicPopup.alert({
                                title: '检查更新',
                                template: data.descript,
                                okText: '更新',    
                                okType: 'button-light'
                            })
                            .then(function (response) {
                                window.open(data.url, '_system');
                                navigator.app && navigator.app.exitApp();
                            });
                        } else {
                            $ionicPopup.confirm({
                                title: '检查更新',
                                template: data.descript,
                                okText: '更新',    
                                okType: 'button-light',         
                                cancelText: '不再提醒',
                                cancelType: 'button-light button-line'
                            })
                            .then(function (response) {
                                if(response) {
                                    window.open(data.url, '_system');
                                } else {
                                    _this.notifyUpdate(false);
                                }
                            });
                        }
                        return $q.when(data);
                    },
                    function (response) {
                        var data = response.data;
                        popupService.alertPopup('检查更新失败', data.error.text);
                        return $q.reject(data);
                    }
                );
        },

        /**
         * 设置是否提示更新
         */
        notifyUpdate: function (option) {
            CM.config.notifyUpdate = option;
            localStorageService.set('config', CM.config);
        },
        
        /**
         * 设置是否启用节省流量模式
         */
        saveTraffic: function (option) {
            CM.config.saveTraffic = option;
            localStorageService.set('config', CM.config);
        },

        /**
         * 添加缓存
         */
        addCache: function (key, value) {
            cache[key] = value;
        },

        /**
         * 读取缓存
         */
        getCache: function (key, defVal) {
            return cache[key] ? cache[key] : defVal;
        },

        /**
         * 清空缓存
         */
        clearCache: function (option) {
            cache = {};
        },

        /**
         * 用户反馈
         */
        feedback: function (message) {
            return apiService.post('/client/tickl', {
                feedback: message
            });
        }

    };
    return clientService;
});

angular.module('app.services')
    .factory('popupService', function($ionicPopup) {

        return {
            // 弹出提示窗口
            alertPopup: function (title, template) {
                return $ionicPopup.alert({
                    title: title,
                    template: template,
                    okType: 'button-light'
                });
            },
            
            // 弹出确认窗口
            confirmPopup: function (title, template) {
                return $ionicPopup.confirm({
                    title: title,
                    template: template,
                    okText: '好',    
                    okType: 'button-light',         
                    cancelText: '取消',
                    cancelType: 'button-light button-line'
                });
            }
        };
    });

/**
 * 注册控制器
 */
angular.module('app.controllers.user')
    
    // 登录页控制器
    .controller('loginCtrl', function($scope, $state, $stateParams, userService, popupService) {

        // 如果用户已经登录,跳转首页
        if (userService.hasLogined()) {
            $state.go('home');
        }

        $scope.data = {
            username: CM.user.username
        };
        
        $scope.submit = function() {
            if (!$scope.data.username || !$scope.data.username.trim() || 
                !$scope.data.password || !$scope.data.password.trim()) {
                return false;
            }
        
            userService.login($scope.data.username, $scope.data.password)
                .success(function(data) {
                    CM.merge(CM.user, data);
                    var state = $stateParams.returnState;
                    if (state) {
                        $scope.$parent.goBack();
                    } else {
                        $state.go('home');
                    }
                })
                .error(function(data, status, headers, config) {
                    popupService.alertPopup('登录失败', data.error.text);
                });
        };

    })

;

/**
 * 导航控制器
 */
angular.module('app.controllers.navigation', [])

    .controller('navigationCtrl', function($scope, $ionicHistory) {
        var enable = false;

        $scope.$on('$ionicView.afterEnter', function () {
            enable = true;
        });

        $scope.$on('$ionicView.beforeLeave', function () {
            enable = false;
        });

        $scope.goBack = function() {
            enable && $ionicHistory.goBack();
        };
    })

;
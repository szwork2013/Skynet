/**
 * 欢迎页控制器
 */
angular.module('app.controllers.startup', [])

    // 欢迎页控制器
    .controller('startupCtrl', function($scope, $state, $document, apiService, localStorageService) {

        $scope.advertise = null;
        
        var loaded = false;
        
        apiService.get('/startup')
        .success(function (data) {
            if (!data.pic) {
                return;
            }
            $scope.advertise = data.pic;
            var img = $document[0].createElement('img');
            img.onload = function() {
                loaded = true;
                localStorageService.set('advertisement', data.pic);
            };
            img.src = data.pic;
        });
        setTimeout(function () {

            setTimeout(function () {
                window.plus && plus.navigator.closeSplashscreen();
                navigator.splashscreen && navigator.splashscreen.hide();
            }, 1000);

            if (!localStorageService.get('booted')) {
                var data = null;
                if (loaded) {
                    var data = encodeURIComponent($scope.advertise);
                }
                localStorageService.set('booted', true);
                $state.go('tutorial', {data:data ? data : ''});
                return;
            }
            if (loaded) {
                $state.go('advertise', {data:encodeURIComponent($scope.advertise)});
            } else {
                var adv = localStorageService.get('advertisement');
                if (adv) {
                    $state.go('advertise', {data:encodeURIComponent(adv)});
                } else {
                    $state.go('home');
                }
            }
        }, 2500);
    })

    // 新手指南控制器
    .controller('tutorialCtrl', function($scope, $stateParams, $state) {
        $scope.advertise = $stateParams.data;
        $scope.skip = function () {
            if ($scope.advertise) {
                $state.go('advertise', {data:$scope.advertise});
            } else {
                $state.go('home');
            }
        };
    })
    
    // 欢迎页广告控制器
    .controller('advertiseCtrl', function($scope, $stateParams, $state) {
        $scope.advertise = decodeURIComponent($stateParams.data);
        setTimeout(function () {
            $state.go('home');
        }, 3000);
    })
;

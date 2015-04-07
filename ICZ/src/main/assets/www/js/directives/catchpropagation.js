/**
 * 捕获首页轮播切换手势冒泡.
 */

angular.module('app')
    .directive('cmCatchPropagation', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                var element = $el[0];
                
                $el.on('touchstart', function (e) {
                    $scope.clearInterval();
                    e.stopPropagation();
                });
                
                $el.on('touchmove', function (e) {
                    e.stopPropagation();
                });
                
                $el.on('touchend', function (e) {
                    $scope.addInterval();
                    e.stopPropagation();
                });
                
                /* 旧版ionic使用
                ionic.onGesture('dragstart', function (e) {
                    $scope.clearInterval();
                    e.preventDefault();
                    e.stopPropagation();
                }, element);
                
                ionic.onGesture('drag', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                }, element);
                
                ionic.onGesture('dragend', function (e) {
                    $scope.addInterval();
                    e.preventDefault();
                    e.stopPropagation();
                }, element);
                */
            }
        };
    })
;
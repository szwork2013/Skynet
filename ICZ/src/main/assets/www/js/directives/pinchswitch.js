/**
 * 商品列表页手势切换.
 */

angular.module('app')
    .directive('cmPinchSwitch', function factory($ionicScrollDelegate) {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                var col2 = angular.element($el[0].querySelector('.productlist-col2')),
                    col3 = angular.element($el[0].querySelector('.productlist-col3'));
                ionic.onGesture('pinchin', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    col2.addClass('hidden');
                    col3.removeClass('hidden');
                    $ionicScrollDelegate.resize();
                }, $el[0]);
                
                ionic.onGesture('pinchout', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    col2.removeClass('hidden');
                    col3.addClass('hidden');
                    $ionicScrollDelegate.resize();
                }, $el[0]);
            }
        };
    })
;
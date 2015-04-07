/**
 * 动态绑定html片段.
 */

angular.module('app')
    .directive('cmBindHtml', function factory($compile, $ionicScrollDelegate) {
        return {
            restrict: 'A',
            scope: {
                cmBindHtml: '='
            },
            link: function($scope, $el, $attrs) {
                var listener = $scope.$watch('cmBindHtml', function() {
                    $el[0].innerHTML = '';
                    $el.append($compile($scope.cmBindHtml)($scope));
                    $ionicScrollDelegate.resize();
                });

                $scope.$on('$destroy', function() {
                    listener();
                });
            }
        };
    })
;
/**
 * 按宽高比例调整元素高度.
 */

angular.module('app')
    .directive('cmLazyload', function factory($document, $parse) {
        return {
            restrict: 'A',
            scope: {
                cmLazyload: '='
            },
            link: function($scope, $el, $attrs) {
                if (CM.config.saveTraffic) {
                    $el[0].src = 'img/spacer.png';
                    return;
                }
                var listener = $scope.$watch('cmLazyload', function() {
                    var src = $scope.cmLazyload;
                    if (!src) {
                        return;
                    }
                    $el.addClass('lazyload');
                    $el[0].src = 'img/spacer.png';
                    var img = $document[0].createElement('img');
                    img.onload = function() {
                        $el.removeClass('lazyload');
                        $el[0].src = this.src;
                    };
                    img.src = src;
                });

                $scope.$on('$destroy', function() {
                    listener();
                });
            }
        };
    })
;
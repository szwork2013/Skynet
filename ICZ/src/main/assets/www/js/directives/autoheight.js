/**
 * 按宽高比例调整元素高度.
 */

angular.module('app')
    .directive('cmAutoHeight', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                var width = $el[0].clientWidth;
                $el.css('height', (width * $attrs.cmAutoHeight)  + 'px');
            }
        };
    })
;
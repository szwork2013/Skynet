/**
 * 设置订单列表页商品滑动容器宽度
 */

angular.module('app')
    .directive('cmAutoWidth', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                setTimeout(function () {
                    $el.css('width', ($attrs.cmAutoWidth * $el.find('li').length) + 'px');
                }, 1000);
            }
        };
    })
;
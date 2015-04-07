/**
 * 统计点击.
 */

angular.module('app')
    .directive('cmCountClick', function factory(nnclickService) {
        return {
            restrict: 'A',
            link: function($scope, $el, $attrs) {
                $el.on('click', function () {
                    // 统计页面点击
                    var title = $attrs.clickTitle,
                        object = $attrs.clickObject,
                        param = $attrs.clickParam;
                    nnclickService.countClick(title, object, param);
                })
            }
        };
    })
;
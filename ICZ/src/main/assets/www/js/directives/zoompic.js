/**
 * 图片放大缩小页面跳转
 */

angular.module('app')
    .directive('cmZoomPic', function factory($state) {
        return {
            restrict: 'A',          
            link: function($scope, $el, $attrs) {

               $el.on('click', function () {
                    var src = encodeURIComponent($el.attr('src'));
                    $state.go('zoompic', {src:src});
               });

            }
        };
    })
;
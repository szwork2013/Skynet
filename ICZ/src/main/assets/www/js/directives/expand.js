/**
 * 商品分类页展开隐藏三级分类.
 */

angular.module('app')
    .directive('cmExpand', function factory($ionicScrollDelegate) {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                var container = $el.parent(),
                    containerHeight = container[0].clientHeight + 1,
                    content = container.parent(),
                    scrollHandle = content.parent().attr('delegate-handle'),
                    viewportHeight = content.parent()[0].clientHeight;
                    
                $el.on('click', function () {
                    var scroll = $ionicScrollDelegate.$getByHandle(scrollHandle),
                        scrollTop = scroll.getScrollPosition().top,
                        containers = content.children(),
                        padding = container.css('padding-bottom'),
                        contentHeight = containers.length * containerHeight,
                        totalHeight = containerHeight;

                    containers.css('padding-bottom', 0).find('img').removeClass('expanded');

                    if (!padding || padding == '0px') {
                        var panelHeight = container.find('ul')[0].clientHeight;
                        totalHeight += panelHeight;
                        contentHeight += panelHeight;
                        container.css('padding-bottom', panelHeight + 'px').find('img').addClass('expanded');
                    }
                    
                    content.css('height', contentHeight + 'px');
                    scroll.resize();
                    
                    var offset = container[0].offsetTop + totalHeight - scrollTop - viewportHeight;
                    if (offset > 0) {
                        scroll.scrollBy(0, offset, true);
                    }
                    
                    setTimeout(function () {
                        scroll.resize();
                    }, 500);
                });
            }
        };
    })
;
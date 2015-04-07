/**
 * 字母表跳转.
 */

angular.module('app')
    
    .directive('cmAlphabetTouch', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {
                var element = $el[0];
                    previousIndex = -1;
                
                var getNode = function (e) {
                    var touch = e.targetTouches[0],
                        x = touch.pageX - element.offsetLeft,
                        y = touch.pageY - element.offsetTop;
                    if (x < 0 || x > element.clientWidth || y < 0 || y >= element.clientHeight) {
                        return;
                    }
                    
                    var nodes = element.querySelectorAll('li'),
                        index = Math.floor(y / nodes[0].clientHeight);
                        
                    if (previousIndex === index) {
                        return null;
                    }
                    previousIndex = index;
                    return nodes[index];
                };
                
                $el.on('touchmove', function (e) {
                    var curNode = getNode(e);
                    
                    if (!curNode) {
                        return;
                    }
                
                    var preNode = this.querySelector('li.hover'),
                        target = document.getElementById(curNode.getAttribute('letter'));
                        
                    preNode && (preNode.className = preNode.className.replace(/ hover/g, ''));
                    curNode.className += ' hover';
                    $scope.scrollTo(target.offsetTop);
                    e.preventDefault();
                });
                
                $el.on('touchstart', function (e) {
                    var curNode = getNode(e);
                    curNode.className += ' hover';
                    var target = document.getElementById(curNode.getAttribute('letter'));
                    $scope.scrollTo(target.offsetTop);
                    e.preventDefault();
                });
                
                $el.on('touchend', function (e) {
                    var preNode = this.querySelector('li.hover');
                    preNode && (preNode.className = preNode.className.replace(/ hover/g, ''));
                    previousIndex = -1;
                    e.preventDefault();
                });
            }
        };
    })
;
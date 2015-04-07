/**
 * 展开ion-list中的所有option-button.
 */

angular.module('app')
    .directive('cmShowOption', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {

                $el.on('click', function () {
                    $scope.$apply(function () {
                        $scope.optionShown = true;
                    });
                    var items = angular.element(document.querySelectorAll('.item-content'));
                    for (var i = 0; i < items.length; i++) {
                        var item = items.eq(i),
                            option = item.next('.item-options');

                        if (!option.length) {
                            continue;
                        }
                        item.css('-webkit-transform', 'translate3d(-' + option[0].clientWidth + 
                                 'px, 0px, 0px)');
                        option.removeClass('invisible');
                    }
                });
            }
        };
    })

    .directive('cmHideOption', function factory() {
        return {
            restrict: 'A',
            scope: false,
            link: function($scope, $el, $attrs) {

                $el.on('click', function () {
                    $scope.$apply(function () {
                        $scope.optionShown = false;
                    });
                    var items = angular.element(document.querySelectorAll('.item-content')),
                        options = items.next('.item-options');
                    
                    items.css('-webkit-transform', 'translate3d(0px, 0px, 0px)');
                    setTimeout(function () {
                        options.addClass('invisible');
                    }, 300);
                });
            }
        };
    })
;
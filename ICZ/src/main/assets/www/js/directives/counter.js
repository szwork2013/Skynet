/**
 * 商品数量加减号内容
 */

angular.module('app')
    .directive('cmCounter', function factory() {
        return {
            restrict: 'EA',

            template: [
                '<button class="counter-dec">-</button>',
                '<input class="counter-num" id="{{inputId}}" name="{{inputName}}" type="text" value="{{ngModel.$viewValue}}" />',
                '<button class="counter-inc">+</button>',
            ].join(''),

            scope: {
                inputName: '@',
                inputId: '@',
                inputReadonly: '@',
                initial: '=',
                maxNum: '=',
                minNum: '='
            },

            require: 'ngModel',

            link: function($scope, el, attrs, ngModel) {
                $scope.ngModel = ngModel;
                // if (!ngModel.$viewValue || isNaN(ngModel.$viewValue)) {
                    // ngModel.$setViewValue($scope.minNum);
                // }

                var input = angular.element(el[0].querySelector('.counter-num')),
                    decBtn = angular.element(el[0].querySelector('.counter-dec')),
                    incBtn = angular.element(el[0].querySelector('.counter-inc'));

                el.addClass('counter').removeAttr('name');
                refreshButtons();
                input.prop('disabled', $scope.inputReadonly);

                decBtn.on('click', function() {
                    ngModel.$setViewValue(Math.max(ngModel.$viewValue - 1, $scope.minNum));
                    refreshButtons();
                    $scope.$apply();
                });
                incBtn.on('click', function() {
                    ngModel.$setViewValue(Math.min(ngModel.$viewValue + 1, $scope.maxNum));
                    refreshButtons();
                    $scope.$apply();
                });

                ngModel.$viewChangeListeners.push(function() {
                    $scope.$eval(attrs.ngChange);
                });

                ngModel.$formatters.push(function(value) {
                    var isMin = value === $scope.minNum,
                        isMax = value === $scope.maxNum;
                    decBtn.toggleClass('disabled', isMin).prop('disabled', isMin);
                    incBtn.toggleClass('disabled', isMax).prop('disabled', isMax);
                    return value;
                });

                // ngModel.$parsers.push(refreshButtons);
                
                var listener = $scope.$watch('maxNum', function (viewValue) {
                    if (isNaN(ngModel.$viewValue) || isNaN($scope.maxNum)) {
                        return;
                    }
                    ngModel.$setViewValue(Math.min(ngModel.$viewValue, $scope.maxNum));
                    refreshButtons();
                });

                function refreshButtons() {
                    var isMin = ngModel.$viewValue === $scope.minNum,
                        isMax = ngModel.$viewValue === $scope.maxNum;
                    decBtn.toggleClass('disabled', isMin).prop('disabled', isMin);
                    incBtn.toggleClass('disabled', isMax).prop('disabled', isMax);
                }

                $scope.$on('$destroy', function() {
                    listener();
                });
            }
        };
    });

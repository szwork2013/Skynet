/**
 * 表单验证
 */
angular.module('app')
    .directive('cmValidate', function factory($compile) {

        return {
            require: '^form',
            link: function($scope, $el, $attrs, formCtrl) {

                $el.addClass('validate-block')
                   .append('<p class="help-block font-mini opacity-hide"></p>');

                if (!$attrs.disableIcon) {
                    $el.addClass('show-icon');
                }

                var input = $el.find('input'),
                    helpBlock = angular.element($el[0].querySelector('.help-block')),
                    name = input.attr('name'),
                    target = $attrs.compareTarget;

                // 空值提示
                if ($attrs.requiredTip) {
                    helpBlock.append($compile('<span class="tip" ng-show="form.' + name + 
                        '.$error.required">' + $attrs.requiredTip + '</span>')($scope));
                }

                // 格式提示
                if ($attrs.patternTip) {
                    helpBlock.append($compile('<span class="tip" ng-show="form.' + name + 
                        '.$error.pattern">' + $attrs.patternTip + '</span>')($scope));
                }

                // 二次输入密码校验
                if (target && $attrs.compareTip) {
                    var tip = $compile('<span class="tip" ng-show="!form.' + name + 
                        '.$error.required && !form.' + name + 
                        '.$error.pattern && form.' + name + 
                        '.$error.compare">' + $attrs.compareTip + '</span>')($scope);
                    helpBlock.append(tip);
                    var listener2 = $scope.$watch(function() {
                        return formCtrl[name].$viewValue == formCtrl[target].$viewValue;
                    }, function (newState, oldState) {
                        formCtrl[name].$setValidity('compare', newState);
                    });
                }

                // 监听表单校验的结果，如果校验成功，为表单项添加表示成功或失败的 class。
                var listener = $scope.$watch(function() {
                    return formCtrl[name].$valid ? 'success' : formCtrl[name].$invalid ? 'danger' : '';
                }, function (newStateClass, oldStateClass) {
                    $el.removeClass(oldStateClass).addClass(newStateClass);
                });

                // 第一次失去焦点时显示提示信息
                input.one('blur', function (e) {
                    helpBlock.removeClass('opacity-hide');
                });

                $scope.$on('$destroy', function() {
                    listener();
                    listener2 && listener2();
                });
            }
        };
    })
;

/**
 * 用户操作控制器
 */
angular.module('app.controllers.user')

    // 注册页控制器
	.controller('registerCtrl',function($scope, $state, popupService, smsService, userService, validateService) {
        var states  = ['start', 'code', 'finish'],
            btnText = {};
        btnText[states[0]] = '获取短信验证码';
        btnText[states[1]] = '下一步';
        btnText[states[2]] = '注册';
        btnText[states[0] + 'loading'] = '正在发送验证码...';
        btnText[states[1] + 'loading'] = '正在检查验证码...';
        btnText[states[2] + 'loading'] = '正在注册...';
        
        angular.extend($scope, {
            states: states,
            state: states[0],
            data: {
                phone: '',
                picValidateCode: '',
                validateCode: '',
                mobileCode: '',
                password: '',
                repassword: '',               
                accept: true
            },
            submitBtn: {
                text: btnText[states[0]],
                disabled: false
            },
            // 获取校验正则表达式
            getRegExp: validateService.getRegExp,
            // 图片验证码
            picValidate: validateService.getPicValidate(),
            // 短信验证码发送计时
            counter: 0,
            // 返回处理
            goBack: function () {
                for (var i = 0; i < states.length; i++) {
                    if ($scope.state == states[i]) {
                        break;
                    }
                }
                if (i == 0 || i == states.length) {
                    $scope.$parent.goBack();
                } else if (i == 1 && $scope.counter > 0) {
                    $scope.state = states[0];
                    angular.extend($scope, {
                        submitBtn: {
                            text: $scope.counter + '秒后重发',
                            disabled: true
                        }
                    });
                } else {
                    $scope.state = states[i - 1];
                    updateBtn(false);
                }
            },
            // 提交表单
            submit: function () {
                $scope[$scope.state]();
            },
            // 发送短信验证码
            sendRegisterVcode: function (changeState) {
                if (changeState) {
                    updateBtn(true);
                } else {
                    angular.extend($scope, {
                        submitBtn: {
                            text: btnText[states[0] + 'loading'],
                            disabled: true
                        }
                    });
                }
                smsService.sendRegisterVcode($scope.data.phone)
                .success(function(data, status, headers, config) {
                    startTimer();
                    if (changeState) {
                        $scope.state = states[1];
                    }
                })
                .error(function(data, status, headers, config) {
                    setTimeout(function () {
                        popupService.alertPopup('短信验证码发送失败', data.error.text);
                    }, 100);
                })
                .finally(function () {
                        updateBtn(false);
                });
            }
        });
        
        // 更新按钮文本
        var updateBtn = function (loading) {
            angular.extend($scope, {
                submitBtn: {
                    text: btnText[$scope.state + (loading ? 'loading' : '')],
                    disabled: loading
                }
            });
        };
        
        // states[0]表单提交处理
        $scope[states[0]] = function () {
            updateBtn(true);
            userService.checkPicValidateCode($scope.data.picValidateCode)
            .success(function() {
                popupService.confirmPopup('确认手机号码', '我们将发送验证码短信到这个号码：' + $scope.data.phone)
                .then(function(res) {
                    res && $scope.sendRegisterVcode(true);
                });
            })
            .error(function(data) {
                popupService.alertPopup('短信验证码发送失败', data.error.text);
            })
            .finally(function () {
                updateBtn(false);
            });
        };
        
        // 发送短信验证码后,倒计时120秒
        var startTimer = function () {
            $scope.counter = 120;
            
            var decrease = function () {
                $scope.$apply(function() {
                    $scope.counter -= 1;
                    var isStart = $scope.state == states[0];
                    if ($scope.counter > 0) {
                        if (isStart) {
                            $scope.submitBtn.text = $scope.counter + '秒后重发';
                        }
                        setTimeout(decrease, 1000);
                    } else if ($scope.state == states[0]) {
                        updateBtn(false);
                    }
                });
            }

            setTimeout(decrease, 1000);
        }
        
        // states[1]表单提交处理
        $scope[states[1]] = function () {
            var data = $scope.data;
            updateBtn(true);
            smsService.validateRegisterVcode(data.phone, data.validateCode)
            .success(function(result) {
                $scope.state = states[2];
            })
            .error(function(data, status, headers, config) {
                popupService.alertPopup('短信验证码错误', data.error.text);
            })
            .finally(function () {
                updateBtn(false);
            });
        };
        
        // states[2]表单提交处理
        $scope[states[2]] = function () {
            var password = $scope.data.password,
                repassword = $scope.data.password;
                
            updateBtn(true);

            userService.registerUsePhone({
                phone: $scope.data.phone,
                code: $scope.data.validateCode,
                account: $scope.data.phone,
                password: password
            })
            .success(function() {
                userService.login($scope.data.phone, $scope.data.password)
                .success(function () {
                    $state.go('home');
                });
            })
            .error(function(data) {
                updateBtn(false);
                popupService.alertPopup('注册失败', data.error.text);
            });
        };
	})

;

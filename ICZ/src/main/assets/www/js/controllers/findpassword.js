/**
 * 找回密码控制器
 */
angular.module('app.controllers.user')

    // 找回密码控制器
	.controller('findPwdCtrl',function($scope, $state, popupService, userService, validateService) {
        angular.extend($scope, {
            data: {
                target: ''
            },
            submitBtn: {
                text: '下一步',
                disabled: false
            },
            submit: function () {
                var target = $scope.data.target;
                
                if (validateService.isEmail(target)) {
                    updateBtn('正在发送验证邮件...');
                    userService.sendResetPasswordEmail(target)
                    .success(function(data) {
                        popupService.alertPopup('邮件发送成功', '找回密码邮件已经发送到您的邮箱');
                    })
                    .error(function(data) {
                        popupService.alertPopup('找回密码失败', data.error.text);
                    })
                    .finally(function () {
                        updateBtn();
                    });
                    return;
                }
                
                if (validateService.isMobile(target)) {
                    updateBtn('正在验证手机...');
                    userService.getFindpwdInfo(target)
                    .success(function(data) {
                        $state.go('findpwdverify', {phone:target});
                    })
                    .error(function(data) {
                        popupService.alertPopup('找回密码失败', data.error.text);
                    })
                    .finally(function () {
                        updateBtn();
                    });
                } else {
                    popupService.alertPopup('找回密码失败', '身份信息错误');
                }

            }
        });
        
        var updateBtn = function (text) {
            var button = $scope.submitBtn;
            button.disabled = !button.disabled;
            button.text = button.disabled ? text : '下一步';
        };
    })
    
    // 找回密码,验证码控制器
    .controller('findPwdVerifyCtrl',function($scope, $state, $stateParams, popupService, 
                                             userService, smsService, validateService) {
        angular.extend($scope, {
            data: {
                phone: $stateParams.phone,
                picValidateCode: '',
                validateCode: ''
            },
            submitBtn: {
                text: '下一步',
                disabled: false
            },
            // 获取校验正则表达式
            getRegExp: validateService.getRegExp,
            // 图片验证码
            picValidate: validateService.getPicValidate(),
            // 短信验证码发送计时
            counter: 0,
            // 是否已发送过短信验证码
            sentCode: false,
            // 发送短信验证码
            sendFindpwdVcode: function () {
                updateBtn('正在检测验证码...');
                userService.checkPicValidateCode($scope.data.picValidateCode)
                .success(function(data, status, headers, config) {
                    updateBtn();
                    updateBtn('正在发送短信验证码...');
                    smsService.sendFindpwdVcode($scope.data.phone)
                    .success(function(data, status, headers, config) {
                        $scope.sentCode = true;
                        startTimer();
                    })
                    .error(function(data, status, headers, config) {
                        popupService.alertPopup('短信验证码发送失败', data.error.text);
                    })
                    .finally(function () {
                        updateBtn();
                    });
                })
                .error(function(data, status, headers, config) {
                    updateBtn();
                    popupService.alertPopup('验证码错误', data.error.text);
                });
            },
            submit: function () {
                var data = $scope.data;
                updateBtn('正在检测短信验证码...');
                smsService.validateFindpwdVcode(data.phone, data.validateCode)
                .success(function(result) {
                    $state.go('findpwdreset', {
                        phone: data.phone,
                        code: data.validateCode
                    });
                })
                .error(function(data, status, headers, config) {
                    popupService.alertPopup('短信验证码错误', data.error.text);
                })
                .finally(function () {
                    updateBtn();
                });
            }
        });
        
        var updateBtn = function (text) {
            var button = $scope.submitBtn;
            button.disabled = !button.disabled;
            button.text = button.disabled ? text : '下一步';
        };
        
        // 发送短信验证码后,倒计时120秒
        var startTimer = function () {
            $scope.counter = 120;
            
            var decrease = function () {
                $scope.$apply(function() {
                    $scope.counter -= 1;
                    if ($scope.counter > 0) {
                        setTimeout(decrease, 1000);
                    }
                });
            }

            setTimeout(decrease, 1000);
        };
    })

    // 找回密码,重置密码控制器
	.controller('findPwdResetCtrl',function($scope, $state, $stateParams, 
                                            popupService, userService, validateService) {
        
        angular.extend($scope, {
            data: {
                phone: $stateParams.phone,
                validateCode: $stateParams.code,
                password: '',
                repassword: '',
            },
            submitBtn: {
                text: '提 交',
                disabled: false
            },
            // 获取校验正则表达式
            getRegExp: validateService.getRegExp,
            // 提交表单
            submit: function () {
                updateBtn();
                var data = $scope.data;
                userService.changePwdByPhone(data.phone, data.validateCode, data.password)
                .success(function() {
                    popupService.alertPopup('修改密码成功')
                    .then(function () {
                        $state.go('login');
                    });
                })
                .error(function(data) {
                    popupService.alertPopup('修改密码失败', data.error.text);
                })
                .finally(function () {
                    updateBtn();
                });
            }
        });
        
        var updateBtn = function () {
            var button = $scope.submitBtn;
            button.disabled = !button.disabled;
            button.text = button.disabled ? '正在提交...' : '提 交';
        };
        
	})

;

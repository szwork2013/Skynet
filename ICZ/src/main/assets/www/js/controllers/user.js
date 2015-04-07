/**
 * 用户操作控制器
 */
angular.module('app.controllers.user', [])

    // 会员中心首页控制器
    .controller('userCtrl', function($scope, nnclickService) {

        // 统计页面访问
        nnclickService.countView('会员中心');

        $scope.user = CM.user;
    })
    
    // 应用设置控制器
    .controller('settingCtrl', function($scope, $state, userService, clientService, cartService, popupService) {
        angular.extend($scope, {
            loading: false,
            // 是否使用节约流量模式
            saveTraffic: CM.config.saveTraffic,
            // 是否需要更新
            needUpdate: CM.info.appversion,
            // 检查更新
            checkUpdate: function () {
                if ($scope.loading) {
                    return;
                }
                clientService.notifyUpdate(true);
                $scope.loading = true;
                $scope.needUpdate = '正在检查...';
                clientService.checkUpdate()
                .then(
                    function (data) {
                        if (data.revision && data.revision != CM.info.appversion) {
                            $scope.needUpdate = '需要更新';
                        } else {
                            $scope.needUpdate = '已是最新';
                            popupService.alertPopup('检查更新', '应用已是最新');
                        }
                    },
                    function (data) {
                        $scope.needUpdate =  CM.info.appversion;
                    }
                )
                .finally(function () {
                    $scope.loading = false;
                });
            },

            // 切换是否使用节约流量模式
            toggleSaveTraffic: function () {
                $scope.saveTraffic = !$scope.saveTraffic;
                clientService.saveTraffic($scope.saveTraffic);
            },

            // 清除缓存
            clearCache: function () {
                clientService.clearCache();
                popupService.alertPopup('清除缓存', '缓存清除完毕');
            },

            // 登出
            logout: function () {
                if (userService.hasLogined()) {
                    userService.quit();
                    cartService.delCart();
                }
                $state.go('home');
            }
        });
    })

    // 账户管理 基本信息控制器
    .controller('userInfoCtrl', function($scope, $state, userService, popupService) {
        angular.extend($scope, {
            loading: true,
            data: {},
            updateEmail: function () {
                var email = $scope.data.email;
                if (CM.isEmail(email)) {
                    $scope.loading = true;
                    userService.validateEmail(email)
                    .success(function(data) {
                        popupService.alertPopup('邮件发送成功', '验证邮件已发送到您的邮箱');
                        $scope.loading = false;
                    })
                    .error(function(data) {
                        popupService.alertPopup('邮件发送失败', data.error.text);
                        $scope.loading = false;
                    });
                } else {
                    popupService.alertPopup('邮件发送失败', '邮箱格式错误');
                }
            },
            confirmEdit: function () {
                $scope.loading = true;
                var data = $scope.data;
                userService.updateUserInfo(data.realname, data.gender, data.postcode, data.idcardtype, 
                                           data.idcardno, data.education, data.income)
                .success(function (data) {
                    $scope.$parent.goBack();
                    $scope.loading = false;
                })
                .error(function (data) {
                    popupService.alertPopup('更新用户基本信息失败', data.error.text)
                    $scope.loading = false;
                });
            }
        });

        var loadUserInfo = function () {
            $scope.loading = true;
            userService.getUserInfo()
            .success(function (data) {
                $scope.data = data.userinfo;
                $scope.loading = false;
            })
            .error(function (data) {
                popupService.alertPopup('获取用户基本信息失败', data.error.text)
                .then(function () {
                    $state.go('login', {returnState: true});
                });
                $scope.loading = false;
            });
        };

        loadUserInfo();
    })

    // 账户管理 修改密码控制器
    .controller('modifyPwdCtrl', function($scope, $state, userService, cartService, popupService) {
        angular.extend($scope, {
            loading: false,
            data: {
                oldPassword: '',
                newPassword: '',
                confirm: ''
            },
            valid: {
                newPassword: 'opacity-hide',
                confirm: 'opacity-hide'
            },
            submit: function () {
                var data = $scope.data;
                if (!data.oldPassword || !data.oldPassword.trim()) {
                    popupService.alertPopup('修改密码失败', '当前密码不能为空');
                    return;
                }
                
                if (!data.newPassword || !data.newPassword.trim()) {
                    popupService.alertPopup('修改密码失败', '新密码不能为空');
                    return;
                }
                
                if (!data.confirm || !data.confirm.trim()) {
                    popupService.alertPopup('修改密码失败', '确认密码不能为空');
                    return;
                }

                if (data.newPassword !== data.confirm) {
                    popupService.alertPopup('修改密码失败', '两次输入的密码不一致');
                    // $scope.valid.confirm = '';
                    return;
                } else {
                    // $scope.valid.confirm = 'opacity-hide';
                }

                $scope.loading = true;
                userService.changePassword(data.oldPassword, data.newPassword)
                .success(function (data) {
                    popupService.alertPopup('修改密码成功', '请重新登录')
                    .then(function () {
                        userService.quit();
                        cartService.delCart();
                        $state.go('login', {returnState: ''});
                    });
                    $scope.loading = false;
                })
                .error(function (data) {
                    popupService.alertPopup('修改密码失败', data.error.text);
                    $scope.loading = false;
                });
            }
        });
    })

    // 账户安全 修改手机控制器
    .controller('modifyPhoneCtrl', function($scope, $state, userService, smsService, popupService) {
        angular.extend($scope, {
            loading: false,
            data: {
                phone: '',
                picValidateCode: '',
                validateCode: ''
            },
            submitBtn: {
                text: '提 交',
                disabled: false
            },
            // 图片验证码
            validateCodeLink: CM.info.service + '/validateCode?Appkey=' + CM.info.appkey + '&dumy=',
            dumy: Math.random(),
            counter: 0,
            sentCode: false,
            // 刷新图片验证码
            refreshCode: function () {
                $scope.dumy = Math.random();
            },
            // 发送短信验证码
            sendFindpwdVcode: function () {
                if (!$scope.data.phone || !$scope.data.phone.trim()) {
                    popupService.alertPopup('短信验证码发送失败', '手机号不能为空');
                    return;
                }
                updateBtn('正在检测验证码...');
                userService.checkPicValidateCode($scope.data.picValidateCode)
                .success(function(data, status, headers, config) {
                    updateBtn();
                    updateBtn('正在发送短信验证码...');
                    smsService.sendFindpwdVcode($scope.data.phone)
                    .success(function(data, status, headers, config) {
                        $scope.sentCode = true;
                        startTimer();
                        updateBtn();
                    })
                    .error(function(data, status, headers, config) {
                        updateBtn();
                        popupService.alertPopup('短信验证码发送失败', data.error.text);
                    });
                })
                .error(function(data, status, headers, config) {
                    updateBtn();
                    popupService.alertPopup('验证码错误', data.error.text);
                });
            },
            submit: function () {
                var data = $scope.data,
                    phone = data.phone;
                    
                if (!$scope.sentCode || !phone || !phone.trim() || data.validateCode || data.validateCode.trim()) {
                    return;
                }

                updateBtn('正在提交...');
                userService.updatePhone(phone, data.validateCode)
                .success(function(data, status, headers, config) {
                    popupService.alertPopup('更改手机成功', '您绑定的手机号已更改为：<br />' + phone)
                    .then(function() {
                        $scope.$parent.goBack();
                    });
                    updateBtn();
                })
                .error(function(data, status, headers, config) {
                    popupService.alertPopup('更改手机失败', data.error.text);
                    updateBtn();
                });
            }
        });
        
        var updateBtn = function (text) {
            var button = $scope.submitBtn;
            button.disabled = !button.disabled;
            button.text = button.disabled ? text : '提 交';
        };
        
        // 发送短信验证码后,倒计时60秒
        var startTimer = function () {
            $scope.counter = 60;
            
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

;

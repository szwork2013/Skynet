angular.module('app.services')
    .factory('userService', ['apiService', 'localStorageService', function(api, localStorageService) {
        var userService = {
            /**
             * 判断当前是否有用户登录
             */
            hasLogined: function() {
                return !!CM.user.userid;
            },

            /**
             * 用户登录
             * 不能重复登录，因此如果当前已有用户登录，则调用该方法时将抛出异常。
             * @param username {String} 用户名
             * @param password {String} 用户密码，明文
             */
            login: function(account, password) {
                if (userService.hasLogined()) {
                    throw "不能重复登录";
                }

                return api.post('/user/login', {
                    account: account,
                    password: password
                })
                .success(function(data, status, headers, config) {
                    data.account = account;
                    angular.extend(CM.user, data);
                    localStorageService.set('user', CM.user);
                })
                .error(function(data, status, headers, config) {
                    localStorageService.remove('user');
                });
            },

            /**
             * 退出当前登录的用户
             */
            quit: function() {
                window.CM.user = {};
                localStorageService.remove('user');
                return api.post('/user/logout');
            },

            /**
             * 获取找回密码相关的用户信息
             * @param account {String} 用户账户，支持用户名，邮箱地址以及手机号码。
             */
            getFindpwdInfo: function(account) {
                return api.post('/user/goFindPWD', {
                    account: account
                });
            },

            /**
             * 使用手机号码注册新的帐号
             * @param infoObj {Object} 用户信息对象
             *   属性：
             *     phone: 手机号码
             *     validateCode: 短信验证码
             *     account: 用户名
             *     password: 用户密码，明文
             */
            registerUsePhone: function(infos) {
                return api.post('/user/register', angular.extend({}, infos));
            },

            /**
             * 修改密码，依据短信验证码验证
             * @param phone {String} 手机号码
             * @param vcode {String} 短信验证码
             * @param newpwd {String} 新密码，明文
             */
            changePwdByPhone: function(phone, vcode, newpwd) {
                return api.post('/user/updatepass', {
                    phone: phone,
                    code: vcode,
                    password: newpwd,
                    confirm: newpwd
                });
            },
            
            /**
             * 修改密码
             * @param oldPassword {String} 旧密码
             * @param newPassword {String} 新密码，明文
             */
            changePassword: function(oldPassword, newPassword) {
                return api.post('/user/updatepassword', {
                    oldpassword: oldPassword,
                    newpassword: newPassword
                });
            },

            /**
             * 发送重置密码的邮件
             * @param email {String} 邮箱地址
             */
            sendResetPasswordEmail: function(email) {
                return api.post('/user/findPassword', {
                    email: email
                });
            },
            
            /**
             * 验证邮箱
             * @param email {String} 图片验证码文本
             */
            validateEmail: function (email) {
                return api.post('/user/validateemail', {
                    email: email
                });
            },

            /**
             * 验证支付密码
             */
            checkPayPassword: function(password) {
                return api.post('/paypassword/validate', {
                    payPassword: password
                });
            },
            
            /**
             * 更改绑定手机
             * @param phone {string} 新手机号码
             * @param code {String} 图片验证码文本
             */
            updatePhone: function (phone, code) {
                return api.post('/userinfo/updatephone', {
                    mobilephone: phone,
                    code: code
                });
            },
            
            /**
             * 校验图片验证码
             * @param code {String} 图片验证码文本
             */
            checkPicValidateCode: function (code) {
                return api.post('/checkValidateCode', {
                    code: code,
                    ismobile: 1
                });
            },
            
            /**
             * 获取用户基本信息
             */
            getUserInfo: function() {
                return api.get('/userinfo');
            },
            
            /**
             * 修改用户基本信息
             */
            updateUserInfo: function(realName, gender, postcode, idCardType, idCardNo, education, income) {
                return api.post('/userinfo/update', {
                    realname: realName,
                    gender: gender,
                    postcode: postcode,
                    idcardtype: idCardType,
                    idcardno: idCardNo,
                    education: education,
                    income: income
                });
            },
            
            /**
             * 获取所有提货人信息
             */
            getConsineeList: function() {
                return api.get('/address/list');
            },

            /**
             * 保存或修改一条提货人信息
             */
            editConsignee: function(addressId, name, address, areaId, phone, postcode) {
                var data = {
                    did: addressId,
                    recipients: name,
                    area: areaId,
                    address: address,
                    phone: phone,
                    postcode: postcode,
                    isdefault: 1
                };

                if (addressId) {
                    return this._update(data);
                }
                else {
                    return this._add(data);
                }
            },

            /**
             * 新增提货人信息
             */
            _add: function(data) {
                return api.post('/address/add', data);
            },

            /**
             * 更新提货人信息
             */
            _update: function(data) {
                return api.post('/address/update', data);
            },

            /**
             * 删除某条收货人信息
             */
            removeConsinee: function(id) {
                return api.post('/address/del', {
                    did: id
                });
            },

            /**
             * 获取地区数据
             */
            getArea: function(areaId) {
                return api.get('/address/area', {
                    areaid: areaId
                });
            }
        };

        return userService;
    }]);

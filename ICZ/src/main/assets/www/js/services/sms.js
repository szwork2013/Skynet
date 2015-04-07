/**
 * 一个短信发送服务，负责调用后台短信接口向用户手机发送短信息
 */
angular.module('app.services')
    .factory('smsService', ['apiService', 'localStorageService', function(api, localStorageService) {
        var REGISTER_TYPE = 'REGIST_MESSAGE',
            FINDPWD_TYPE = 'FINDPWD_MESSAGE';

        var smsService = {
            /**
             * 发送手机找回密码的验证码
             * @parse phone 手机号码
             */
            sendFindpwdVcode: function(phone) {
                return smsService._sendSMS({
                    phone: phone,
                    sendType: FINDPWD_TYPE
                });
            },

            /**
             * 发送手机注册验证码
             * @parse phone 手机号码
             */
            sendRegisterVcode: function(phone) {
                return smsService._sendSMS({
                    phone: phone,
                    sendType: REGISTER_TYPE
                });
            },

            /**
             * 校验手机找回密码的验证码
             */
            validateFindpwdVcode: function(phone, vcode) {
                return smsService._validateVcode({
                    phone: phone,
                    code: vcode,
                    type: FINDPWD_TYPE
                });
            },

            /**
             * 校验手机注册验证码
             */
            validateRegisterVcode: function(phone, vcode) {
                return smsService._validateVcode({
                    phone: phone,
                    code: vcode,
                    type: REGISTER_TYPE
                });
            },

            /**
             * 调用发送手机短信息接口
             */
            _sendSMS: function(params) {
                return api.post('/user/send-message', params || {});
            },

            /**
             * 校验通过手机短信发送的验证码
             */
            _validateVcode: function(params) {
                return api.post('/user/validate', params || {});
            }
        };

        return smsService;
    }]);

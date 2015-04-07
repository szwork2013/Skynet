/**
 * 表单验证
 */
angular.module('app.services').factory('validateService', function($q, apiService) {

    var reg = {
        // 手机号
        mobile: /^\d{11}$/,
        // 邮箱
        email: /^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|info|INFO|biz|BIZ|name|NAME|pro|PRO|coop|COOP|aero|AERO|museum|MUSEUM)$/,
        // 身份证号
        idCard: /(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$)/,
        // 密码
        password: /^(?=.*[A-z]{2})(?=.*\d{2}).{8,16}$/,
        // 图片验证码
        validateCode: /^[A-z0-9]{4}$/,
        // 短信验证码
        mobileCode: /^\d{6}$/
    };

    var _check = function (key, text) {
        return reg[key].test(text);
    };

    var path = CM.info.service + '/validateCode?Appkey=' + CM.info.appkey + '&dumy=';

    return {
        // 获取表单校验正则表达式
        getRegExp: function (key) {
            return reg[key];
        },

        // 校验是否为邮箱格式
        isEmail: function (text) {
            return _check('email', text);
        },

        // 校验是否为手机格式
        isMobile: function (text) {
            return _check('mobile', text);
        },

        // 校验是否为身份证号格式
        isIdCardNumber: function (text) {
            return _check('idCard', text);
        },

        // 获取图片验证码对象
        getPicValidate: function () {
            var dumy = Math.random();
            var picValidate = {
                link: function () {
                    return path + dumy;
                },
                refresh: function () {
                    dumy = Math.random();
                }
            };
            return picValidate;
        }
    };
});

(function() {
    'use strict';
    var CM = window.CM = window.comall = window.comall || {};

    // 应用信息
    CM.info = {
        // API 服务端根地址
        // 测试
        // service: 'http://120.24.79.163:8080/html5',
        // servername: 'ftest',
        // 生产
        service: 'http://120.24.214.207:8080/html5',
        servername: 'online',
        // 应用 key，固定值
        appkey: 'CDF201410312222',
        // 系统类型： ['android', 'ios']
        os: undefined,
        // 系统版本
        osversion: undefined,
        // 应用版本
        appversion: '1.0.3',
        // 应用渠道标记
        channel: 'A2000',
        // 客户端唯一性标识，同一设备内唯一
        unique: undefined
    };

    // 登录用户信息
    CM.user = {
        // 用户 ID
        userid: undefined,
        // 用户的会话 ID
        usersession: undefined,
        // 用户账户名称
        username: undefined,
        // 用户级别名称（中文）
        level: undefined,
        // 用户积分，字符串类型，浮点数值
        emoney: undefined,
        // 用户分组 ID
        userGroupId: undefined,
        // 用户分组名称
        userGroupName: undefined
    };

    // 应用本地购物车信息
    CM.cart = {
        // 购物车商品总数
        total: 0,
        // 过期商品列表
        expiredProducts: undefined
    };

    // 应用配置信息
    CM.config = {
        // 是否提示更新
        notifyUpdate: true,
        // 是否启用节省流量模式
        saveTraffic: false
    };
    
    /**
     * 执行对象的浅拷贝，
     * 支持多个源对象，返回目标对象；
     * 其中，对象继承的属性不拷贝，
     * 在源对象中值为 undefined 的属性不拷贝，
     * 在过滤列表中声明的属性不拷贝。
     */
    CM.merge = function(dest) {
        var args = arguments,
            srcs = Array.prototype.slice.call(args, 1),
            hasFilters = false,
            filters, i, src, key, val;

        if (angular.isArray(srcs[srcs.length - 1])) {
            filters = srcs.pop().join(' ');
        }
        else if (angular.isString(srcs[srcs.length - 1])) {
            filters = srcs.pop();
        }

        if (filters) {
            filters = ' ' + filters.replace(/\s/g, ' ') + ' ';
            hasFilters = true;
        }

        for (i = 0; i < srcs.length; i++) {
            src = srcs[i];
            for (key in src) {
                val = src[key];
                if (Object.prototype.hasOwnProperty.call(src, key) &&
                    val !== undefined &&
                    !(hasFilters && filters.indexOf(' ' + key + ' ') > -1)) {
                    dest[key] = val;
                }
            }
        }

        return dest;
    };

    /**
     * 判断是否是手机号码
     */
    CM.isMobile = function(str) {
        return /^\d{11}$/.test(str);
    };
    
    /**
     * 判断是否是邮箱
     */
    CM.isEmail = function(str) {
        return /^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|info|INFO|biz|BIZ|name|NAME|pro|PRO|coop|COOP|aero|AERO|museum|MUSEUM)$/.test(str);
    };
    
    /**
     * 判断是否是身份证号
     */
    CM.isIdCardNumber = function(str) {
        return /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test(str) ||
               /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/.test(str);
    };

    /**
     * 使用随机方式生成并返回一个 UUID 字符串，其格式如下：
     *
     *     51159332-e156-15de-f641-ad94d1756a97
     *
     * 使用此方法生成的 UUID 只能保证在执行该方法的 javascript 环境中是唯一的，
     * 而不能保证真正全局唯一（既时间及空间唯一性），虽然其重复的可能性非常低。
     */
    CM.duuid = (function() {
        var guidArchive = {};

        function S4() {
            return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        }

        function create() {
            var guid = S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4();

            if (guidArchive[guid]) {
                guid = create();
            }
            else {
                guidArchive[guid] = true;
            }

            return guid;
        }

        return create;
    })();
})();

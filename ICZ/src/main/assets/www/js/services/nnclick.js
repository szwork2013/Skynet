/**
 * 99click监测
 */
angular.module('app.services')
    .factory('nnclickService', function() {
        var channel = CM.info.servername == 'online' ? CM.info.channel : 'test';

        return {
            // 初始化99click统计
            countOpen: function () {
                var nnclick = window.nnclick;
                nnclick && nnclick.countAppOpen('v' + CM.info.appversion, channel, 'name=zhongmian');
            },
            // 统计页面访问
            countView: function (title, param) {
                var nnclick = window.nnclick;
                nnclick && nnclick.countView(title, param);
            },
            // 统计点击
            countClick: function (title, target, param) {
                var nnclick = window.nnclick;
                nnclick && nnclick.countClick(title, target, param);
            }
        }
    });

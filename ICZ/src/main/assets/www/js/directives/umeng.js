/**
 * 友盟联合登录和分享
 */

angular.module('app')

    // 友盟分享
    .directive('umengShare', function factory() {
        return {
            restrict: 'A',          
            link: function($scope, $el, $attrs) {
               
                var opt = {
                    'data' : {
                            'content' : {
                                'text' : 'test', //要分享的文字
                            }
                    } 
                }

                $el.on('click', function () {
                    $(this).umshare(opt);
                });
            }
        };
    })
    
    // 友盟联合登录
    .directive('umengLogin', function factory() {
        return {
            restrict: 'A',          
            link: function($scope, $el, $attrs) {

                $el.on('click', function () {
                    $.fn.umshare.checkToken($attrs.umengLogin, function(user){
                        $.fn.umshare.tip('登录成功,token:' + user.token + ', uid:' + user.uid);
                        alert('登录成功,token:' + user.token + ', uid:' + user.uid);
                    });
                });
            }
        };
    })
;
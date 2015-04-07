angular.module('app', ['ionic',
                       
                       'app.services',
                       
                       'app.controllers.navigation',
                       'app.controllers.startup',
                       'app.controllers.home',
                       'app.controllers.brands',
                       'app.controllers.classification',
                       'app.controllers.cart',
                       'app.controllers.user',
                       'app.controllers.product',
                       'app.controllers.checkout',
                       'app.controllers.order',
                       'app.controllers.refund',
                       'app.controllers.search',
                       'app.controllers.subject'])

.run(function($ionicPlatform, localStorageService, cartService, popupService, nnclickService) {
    $ionicPlatform.ready(function() {
        CM.info.unique = window.device ? window.device.uuid : CM.duuid();
        CM.info.os = ionic.Platform.platform();
        CM.info.osversion = ionic.Platform.version();
        CM.user = localStorageService.get('user', {});
        CM.config = localStorageService.get('config', CM.config);
        cartService.shoppingCart();
        
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        if(window.cordova && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        }
        if(window.StatusBar) {
            // org.apache.cordova.statusbar required
            StatusBar.overlaysWebView();
            StatusBar.styleLightContent();
        }
        
        // Android处理返回键
        var lock = false;
        var backButtonHandler = function (e) {
            var navBar = document.querySelector('ion-nav-bar:not(.hide)'),
                back = navBar && 
                       (navBar.querySelector('.nav-bar-block[nav-bar=active] .left-buttons:not(.hide) .icon-back') ||
                       navBar.querySelector('.nav-bar-block[nav-bar=entering] .left-buttons:not(.hide) .icon-back'));
        
            if (back) {
                angular.element(back).triggerHandler('click');
                return;
            }
            if (lock) {
                return;
            }
            lock = true;
            popupService.confirmPopup('退出', '您是否要退出中免商城?')
            .then(function (res) {
                lock = false;
                if(res) {
                    navigator.app && navigator.app.exitApp();
                }
            });
        };
        $ionicPlatform.registerBackButtonAction(backButtonHandler, 100);

        // 统计应用开启
        nnclickService.countOpen();
    });
})

/* 设置友盟appkey,暂未使用
.config(function(apiServiceProvider) {
    window.umappkey = '54a36e6bfd98c555c90005f5';
})*/

.config(function(apiServiceProvider) {
    apiServiceProvider.serviceAddress = CM.info.service;
})

.config(function($stateProvider, $urlRouterProvider, $httpProvider, apiServiceProvider) {

    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

    // Override $http service's default transformRequest
    $httpProvider.defaults.transformRequest = [function(data) {
        return angular.isObject(data) && String(data) !== '[object File]' ?
            apiServiceProvider.formatUrlParameter(data) : data;
    }];

    angular.extend($httpProvider.defaults.headers.common, {
        Appkey:      function() { return CM.info.appkey; },
        Os:          function() { return CM.info.os; },
        servername:  function() { return CM.info.servername; },
        Osversion:   function() { return CM.info.osversion; },
        Appversion:  function() { return CM.info.appversion; },
        Unique:      function() { return CM.info.unique; },
        Userid:      function() { return CM.user.userid; },
        Usersession: function() { return CM.user.usersession; }
    });

    // Ionic uses AngularUI Router which uses the concept of states
    // Learn more here: https://github.com/angular-ui/ui-router
    // Set up the various states which the app can be in.
    // Each state's controller can be found in controllers.js
    $stateProvider

        .state('startup', {
            url: '',
            templateUrl: 'templates/startup/startup.html',
            controller: 'startupCtrl'
        })
        
        .state('tutorial', {
            url: '/tutorial/:data',
            templateUrl: 'templates/startup/tutorial.html',
            controller: 'tutorialCtrl'
        })

        .state('advertise', {
            url: '/advertise/:data',
            templateUrl: 'templates/startup/advertise.html',
            controller: 'advertiseCtrl'
        })
    
        .state('login', {
            cache: false,
            url: '/login/:returnState',
            templateUrl: 'templates/user/login.html',
            controller: 'loginCtrl'
        })
        
        .state('register', {
            cache: false,
            url: '/register',
            templateUrl: 'templates/user/register.html',
            controller: 'registerCtrl'
        })

        .state('protocol', {
            cache: true,
            url: '/protocol',
            templateUrl: 'templates/user/protocol.html'
        })

        .state('findpwd', {
            cache: false,
            url: '/findpwd',
            templateUrl: 'templates/user/findpwd.html',
            controller: 'findPwdCtrl'
        })

        .state('findpwdverify', {
            cache: true,
            url: '/findpwdverify/:phone',
            templateUrl: 'templates/user/findpwdverify.html',
            controller: 'findPwdVerifyCtrl'
        })

        .state('findpwdreset', {
            cache: true,
            url: '/findpwdreset/:phone/:code',
            templateUrl: 'templates/user/findpwdreset.html',
            controller: 'findPwdResetCtrl'
        })

        .state('home', {
            cache: true,
            url: '/home',
            templateUrl: 'templates/home/home.html',
            controller: 'homeCtrl'
        })
        
        .state('brands', {
            cache: true,
            url: '/brands',
            templateUrl: 'templates/brand/brands.html',
            controller: 'brandsCtrl'
        })
        
        .state('classification', {
            cache: true,
            url: '/classification',
            templateUrl: 'templates/classification/classification.html',
            controller: 'classificationCtrl'
        })
        
        .state('cart', {
            cache: false,
            url: '/cart',
            templateUrl: 'templates/cart/cart.html',
            controller: 'cartCtrl'
        })
        
        .state('order', {
            cache: false,
            url: '/order/:cartId/:data',
            templateUrl: 'templates/checkout/order.html',
            controller: 'checkoutCtrl'
        })
        
        .state('order.info', {
            url: '/info',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/orderinfo.html' 
                }
            }
        })
        
        .state('order.consignee', {
            url: '/consignee',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/user/consigneelist.html'
                }
            }
        })

        .state('order.editconsignee', {
            url: '/editconsignee',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/user/editconsignee.html'
                }
            }
        })

        .state('order.timeofreceipt', {
            url: '/timeofreceipt',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/timeofreceipt.html'
                }
            }
        })

        .state('order.coupon', {
            url: '/coupon',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/coupon.html'
                }
            }
        })
        
        .state('order.prepaidcard', {
            url: '/prepaidcard',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/prepaidcard.html'
                }
            }
        })

        .state('order.invoice', {
            url: '/invoice',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/invoice.html'
                }
            }
        })

        .state('order.invoicedetail', {
            url: '/invoicedetail',
            views: {              
                'order.tab': {
                    templateUrl: 'templates/checkout/invoicedetail.html'
                }
            }
        })

        .state('ordersuccess', {
            cache: false,
            url: '/ordersuccess/:orderId/:aliasId/:price',
            templateUrl: 'templates/checkout/ordersuccess.html',
            controller: 'orderSuccessCtrl'
        })

        .state('orderlist', {
            cache: false,
            url: '/orderlist',
            templateUrl: 'templates/checkout/orderlist.html',
            controller: 'orderListCtrl'
        })

        .state('orderdetail', {
            cache: false,
            url: '/orderdetail/:id',
            templateUrl: 'templates/checkout/orderdetail.html',
            controller: 'orderDetailCtrl'
        })

        .state('refundlist', {
            cache: false,
            url: '/refundlist',
            templateUrl: 'templates/user/refundlist.html',
            controller: 'refundListCtrl'
        })

        .state('refunddetail', {
            cache: false,
            url: '/refunddetail',
            templateUrl: 'templates/user/refunddetail.html',
            controller: 'refundDetailCtrl'
        })

        .state('refundapply', {
            cache: false,
            url: '/refundapply',
            templateUrl: 'templates/user/refundapply.html',
            controller: 'refundApplyCtrl'
        })

        .state('pay', {
            cache: false,
            url: '/pay/:id',
            templateUrl: 'templates/checkout/pay.html',
            controller: 'payCtrl'
        })

        .state('paysuccess', {
            cache: false,
            url: '/paysuccess/:id',
            templateUrl: 'templates/checkout/paysuccess.html',
            controller: 'paySuccessCtrl'
        })
        
        .state('user', {
            cache: false,
            url: '/user',
            templateUrl: 'templates/user/user.html',
            controller: 'userCtrl'
        })

        .state('settings', {
            url: '/settings',
            templateUrl: 'templates/user/settings.html',
            controller: 'settingCtrl'
        })      

        .state('company', {
            url: '/company',
            templateUrl: 'templates/user/company.html'
        })

        .state('about', {
            url: '/about',
            templateUrl: 'templates/user/about.html'
        })

        .state('help', {
            url: '/help',
            templateUrl: 'templates/user/help.html'
        })

        .state('collections', {
            cache: false,
            url: '/collections',
            templateUrl: 'templates/user/collections.html',
            controller: 'collectionsCtrl'
        })

        .state('cardmanagement', {
            cache: false,
            url: '/cardmanagement',
            templateUrl: 'templates/user/cardmanagement.html'
        })
        
        .state('coupons', {
            cache: false,
            url: '/coupons',
            templateUrl: 'templates/user/coupons.html',
            controller: 'couponsCtrl'
        })
        
        .state('prepaidcard', {
            cache: false,
            url: '/prepaidcard',
            templateUrl: 'templates/user/prepaidcard.html',
            controller: 'prepaidCardCtrl'
        })

        .state('accountmanagement', {
            url: '/accountmanagement',
            templateUrl: 'templates/user/accountmanagement.html'
        })

        .state('basicinfo', {
            cache: false,
            url: '/basicinfo',
            templateUrl: 'templates/user/basicinfo.html',
            controller: 'userInfoCtrl'
        })

        .state('accountsecurity', {
            url: '/accountsecurity',
            templateUrl: 'templates/user/accountsecurity.html'
        })

        .state('accountmail', {
            url: '/accountmail',
            templateUrl: 'templates/user/accountmail.html'
        })

        .state('modifypwd', {
            cache: false,
            url: '/modifypwd',
            templateUrl: 'templates/user/modifypwd.html',
            controller: 'modifyPwdCtrl'
        })

        .state('modifyphone', {
            cache: false,
            url: '/modifyphone/:phone',
            templateUrl: 'templates/user/modifyphone.html',
            controller: 'modifyPhoneCtrl'
        })

        .state('verifyphone', {
            cache: false,
            url: '/verifyphone',
            templateUrl: 'templates/user/verifyphone.html'
        })

        .state('consignee', {
            cache: false,
            url: '/consignee',
            templateUrl: 'templates/user/consignee.html',
            controller: 'consigneeCtrl'
        })

        .state('consignee.consigneelist', {
            url: '/consigneelist',
            views: {              
                'consignee.tab': {
                    templateUrl: 'templates/user/consigneelist.html'
                }
            }
        })

        .state('consignee.editconsignee', {
            url: '/editconsignee',
            views: {              
                'consignee.tab': {
                    templateUrl: 'templates/user/editconsignee.html'
                }
            }
        })

        .state('service', {
            url: '/service',
            templateUrl: 'templates/user/service.html'
        })

        .state('message', {
            url: '/message',
            templateUrl: 'templates/user/message.html'
        })
        
        .state('scan', {
            url: '/scan',
            templateUrl: 'templates/scan/scan.html'
        })
        
        .state('search', {
            cache: false,
            url: '/search',
            templateUrl: 'templates/search/search.html',
            controller: 'searchCtrl'
        })
        
        .state('product', {
            cache: true,
            url: '/product/:id',
            templateUrl: 'templates/product/product.html',
            controller: 'productCtrl'
        })          
        
        .state('product.detail', {
            url: '/detail',
            views: {              
                'product.tab': {
                    templateUrl: 'templates/product/productdetail.html' 
                }
            }
        })

        .state('product.info', {
            url: '/info',
            views: {              
                'product.tab': {
                    templateUrl: 'templates/product/productinfo.html' 
                }
            }
        })

        .state('product.size', {
            url: '/size',
            views: {              
                'product.tab': {
                    templateUrl: 'templates/product/productsize.html' 
                }
            }
        })

        .state('zoompic', {
            url: '/zoompic/:src',
            templateUrl: 'templates/product/productzoompic.html',
            controller: 'zoomPicCtrl'
        })
        
        .state('productlist', {
            url: '/productlist/:type/:id/:title/:filter',
            templateUrl: 'templates/product/productlist.html',
            controller: 'productListCtrl'
        })

        .state('productlist.view', {
            url: '/view',
            views: {              
                'productlist.tab': {
                    templateUrl: 'templates/product/productlistview.html' 
                }
            }
        })

        .state('productlist.filter', {
            url: '/filter',
            views: {              
                'productlist.tab': {
                    templateUrl: 'templates/product/productlistfilter.html' 
                }
            }
        })

        .state('productlist.select', {
            url: '/select',
            views: {              
                'productlist.tab': {
                    templateUrl: 'templates/product/productlistselect.html' 
                }
            }
        })
        
        .state('subject', {
            url: '/subject/:id/:title',
            templateUrl: 'templates/subject/subject.html',
            controller: 'subjectCtrl'
        })

    ;

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise("/home");

});


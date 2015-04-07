angular.module('app', ['ionic',
                       'app.controllers.navigation',
                       'app.controllers.index'])

.run(function($ionicPlatform) {
    $ionicPlatform.ready(function() {

    });
})

.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

    // Ionic uses AngularUI Router which uses the concept of states
    // Learn more here: https://github.com/angular-ui/ui-router
    // Set up the various states which the app can be in.
    // Each state's controller can be found in controllers.js
    $stateProvider

        .state('index', {
            cache: true,
            url: '/index',
            templateUrl: 'templates/index/index.html',
            controller: 'indexCtrl'
        })

        .state('member.info', {
            cache: true,
            url: '/info',
            templateUrl: 'templates/member/info.html',
            controller: 'memberInfoCtrl'
        })
    ;

    // if none of the above states are matched, use this as the fallback
    $urlRouterProvider.otherwise("/index");

});


/**
 * 主题页控制器
 */
angular.module('app.controllers.subject', [])
    
    // 主题页控制器
    .controller('subjectCtrl', function($scope, $stateParams, $sce, $ionicScrollDelegate, 
                                        subjectService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('专题页', 'id=' + $stateParams.id);

        var scroll = $ionicScrollDelegate.$getByHandle('subject-scroll');
    
        angular.extend($scope, {
            // 主题id
            subjectId: $stateParams.id,
            // 主题标题
            title: decodeURIComponent($stateParams.title),
            loading: true,
            // 主题数据
            data: {},
            // 主题模板
            template: '',
            scrollTop: function () {
                scroll.scrollTop(true);
            },
            compileHtml: function(html) {
                return $sce.trustAsHtml(html);
            }
        });
        
        var loadSubject = function () {
            subjectService.getSubject($stateParams.id)
            .then(function (data) {
                $scope.data = data.subject;
                $scope.title = data.subject.title;
                $scope.template = 'subject-template' + $scope.data.subjectStyle + '.html';
            }).finally (function() {
                $scope.loading = false;
            });
        };
        
        loadSubject();
    })

;
/**
 * 搜索控制器
 */
angular.module('app.controllers.search', [])

    // 搜索页控制器
    .controller('searchCtrl', function($scope, $state, searchService, nnclickService) {

        // 统计页面访问
        nnclickService.countView('搜索页');

        angular.extend($scope, {
            data: {
                count: 10,
                page: 0,
                moreData: false,
                products: [],
                history: searchService.getSearchHistory()
            },
            searchKeyword: '',
            keyword: '',
            inSearch: false,
            searchResult: false,
            goBack: function () {
                $state.go('home');
            },
            searchSubmit: function() {
                if (!$scope.searchKeyword) {
                    $scope.searchResult = false;
                    return false;
                }
                
                searchService.addSearchHistory($scope.searchKeyword);
                // $scope.data.history = searchService.getSearchHistory();
                search($scope.searchKeyword);
            },
            searchByHistory: function (index) {
                search($scope.data.history[index]);
            },
            searchMore: function() {
                search($scope.keyword);
            }
        });
        
        var search = function(keyword) {
            $state.go('productlist.view', {
                type:'keyword',
                id:keyword,
                title: encodeURIComponent(keyword),
                filter: ''
            });
            /*if (reset) {
                $scope.data.page = 0;
                $scope.data.moreData = false;
                $scope.data.products = [];
                $scope.inSearch = true;
            }
        
            if (keyword) {
                $scope.searchKeyword = keyword.trim();
            }
            
            $scope.keyword = $scope.searchKeyword;
            $scope.searchResult = true;
            
            searchService.search($scope.searchKeyword, $scope.data.page, $scope.data.count, $scope.data.order)
            .success(function(data) {
                $scope.data.moreData = data.slp.length == $scope.data.count;
                $scope.data.products = $scope.data.products.concat(data.slp);
                $scope.data.page += 1;
                $scope.inSearch = false;
                $scope.$broadcast('scroll.infiniteScrollComplete');
            })
            .error(function(data) {
                $scope.inSearch = false;
                $scope.$broadcast('scroll.infiniteScrollComplete');
            });*/
        }
        
    })
;
/**
 * 品牌页控制器
 */
angular.module('app.controllers.brands', [])

    .controller('brandsCtrl', function($scope, $state, $ionicScrollDelegate, productService, nnclickService) {

        // 统计页面访问
        nnclickService.countView('品牌页');

        var scroll = $ionicScrollDelegate.$getByHandle('brand-scroll');
        
        angular.extend($scope, {
            data: null,
            loading: true,
            scrollTo: function (top) {
                scroll.scrollTo(0, top, true);
            },
            goTo: function (id, title) {
                $state.go('productlist.view', {
                    type:'brand',
                    id:id,
                    title: encodeURIComponent(title),
                    filter: ''
                });
            }
        });
        
        var updateData = function (data) {
            var brands = data.brands,
                commends = data.commends,
                set = {},
                list = [],
                letterCount = 0;
            for (var i in brands) {
                var brand = brands[i];
                brand.href = '#/productlist/brand/' + brand.id + '/' + encodeURIComponent(brand.name) + '//view';
                
                if (!set[brand.initial]) {
                    set[brand.initial] = 1;
                    list.push({
                        letter: brand.initial,
                        brands: []
                    });
                    letterCount++;
                }
                list[letterCount-1].brands.push(brand);
            }
            for (var i in commends) {
                var commend = commends[i];
                commend.href = '#/productlist/brand/' + commend.brandId + '/' + encodeURIComponent(commend.brandName) + '//view';
            }
            $scope.data = {
                brands: list,
                commends: commends
            };
            $scope.loading = false;
        };

        // 加载数据
        productService.brands().then(updateData);
        
    })
;

/**
 * 商品分类控制器
 */
angular.module('app.controllers.classification', [])

    .controller('classificationCtrl', function($scope, $state, $ionicSlideBoxDelegate, 
                                               productService, clientService, nnclickService) {
    
        // 统计页面访问
        nnclickService.countView('分类页');

        // 更新slide box,防止返回缓存页面时渲染错误
        $scope.$on("$ionicView.afterEnter", function() {
            $ionicSlideBoxDelegate.update();
        });
    
        angular.extend($scope, {
            data: null,
            loading: true,
            slideChanged: function (index) {
                for (var i in this.data) {
                    var button = this.data[i];
                    if (i == index) {
                        button.state = 'button-confirm';
                    } else {
                        button.state = 'button-cancel';
                    }
                }
            },
            changeSlide: function (index) {
                $ionicSlideBoxDelegate.$getByHandle('category-slide').select(index);
            },
            goTo: function (subcategory, parentId) {
                $state.go('productlist.view', {
                    type: 'category',
                    id: subcategory.id,
                    title: encodeURIComponent(subcategory.title),
                    filter:encodeURIComponent(JSON.stringify(filters[parentId]))
                });
            }
        });
        
        // key:二级分类id, value:三级分类集合
        var filters = {};
        
        var updateData = function (data) {
            data = data.categories_pictext;
            
            for (var i in data) {
                data[i].state = i==0 ? 'button-confirm' : 'button-cancel';
                var categories = data[i].sub_categories;
                for (var j in categories) {
                    var category = categories[j],
                        titles = category.title.split('|');
                        
                    category.cnTitle = titles[0];
                    category.enTitle = titles[1];
                    
                    var filter = {},
                        subcategories = category.sub_categories;
                    for (var k in subcategories) {
                        var subcategory = subcategories[k];
                        filter[subcategory.id] = subcategory.title;
                    }
                    filters[category.id] = filter;
                }
            }
            
            $scope.data = data;
            $scope.loading = false;
        };

        // 加载数据
        productService.classification().then(updateData);
        
    })
;

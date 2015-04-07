/**
 * 商品详情控制器
 */
angular.module('app.controllers.product', [])

    // 图片放大缩小效果控制器
    .controller('zoomPicCtrl', function($scope, $stateParams) {
        angular.extend($scope, {
            // 图片scr
            src: decodeURIComponent($stateParams.src.replace(/_pic500_/, '_pic900_'))
        });     
    })
    
    // 商品详情控制器
    .controller('productCtrl', function($scope, $state, $stateParams, $sce, $compile, $ionicModal, $ionicSlideBoxDelegate, $timeout,
                                        productService, varietyService, cartService, popupService, collectService, userService, nnclickService) {
        
        /* 更新页面缓存
        $scope.$on("$ionicView.beforeEnter", function() {
            /product\/(\d+)(?=\/)/.exec(window.location.hash);
            var id = RegExp.$1;
            if ($scope.productId != id) {
                console.log($scope.productId);
                $scope.productId = id;
                loadProduct(id);
                loadProductExtInfo(id);
            }
        }); */

        // 更新slide box,防止返回缓存页面时渲染错误
        $scope.$on("$ionicView.afterEnter", function() {
            $ionicSlideBoxDelegate.update();
        });

        // 统计页面访问
        nnclickService.countView('商品详情页', 'id=' + $stateParams.id);
        
        angular.extend($scope, {
            // 商品id
            productId: $stateParams.id,
            // 商品对象
            product: null,
            // 商品扩展属性对象
            productExt: null,
            // 商品信息按钮组
            infoButtons: [
                {text: '商品介绍', state: 'button-confirm'},
                {text: '品牌故事', state: 'button-cancel'},
                {text: '保养知识', state: 'button-cancel'},
                {text: '小贴士', state: 'button-cancel'}
            ],
            // 商品信息选项卡状态
            infoState: 0,
            // 切换商品信息选项卡
            showInfoContent: function(infoState) {

                $scope.infoState = infoState;

                for (var i in $scope.infoButtons) {
                    var button = $scope.infoButtons[i];
                    if (i == infoState) {
                        button.state = 'button-confirm';
                    } else {
                        button.state = 'button-cancel';
                    }
                }

            },
            // 添加到收藏
            addToCollection: function () {
                if (!userService.hasLogined()) {
                    popupService.confirmPopup('添加收藏失败', '您还未登录,请先登录')
                    .then(function (res) {
                        if(res) {
                            $state.go('login', {returnState:true});
                        }
                    });
                    return;
                }
                collectService.add($scope.productId)
                .success(function () {
                    popupService.alertPopup('收藏添加成功', $scope.product.shortName + '<br />已添加到收藏');
                    $scope.product.collect = true;
                })
                .error(function (data) {
                    popupService.alertPopup('收藏添加失败', data.error.text);
                });
            },
            // 移除收藏
            removeCollection: function () {
                popupService.confirmPopup('删除收藏', '是否要删除收藏？')
                .then(function (res) {
                    if(res) {
                        collectService.del($scope.productId)
                        .success(function (data) {
                            popupService.alertPopup('收藏删除成功', $scope.product.shortName + '<br />已从收藏中删除');
                            $scope.product.collect = false;
                        })
                        .error(function(data) {
                            popupService.alertPopup('收藏删除失败', data.error.text);
                        });
                    }
                });
            },

            // 一品多款信息
            variety: null,
            // 购物车信息
            cart: CM.cart,
            // 正在加入购物车
            loading: false,
            // 加入购物车动画
            addToCartAnimation: false,
            // 弹出层标题
            modalTitle: '加入购物车',
            // 加入购物车
            confirmEdit: function () {
                $scope.loading = true;
                cartService.addCartItem($scope.variety.selectedVariety, $scope.variety.count)
                .success(function (data) {
                    // 统计页面点击
                    nnclickService.countClick('商品详情页', '加入购物车', 'id=' + $scope.productId);

                    $scope.addToCartAnimation = true;
                    $timeout(function () {
                        $scope.varietyModal.hide();
                    }, 500);
                    setTimeout(function () {
                        $scope.addToCartAnimation = false;
                    }, 800);
                })
                .error(function (data) {
                    popupService.alertPopup('添加购物车失败', data.error.text);
                })
                .finally(function (data) {
                    $scope.loading = false;
                });
            }
        });
        
        // 获取商品信息
        var loadProduct = function (id) {
            $scope.product = null;
            productService.productInfo(id)
            .success(function(data, status, headers, config) {
                data.buyPrice = data.product_simpledesc.prices[0].value;
                if (data.product_simpledesc.prices[1]) {
                    data.marketPrice = data.product_simpledesc.prices[1].value;
                }
                data.name = data.shortName;
                
                for (var j in data.product_dese) {
                    var property = data.product_dese[j]; 
                    if (property.key == '品牌') {
                        data.brand = property.value;   
                        break;                
                    }
                }
                
                $scope.product = data;
                $ionicSlideBoxDelegate.select(0);
                $ionicSlideBoxDelegate.update(0);
            })
            .error(function(data, status, headers, config) {
            });
        };

        // 获取商品扩展信息
        var loadProductExtInfo = function (id) {
            $scope.productExt = null;
            productService.productExtInfo(id)
            .success(function(data, status, headers, config) {
                $scope.branddesc = data.brandAttrs.branddesc && 
                                   data.brandAttrs.branddesc.replace(/src=\"([^\"]*)\"/gm, 'cm-lazyload="\'$1\'"')
                                                            .replace(/<img/gm, '<img cm-zoom-pic');
                $scope.baoyangchangshi = data.catgoryAttrs.baoyangchangshi && 
                                         data.catgoryAttrs.baoyangchangshi.replace(/src=\"([^\"]*)\"/gm, 'cm-lazyload="\'$1\'"')
                                                                          .replace(/<img/gm, '<img cm-zoom-pic');                            
                $scope.tips = data.catgoryAttrs.gouwuxiaotieshim && 
                              data.catgoryAttrs.gouwuxiaotieshim.replace(/src=\"([^\"]*)\"/gm, 'cm-lazyload="\'$1\'"')
                                                                .replace(/<img/gm, '<img cm-zoom-pic');

                $scope.productExt = data;

                if ($scope.productExt.productSize != null) {
                
                    var colcount = $scope.productExt.productSize.colcount;
                    var sizedata = $scope.productExt.productSize.data;
                    var newsizedata = [];
                    var tempdata = [];
                    
                    for(var i = 0; i< sizedata.length; i++){
                        tempdata.push(sizedata[i]);
                        
                        if(tempdata.length%colcount === 0){
                            newsizedata.push(tempdata);
                            tempdata = [];
                        }
                        
                    }

                    $scope.newsizedata = newsizedata;

                }
                
                // 一品多款数据初始化
                $scope.variety = varietyService.initVariety(data.moreStyles, $scope.productId);
            })
            .error(function(data, status, headers, config) {
            });
        };
        
        // 加入购物车弹出层
        $ionicModal.fromTemplateUrl('templates/product/productvariety.html', {
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
            $scope.varietyModal = modal;
        });
        
        $scope.$on('$destroy', function() {
            $scope.varietyModal.remove();
        });
        
        // 切换一品多款后,重新加载商品图片
        $scope.$on('modal.hidden', function() {
            if ($scope.variety && $scope.variety.selectedVariety != $scope.productId) {
                $scope.productId = $scope.variety.selectedVariety;
                loadProduct($scope.productId);
            }
        });
        
        // 加载数据
        loadProduct($scope.productId);
        loadProductExtInfo($scope.productId);
    })

    // 列表控制器
    .controller('productListCtrl', function($scope, $state, $stateParams, $ionicScrollDelegate, 
                                            productService, nnclickService) {
        var pageTypes = {
                category: '分类列表页',
                brand: '品牌列表页',
                keyword: '搜索列表页'
            },
            params = {
                type: $stateParams.type,
                id: $stateParams.id,
                title: decodeURIComponent($stateParams.title),
                filter: $stateParams.filter ? JSON.parse(decodeURIComponent($stateParams.filter)) : null,
            },
            orderFilter = {
                'mer_sellcount---string---desc': '销量降序',
                'mer_sellcount---string---asc': '销量升序',
                'mer_price---string---desc': '价格降序',
                'mer_price---string---asc': '价格升序',
                'mndsModifyDate---string---desc': '上架时间降序',
                'mndsModifyDate---string---asc': '上架时间升序'
            };
        
        if (params.type == 'category' && params.filter == null) {
            params.filter = {};
            params.filter[params.id] = params.title;
            $scope.disableCategoryFilter = true;
        }

        // 统计页面访问
        nnclickService.countView(pageTypes[params.type], 'id=' + params.id);
        
        angular.extend($scope, {
            cart: CM.cart,
            // 来源类型,keyword:搜索页,category:分类页,brand:品牌页
            type: params.type,
            // 页面类型
            pageType: pageTypes[params.type],
            // 请求参数对象
            data: {
                keyword: null,
                category: null,
                brand: null,
                beginPrice: null,
                endPrice: null,
                order: 'mer_sellcount---string---desc',
                page: 1,
                perPage: 12
            },
            // 商品集合
            products: [],
            // 无限滚动控制
            moreData: false,
            // 排序候选
            order: {
                name: '排序',
                filter: orderFilter
            },
            // 筛选候选
            filters: {
                category: {
                    name: '分类',
                    filter: params.filter,
                    value: '',
                    checked: true
                }
            },
            // 筛选页展示顺序
            filterOrder: ['category'],
            // 筛选页的价格筛选
            priceFilter: {
                beginPrice: null,
                endPrice: null,
            },
            // 筛选值选择页展示数据
            filterSelection: {
                filterKey: '',
                selected: '',
                selections: []
            },
            // 数据加载中标记
            inSearch: true,
            // 标题
            title: params.title,
            // 状态转换
            goTo: function(state) {
                $state.go(state);
            },
            /**
             * 加载商品列表
             * @param reset     是否重置分页
             * @param initial   是否初次加载,获取筛选条件
             * @param nocache   是否初次加载,获取筛选条件
             */
            search: function(reset, initial, noCache) {
                $scope.moreData = false;
                if (reset) {
                    $scope.data.page = 1;
                    $scope.inSearch = !noCache;
                }
                
                productService.productList($scope.data, noCache)
                .then(function(data) {
                    if (reset) {
                        $scope.products = data.slp;
                    } else {
                        $scope.products = $scope.products.concat(data.slp);
                    }
                    $scope.data.page += 1;
                    // 重置内容区高度
                    $ionicScrollDelegate.resize();
                    $scope.moreData = $scope.products.length < data.total_count;
                    
                    // 获取筛选条件
                    if (initial) {
                        if ($scope.type != 'brand') {
                            $scope.filters.brand = {
                                name: '品牌',
                                filter: null,
                                value: '',
                                checked: true
                            };
                            $scope.filterOrder.push('brand');
                        }
                        
                        for (var key in $scope.filters) {
                            
                            if (key != $scope.type) {
                                var filter = {};
                                for (var i in data[key]) {
                                    var item = data[key][i];
                                    filter[item.id] = item.name;
                                }
                                $scope.filters[key].filter = filter;
                            }
                        }
                        
                        for (var i in data['conditions']) {
                            var condition = data['conditions'][i];
                            $scope.data[condition.innername] = null;
                            $scope.filterOrder.push(condition.innername);
                            
                            var filter = {};
                            for (var j in condition.standardValue) {
                                var value = condition.standardValue[j];
                                filter[value] = value;
                            }
                            $scope.filters[condition.innername] = {
                                name: condition.outername,
                                filter: filter,
                                value: '',
                                checked: true
                            }
                        }
                    }
                }).finally (function() {
                    $scope.inSearch = false;
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                    $scope.$broadcast('scroll.refreshComplete');
                });
            },
            // 初始化筛选页
            initFilter: function () {
                var view = $scope.filters;
                
                for (var key in view) {
                    var filter = view[key];
                    filter.value = $scope.data[key];
                }
                $scope.priceFilter = {
                    beginPrice: $scope.data.beginPrice,
                    endPrice: $scope.data.endPrice,
                };
                
                $scope.goTo('productlist.filter');
            },
            // 应用筛选页结果
            applyFilter: function () {
                for (var key in $scope.filters) {
                    var filter = $scope.filters[key];
                    if (filter.checked) {
                        $scope.data[key] = filter.value;
                        if (key == 'category' && $scope.type == key) {
                            $scope.title = filter.filter[filter.value];
                        }
                    } else {
                        $scope.data[key] = null;
                    }
                }
                if (/^\+?[1-9][0-9]*$/.test($scope.priceFilter.beginPrice)) {
                    $scope.data.beginPrice = $scope.priceFilter.beginPrice;
                }
                if (/^\+?[1-9][0-9]*$/.test($scope.priceFilter.endPrice)) {
                    $scope.data.endPrice = $scope.priceFilter.endPrice;
                }
                $scope.search(true);
                $scope.goTo('productlist.view');
            },
            // 重置筛选页
            cancelFilter: function () {
                for (var key in $scope.filters) {
                    if (key != 'category' || $scope.type != 'category') {
                        $scope.data[key] = null;
                    } else {
                        $scope.data[key] = params.id;
                    }
                }
                $scope.data.beginPrice = '';
                $scope.data.endPrice = '';
                $scope.search(true);
                if ($scope.type == 'category') {
                    $scope.title = params.title;
                }
                $scope.goTo('productlist.view');
            },
            // 初始化筛选项选择页
            initFilterSelections: function (filterKey) {
                $scope.filterSelection.filterKey = filterKey;
                if (filterKey == 'order') {
                    $scope.filterSelection.selections = $scope.order.filter;
                    $scope.filterSelection.selected = $scope.data[filterKey];
                } else {
                    $scope.filterSelection.selections = $scope.filters[filterKey].filter;
                    $scope.filterSelection.selected = $scope.filters[filterKey].value;
                }
                $scope.goTo('productlist.select');
            },
            // 应用筛选项
            applyFilterSelection: function () {
                var key = $scope.filterSelection.filterKey,
                    value = $scope.filterSelection.selected;
                    
                if (key == 'order') {
                    $scope.data[key] = value;
                    $scope.goTo('productlist.view');
                    $scope.search(true);
                } else {
                    var filter = $scope.filters[key];
                    filter.value = value;
                    filter.checked = true;
                    $scope.goTo('productlist.filter');
                }
            },
            // 清除筛选项
            cancelFilterSelection: function () {
                var key = $scope.filterSelection.filterKey;
                if (key == 'order') {
                    $scope.goTo('productlist.view');
                } else {
                    var filter = $scope.filters[key];
                    if (key != 'category' || $scope.type != 'category') {
                        filter.value = null;
                        filter.checked = false;
                    }
                    $scope.goTo('productlist.filter');
                }
            }
        });
        
        // 初次加载
        $scope.data[$scope.type] = params.id;
        $scope.search(true, true);
    })

;
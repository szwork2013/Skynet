/**
 * 购物车控制器
 */
angular.module('app.controllers.cart', [])

    .controller('cartCtrl', function($scope, $state, $ionicModal, productService, cartService, 
                                     userService, varietyService, popupService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('购物车');

        angular.extend($scope, {
            // 购物车信息
            cart: null,
            // 被编辑商品id
            productId: null,
            // 被编辑商品对象
            product: null,
            // 被编辑一品多款信息
            variety: null,
            // 是否通信中
            loading: true,
            // 是否编辑中
            editMode: false,
            // 弹出层标题
            modalTitle: '编辑商品',
            // 切换全选
            toggleAllSelected: function(selected) {
                var cart = $scope.cart;
                if (arguments.length) {
                    cart.allSelected = selected;
                }
                for(var i in cart.cartItems) {
                    cart.cartItems[i].selected = cart.allSelected;
                }

                calculateCartInfo();
            },
            // 切换商品选中
            changeSelected: function() {
                var cart = $scope.cart;
                cart.allSelected = true;
                for(var i in cart.cartItems) {
                    if (!cart.cartItems[i].selected) {
                        cart.allSelected = false;
                        break;
                    }
                }
                
                calculateCartInfo();
            },
            // 编辑商品
            editCartItem: function (product) {
                var productId = product.id;
                
                $scope.variety = null;
                $scope.varietyModal.show();
                productService.productVariety(productId)
                .success(function (data) {
                    $scope.productId = productId;
                    $scope.product = product;
                    $scope.variety = varietyService.initVariety(data.styles, productId);
                    $scope.variety.count = $scope.product.count;
                })
                .error(function () {
                });
            },
            // 应用编辑
            confirmEdit: function () {
                $scope.varietyModal.hide();
                if($scope.variety.selectedVariety == $scope.productId) {
                    var amount = $scope.variety.count - $scope.product.count;
                    if (!amount) {
                        return;
                    }
                    $scope.loading = true;
                    cartService.addCartItem($scope.productId, amount)
                    .success(function () {
                        loadCart();
                    })
                    .error(function (data) {
                        popupService.alertPopup('编辑失败', data.error.text);
                        $scope.loading = false;
                    });
                } else {
                    $scope.loading = true;
                    cartService.addCartItem($scope.variety.selectedVariety, $scope.variety.count)
                    .success(function () {
                        cartService.delCartItem($scope.cart.cartId, $scope.productId, $scope.product.count)
                        .success(function () {
                            loadCart();
                        })
                        .error(function (data) {
                            popupService.alertPopup('编辑失败', data.error.text);
                            $scope.loading = false;
                        });
                    })
                    .error(function (data) {
                        popupService.alertPopup('编辑失败', data.error.text);
                        $scope.loading = false;
                    });
                }
            },
            // 删除购物车项
            delCartItem: function (product) {
                popupService.confirmPopup('删除商品', '您是否要从购物车删除<br />' + product.shortname + '?')
                .then(function (res) {
                    if(res) {
                        $scope.loading = true;
                        
                        cartService.delCartItem($scope.cart.cartId, product.id, product.count)
                        .success(function () {
                            loadCart();
                        })
                        .error(function () {
                            $scope.loading = false;
                        });
                    }
                });
            },

            // 显示选择赠品弹出层
            showPresentSelection: function (product) {
                $scope.product = product;
                var presents = product.presents;
                for (var i in presents) {
                    var present = presents[i];
                    present.selectedTemp = present.selected;
                }
                $scope.presentModal.show();
            },

            // 获取剩余可选单品赠品总数
            getRemainPresentTotal: function (product, temp) {
                if (!product || !product.presentMaxCount || !product.presents) {
                    return 0;
                }

                var presents = product.presents,
                    selectedTotal = 0;
                for (var i in presents) {
                    var present = presents[i];
                    selectedTotal += present[temp ? 'selectedTemp' : 'selected'];
                }
                return product.presentMaxCount - selectedTotal;
            },

            // 获取剩余可选赠品数量
            getRemainPresentCount: function (product, present) {
                var remain = present.selectedTemp + $scope.getRemainPresentTotal(product, true),
                    sellCount = present.sellCount;
                return sellCount < remain ? sellCount : remain;
            },

            // 点击赠品图片切换选中赠品数量
            togglePresentSelection: function (product, present) {
                if (present.selectedTemp) {
                    present.selectedTemp = 0;
                } else if ($scope.getRemainPresentCount(product, present)) {
                    present.selectedTemp = 1;
                }
            },

            // 确认赠品选择
            confirmPresentSelection: function () {
                var presents = $scope.product.presents;
                for (var i in presents) {
                    var present = presents[i];
                    present.selected = present.selectedTemp;
                }
                calculateCartInfo();
                $scope.presentModal.hide();
            },

            // 结算
            checkout: function () {
                if (userService.hasLogined()) {
                    var selectedProduct = getSelectedProduct(),
                        data = [];
                    
                    for (var i in selectedProduct) {
                        var product = selectedProduct[i],
                            presents = product.presents,
                            selectedPresents = [],
                            item = {id:product.id, amount:product.count};

                        for (var i in presents) {
                            var present = presents[i];
                            if (present.selected) {
                                selectedPresents.push({
                                    id: present.id,
                                    amount: present.selected,
                                    ruleid: present.ruleid
                                });
                            }
                        }
                        if (selectedPresents.length) {
                            item.presents = selectedPresents;
                        }
                        data.push(item);
                    }

                    // 统计页面点击
                    nnclickService.countClick('购物车', '结算');

                    $state.go('order.info', {cartId: $scope.cart.cartId,data:JSON.stringify(data)});
                } else {
                    popupService.confirmPopup('结算失败', '您还未登录,请先登录')
                    .then(function (res) {
                        if(res) {
                            $state.go('login', {returnState:'cart'});
                        }
                    });
                }
            }
        });
        
        // 加载购物车
        var loadCart = function () {
            $scope.cart = {
                cartId: null,
                // 选中商品总数
                total: 0,
                // 选中商品总价格
                sumPrice: 0,
                // 商品列表
                cartItems: null,
                // 赠品列表
                presents: null,
                // 全选商品
                allSelected: null
            };
            $scope.loading = true;
            cartService.shoppingCart()
            .success(function (data) {
                if (data.cartId) {
                    for (var i in data.cartNormalProduct) {
                        var product = data.cartNormalProduct[i],
                            presents = product.presents;
                        product.selected = true;
                        product.shortName || (product.shortName = product.shortname);
                        product.buyPrice = product.price && product.price.value;
                        product.marketPrice = product.marketprice && product.marketprice.value;
                        
                        if (!presents) {
                            continue;
                        }

                        if (presents.length == 1) {
                            var present = presents[0],
                                sellCount = presents[0].sellCount,
                                presentMaxCount = product.presentMaxCount;
                            present.selected = (+present.price) != 0 ? 0 :
                                               (sellCount < presentMaxCount ? sellCount : presentMaxCount);
                        } else {
                            for (var i in presents) {
                                var present = presents[i];
                                present.selected = 0;
                            }
                        }
                    }
                    var cart = $scope.cart;
                    cart.cartId = data.cartId;
                    cart.cartItems = data.cartNormalProduct;
                    cart.presents = data.cartPresentsList;
                    cart.allSelected = true;
                    calculateCartInfo();
                }
            })
            .error(function (data) {
                popupService.alertPopup('加载购物车失败', data.error.text);
            })
            .finally(function () {
                $scope.loading = false;
            });
        };
        
        // 获取已选中商品
        var getSelectedProduct = function () {
            var result = [];
            for (var i in $scope.cart.cartItems) {
                var item = $scope.cart.cartItems[i];
                if (item.selected) {
                    result.push(item);
                }
            }
            return result;
        }
        
        // 计算购物车信息
        var calculateCartInfo = function() {
            var sumPrice = 0, 
                total = 0;
            var selProduct = getSelectedProduct();

            for(var i in selProduct) {
                var product = selProduct[i],
                    presents = product.presents;
                sumPrice += parseFloat(product.price.value, 10) * product.count;
                total += product.count;
                for (var i in presents) {
                    var present = presents[i];
                    sumPrice += parseFloat(present.price, 10) * present.selected;
                    total += present.selected;
                }
            }
            $scope.cart.sumPrice = sumPrice.toFixed(2);
            $scope.cart.total = total;
        };
        
        // 加入购物车弹出层
        $ionicModal.fromTemplateUrl('templates/product/productvariety.html', {
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
            $scope.varietyModal = modal;
        });

        // 选择赠品弹出层
        $ionicModal.fromTemplateUrl('templates/cart/present.html', {
            scope: $scope,
            animation: 'slide-in-up'
        }).then(function(modal) {
            $scope.presentModal = modal;
        });
        
        $scope.$on('$destroy', function() {
            $scope.varietyModal.remove();
            $scope.presentModal.remove();
        });
        
        loadCart();
    })
;

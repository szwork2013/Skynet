/**
 * 结算制器
 */
angular.module('app.controllers.checkout', [])

    .controller('checkoutCtrl', function($scope, $state, $stateParams, checkoutService, 
                                         userService, couponService, popupService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('结算中心');

        angular.extend($scope, {
            // 订单信息
            order: {
                temCartId: null,
                // 收货人信息
                consignee: null,
                total: null,
                freight: null,
                orderDiscount: null,
                couponDiscount: null,
                cardDiscount: null,
                result: null,
                timeOfReceipt: 3,
                invoice: false,
                coupon: null,
                prepaidCard: 0,
                remark: '',
                idCardNumber: ''
            },
            // 商品列表
            productList: {
                haiwaizhiyou: [],
                normal: []
            },
            // 赠品列表
            presentList: [],
            // 是否通信中
            loading: true,
            // 提交订单
            submitOrder: function () {
                var order = $scope.order,
                    needIdCardNo = $scope.productList.haiwaizhiyou.length > 0;

                if ($scope.productList.haiwaizhiyou.length && !order.idCardNumber) {
                    popupService.alertPopup('提交订单失败', '请填写身份证号码');
                    return;
                }
                
                if (needIdCardNo && !CM.isIdCardNumber(order.idCardNumber)) {
                    popupService.alertPopup('提交订单失败', '身份证号码格式错误');
                    return;
                }
                
                var invoiceTitle = order.invoice ? $scope.invoice.title : undefined,
                    invoiceContent = order.invoice ? $scope.invoice.content : undefined;

                $scope.loading = true;
                checkoutService.submitOrder($stateParams.cartId, order.tempCartId, order.consignee.id, order.timeOfReceipt, 
                                            order.invoice, order.coupon, order.prepaidCard, order.remark, order.idCardNumber, 
                                            invoiceTitle, invoiceContent)
                .success(function (data) {
                    var products = JSON.parse($stateParams.data),
                        cart = CM.cart;
                    for (var i in products) {
                        var product = products[i];
                        cart.total -= product.amount;
                    }

                    // 统计页面访问
                    nnclickService.countView('提交订单成功页', 'id=' + data.orderId);
                    $state.go('ordersuccess', {orderId:data.orderId, aliasId:data.orderAliasId, price:data.price});
                })
                .error(function (data) {
                    popupService.alertPopup('提交订单失败', data.error.text);
                    $scope.loading = false;
                });
            },
            // 收货信息列表
            consigneeList: [],
            // 编辑中的收货信息
            editingConsignee: null,
            // 编辑收货信息中的地区信息
            areaInfo: {
                selected: [],
                area: [],
                complete: false
            },
            // 选择订单使用的收货信息
            selectConsignee: function (index) {
                $scope.loading = true;
                var consignee = $scope.consigneeList[index];
                $scope.order.consignee = consignee;
                userService.editConsignee(consignee.id, consignee.name, consignee.address, 
                                          consignee.area_id ? consignee.area_id : consignee.city_id,
                                          consignee.mobilephone, consignee.postcode)
                .success(function(data) {
                    checkout();
                });
                $state.go('order.info');
            },
            // 编辑收货信息
            editConsignee: function (index) {
                if (index !== undefined) {
                    var consignee = $scope.consigneeList[index],
                        selected = [],
                        area = [];
                    userService.getArea()
                    .success(function (data) {
                        area.push(data);
                        userService.getArea(consignee.province_id)
                        .success(function (data) {
                            area.push(data);
                            if (consignee.city_id) {
                                userService.getArea(consignee.city_id)
                                .success(function (data) {
                                    area.push(data);
                                });
                            }
                        });
                    });
                    $scope.editingConsignee = consignee;
                    selected.push(+consignee.province_id);
                    selected.push(+consignee.city_id);
                    if (consignee.area_id) {
                        selected.push(+consignee.area_id);
                    }
                    $scope.areaInfo.selected = selected;
                    $scope.areaInfo.area = area;
                    $scope.areaInfo.complete = true;
                } else {
                    var selected = [],
                        area = [];
                    userService.getArea()
                    .success(function (data) {
                        area.push(data);
                    });
                    $scope.editingConsignee = {};
                    $scope.areaInfo.selected = selected;
                    $scope.areaInfo.area = area;
                    $scope.areaInfo.complete = false;
                }
                $state.go('order.editconsignee');
            },
            // 收货信息编辑提交
            submitConsignee: function () {
                var consignee = $scope.editingConsignee,
                    id = consignee.id,
                    name = consignee.name,
                    address = consignee.address,
                    mobilephone = consignee.mobilephone,
                    postcode = consignee.postcode,
                    areaComplete = $scope.areaInfo.complete,
                    selected = $scope.areaInfo.selected,
                    regionId = selected[selected.length-1];
                
                if (!name || !address || !mobilephone || !areaComplete || !regionId) {
                    popupService.alertPopup('提交失败', '您的信息不全');
                    return;
                }
                
                if (!CM.isMobile(mobilephone)) {
                    popupService.alertPopup('提交失败', '手机号码格式错误');
                    return;
                }
            
                $scope.loading = true;
                $scope.order.consignee = consignee;
                userService.editConsignee(id, name, address, regionId, mobilephone, postcode)
                .success(function(data) {
                    checkout();
                });
                $state.go('order.info');
            },
            // 删除收货信息
            delConsignee: function (index) {
                popupService.confirmPopup('', '是否要删除联系人？')
                .then(function (res) {
                    if(res) {
                        userService.removeConsinee($scope.consigneeList[index].id)
                        .success(function (data) {
                            var isDefault = $scope.consigneeList[index].isDefault;
                            $scope.consigneeList.splice(index, 1);
                            if (isDefault) {
                                checkout();
                            }
                        })
                        .error(function (data) {
                            popupService.alertPopup('删除失败', data.error.text);
                        });
                    }
                });
            },
            // 切换地区,加载级联选择
            changeArea: function (index) {
                var area = $scope.areaInfo.area,
                    selected = $scope.areaInfo.selected,
                    id = selected[index];
                area.splice(index + 1, area.length - index);
                selected.splice(index + 1, selected.length - index);
                
                if (!id) {
                    return;
                }
                
                userService.getArea(id)
                .success(function (data) {
                    if (data.length) {
                        area.push(data);
                        selected.push('');
                        $scope.areaInfo.complete = false;
                    } else {
                        $scope.areaInfo.complete = true;
                    }
                });
            },
            
            // 收货时间文本
            timeOfReceiptText: ['工作日送达', '休息日送达', '所有日期均可送达'],
            // 发票页跳转
            toggleInvoice: function () {
                if ($scope.order.invoice) {
                    $scope.order.invoice = false;
                    $state.go('order.invoice');
                }
            },
            
            // 发票信息
            invoice: {
                title: '',
                content: '',
                contentList: null,
            },
            // 添加发票信息
            addInvoice: function () {
                $scope.order.invoice = true;
                $state.go('order.info');
            },
            
            // 优惠券信息
            coupon: {
                list: null,
                activate: '',
                selected: ''
            },
            // 激活优惠券
            activateCoupon: function () {
                var couponId = $scope.coupon.activate;
                if (!couponId) {
                    return;
                }
                popupService.confirmPopup('激活优惠券', '是否要激活优惠券：<br />' + couponId)
                .then(function(res) {
                    if(res) {
                        $scope.loading = true;
                        couponService.activate(couponId)
                        .success(function (data) {
                            popupService.alertPopup('优惠券激活成功');
                            checkout();
                        })
                        .error(function (data) {
                            popupService.alertPopup('优惠券激活失败', data.error.text);
                            $scope.loading = false;
                        });
                    }
                });
            },
            // 取消优惠券
            cancelCoupon: function () {
                if ($scope.order.coupon) {
                    $scope.order.coupon = '';
                    updateCheckout();
                }
                $state.go('order.info');
            },
            // 使用优惠券
            applyCoupon: function () {
                if ($scope.order.coupon != $scope.coupon.selected) {
                    $scope.order.coupon = $scope.coupon.selected;
                    updateCheckout();
                }
                $state.go('order.info');
            },
            
            // 中免卡信息
            prepaidCard: {
                amount: 0,
                status: '',
                value: '',
                password: ''
            },
            // 取消中免卡支付
            cancelPrepaidCard: function () {
                if ($scope.order.prepaidCard) {
                    $scope.order.prepaidCard = '';
                    updateCheckout();
                }
                $state.go('order.info');
            },
            // 使用中免卡支付
            applyPrepaidCard: function () {
                var prepaidCard = $scope.prepaidCard,
                    value = prepaidCard.value;

                if (value > $scope.order.result) {
                    popupService.alertPopup('支付失败', '支付额超过订单金额');
                    return;
                }
                if (value > prepaidCard.amount) {
                    popupService.alertPopup('支付失败', '中免卡余额不足');
                    return;
                }
                $scope.loading = true;
                userService.checkPayPassword(prepaidCard.password)
                .success(function (data) {
                    $scope.order.prepaidCard = value;
                    updateCheckout();
                    $state.go('order.info');
                })
                .error(function (data) {
                    $scope.loading = false;
                    popupService.alertPopup('支付密码错误', data.error.text);
                });
            },
        });
        
        // 生成订单信息
        var checkout = function () {
            $scope.loading = true;
            
            angular.extend($scope.order, {
                consignee: null,
                total: null,
                freight: null,
                orderDiscount: null,
                couponDiscount: null,
                result: null,
                coupon: ''
            });
            
            checkoutService.checkout($stateParams.cartId, $stateParams.data)
            .success(function (data) {
                var order = $scope.order,
                    map = data.valueMap;
                // $scope.order.consignee = data.adress_default;
                order.tempCartId = data.cartId;
                order.total = map.total;
                order.freight = map.realFreight;
                order.orderDiscount = map.orderRebate;
                order.couponDiscount = map.rebateCard;
                order.cardDiscount = map.prepaidCard;
                order.result = map.result;

                var haiwaizhiyou = [],
                    normal = [];
                for (var i in data.productList) {
                    var product = data.productList[i];
                    if (product.haiwaizhiyou == '是') {
                        haiwaizhiyou.push(product);
                    } else {
                        normal.push(product);
                    }
                }
                $scope.productList.haiwaizhiyou = haiwaizhiyou;
                $scope.productList.normal = normal;
                $scope.presentList = data.presentProductList;
                $scope.coupon.list = data.couponList;
                $scope.prepaidCard.amount = data.prepaidCard.balance.toFixed(2);
                $scope.prepaidCard.status = data.prepaidCard.status;
                $scope.invoice.content = data.invoiceContents[0];
                $scope.invoice.contentList = data.invoiceContents;
                getConsigneeList();
            })
            .error(function (data) {
                popupService.alertPopup('结算失败', data.error.text)
                .then(function () {
                    $state.go('cart');
                });
            });
        };
        
        // 获取收货信息列表
        var getConsigneeList = function () {
            $scope.loading = true;
            userService.getConsineeList()
            .success(function(data) {
                var consignee = null;
                for (var i in data.valid_address_list) {
                    consignee = data.valid_address_list[i];
                    if (consignee.isDefault) {
                        $scope.order.consignee = consignee;
                    }
                }
                if (!$scope.order.consignee) {
                    $scope.order.consignee = consignee;
                }
                $scope.consigneeList = data.valid_address_list;
                $scope.loading = false;
            })
            .error(function (data) {
                $scope.loading = false;
            });
        };
        
        // 更新结算金额
        var updateCheckout = function () {
            $scope.loading = true;
            var order = $scope.order,
                chooseCardIds = order.coupon,
                unChooseCardIds = [];
            for (var i in $scope.coupon.list) {
                var coupon = $scope.coupon.list[i];
                if (coupon.couponcard_id != order.coupon) {
                    unChooseCardIds.push(coupon.couponcard_id);
                }
            }
            checkoutService.updateCheckout(order.tempCartId, order.consignee.id, chooseCardIds, unChooseCardIds.join(','), order.prepaidCard)
            .success(function(data) {
                var map = data.valueMap;
                // $scope.order.consignee = data.adress_default;
                order.tempCartId = data.cartId;
                order.total = map.total;
                order.freight = map.realFreight;
                order.orderDiscount = map.orderRebate;
                order.couponDiscount = map.rebateCard;
                order.cardDiscount = map.prepaidCard;
                order.result = map.result;
                $scope.loading = false;
            })
            .error(function (data) {
                $scope.loading = false;
            });
        };
        
        checkout();
    })

;

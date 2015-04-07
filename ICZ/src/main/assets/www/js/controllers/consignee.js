/**
 * 用户操作控制器
 */
angular.module('app.controllers.user')
    
    // 会员中心收货地址控制器
    .controller('consigneeCtrl', function($scope, $state, userService, popupService, nnclickService) {
        
        // 统计页面访问
        nnclickService.countView('收货地址页');

        angular.extend($scope, {
            loading: true,
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
                userService.editConsignee(consignee.id, consignee.name, consignee.address, 
                                          consignee.area_id ? consignee.area_id : consignee.city_id, 
                                          consignee.mobilephone, consignee.postcode)
                .success(function () {
                    $scope.loading = false;
                });
                $scope.$parent.goBack();
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
                $state.go('consignee.editconsignee');
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
                userService.editConsignee(id, name, address, regionId, mobilephone, postcode)
                .success(function () {
                    getConsigneeList();
                });
                $scope.$parent.goBack();
            },
            // 删除收货信息
            delConsignee: function (index) {
                popupService.confirmPopup('', '是否要删除联系人？')
                .then(function (res) {
                    if(res) {
                        $scope.loading = true;
                        userService.removeConsinee($scope.consigneeList[index].id)
                        .success(function (data) {
                            $scope.consigneeList.splice(index, 1);
                            $scope.loading = false;
                        })
                        .error(function (data) {
                            popupService.alertPopup('删除失败', data.error.text);
                            $scope.loading = false;
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
            }
        });
        
        // 获取收货信息列表
        var getConsigneeList = function () {
            $scope.loading = true;
            userService.getConsineeList()
            .success(function(data) {
                $scope.consigneeList = data.valid_address_list;
                $scope.loading = false;
            })
            .error(function (data) {
                $scope.loading = false;
            });
        };
        
        getConsigneeList();
    })
    
;

/**
 * 一品多款
 */
angular.module('app.services')
    .factory('varietyService', function() {
        // 更改一品多款属性选择
        var changeVariety = function (property, value) {
            if (this.selectedProperties[property] == value) {
                return;
            }
            this.selectedProperties[property] = value;
            var completekey = [],
                availableKey = [];
            for (var p in this.selectedProperties) {
                var v = this.selectedProperties[p];
                completekey.push(v);
                if (p != property && v) {
                    availableKey.push(p + v);
                }
            }
            this.selectedVariety = this.varietyMap[completekey.join(',')];
            if (!this.selectedVariety) {
                var availableMap = this.propertyMap[property][value];
                for (var p in this.selectedProperties) {
                    if (p == property || !this.selectedProperties[p]) {
                        continue;
                    }
                    if (availableKey.length > 0 && !availableMap[availableKey.join(',')]) {
                        this.selectedProperties[p] = null;
                        availableKey.shift();
                    } else {
                        break;
                    }
                }
            } else {
                checkSelectAmount(this);
            }
        };
        
        // 检查属性是否有对应商品
        var checkAvailable = function (property, value) {
            var availableMap = this.propertyMap[property][value],
                key = [];
            for (var p in this.selectedProperties) {
                var v = this.selectedProperties[p];
                if (p != property && v) {
                    key.push(p + v);
                }
            }
            if (key.length == 0) {
                return true;
            }
            return availableMap[key.join(',')];
        };

        // 更改选中商品时,如果选中数量为0且商品有货则将数量改为1
        var checkSelectAmount = function (variety) {
            var product = variety.productMap[variety.selectedVariety];
            if (variety.count == 0 && product.count > 0) {
                variety.count = 1;
            }
        }

        return {
            initVariety: function (data, selectedId) {
                var variety = {
                    // 被选中一品多款商品id
                    selectedVariety: selectedId,
                    // 被选中一品多款属性,key:属性名,value:属性值
                    selectedProperties: {},
                    // 一品多款商品map,key:商品id,value:商品对象
                    productMap: {},
                    // 一品多款属性map,key:属性名,value:属性值对应关系map(key:属性值,value:对应集合)
                    propertyMap: {},
                    // 一品多款map,key:逗号分隔属性值(红色,XL),value:商品id
                    varietyMap:{},
                    // 添加购物车商品数量或购物车中商品数量
                    count: 0,
                    // 更改一品多款属性选择
                    changeVariety: changeVariety,
                    // 检查属性是否有对应商品
                    checkAvailable: checkAvailable
                };
                
                // 一品多款数据初始化
                for (var i in data) {
                    var product = data[i];
                    variety.productMap[product.productId] = product;
                    
                    if (product.productId == variety.selectedVariety) {
                        for (var property in product.styleMap) {
                            variety.selectedProperties[property] = product.styleMap[property];
                        }
                    }
                    
                    // 生成属性对应map
                    var completeKey = [],
                        keys = [];
                    for (var property in product.styleMap) {
                        var value = product.styleMap[property];
                        completeKey.push(value);
                        for (var p in product.styleMap) {
                            var v = product.styleMap[p];
                            if (!variety.propertyMap[property]) {
                                variety.propertyMap[property] = {};
                            }
                            var valueMap = variety.propertyMap[property];
                            if (!valueMap[value]) {
                                valueMap[value] = {};
                            }
                            if (property == p) {
                                continue;
                            }
                            for (var j in keys) {
                                var key = keys[j];
                                key.push(p + v);
                                valueMap[value][key.join(',')] = true;
                            }
                            keys.push([p + v]);
                            valueMap[value][p + v] = true;
                        }
                    }
                    variety.varietyMap[completeKey.join(',')] = product.productId;
                }

                checkSelectAmount(variety);
                
                return variety;
            }
        };
    });

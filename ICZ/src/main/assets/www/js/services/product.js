angular.module('app.services')

.factory('productService',function($q, apiService, clientService){

	return {
        // 获取分类信息
		classification: function(cid){
            var data = clientService.getCache('classification');
            if (data) {
                return $q.when(data);
            }
        
			return apiService.get('/type', {
                intCid: cid
            }).then(
                function (object) {
                    data = object.data;
                    clientService.addCache('classification', data);
                    return data;
                },
                function (object) {
                    return $q.reject(object.data);
                }
            );
		},
        
        // 获取品牌信息
		brands: function(){
            var data = clientService.getCache('brands');
            if (data) {
                return $q.when(data);
            }
        
			return apiService.get('/brands').then(
                function (object) {
                    data = object.data;
                    clientService.addCache('brands', data);
                    return data;
                },
                function (object) {
                    return $q.reject(object.data);
                }
            );
		},
		
        // 获取商品列表信息
		productList: function(params, noCache){
            noCache && clientService.addCache('productlist', {});
        
            var params = angular.extend({}, params)
            var keyMap = {
                beginprice: 'beginPrice',
                endprice: 'endPrice',
                per_page: 'perPage'
            };
            for (var key in keyMap) {
                var value = keyMap[key];
                params[key] = params[value];
                params[value] = undefined;
            }
            
            var key = 'productlist-' + JSON.stringify(params),
                cache = clientService.getCache('productlist') || {},
                data = cache[key];
            if (data) {
                return $q.when(data);
            }
            
			return apiService.post('/productlist', params).then(
                function (object) {
                    data = object.data;
                    cache[key] = data;
                    clientService.addCache('productlist', cache);
                    return data;
                },
                function (object) {
                    return $q.reject(object.data);
                }
            );
		},

        // 获取商品详情信息
		productInfo: function(pid){
			return apiService.get('/product/info', {
                pid: pid
            });
		},
        
        // 获取商品详情扩展属性,包含一品多款信息
        productExtInfo: function(pid){
			return apiService.get('/product/extinfo', {
                pid: pid
            });
		},
        
        // 获取商品一品多款信息
        productVariety: function(pid){
			return apiService.get('/product/morestyle', {
                pid: pid
            });
		},

		productDiscuss: function(params){
			return apiService.get('/product/discuss', params);
		}
	}


});

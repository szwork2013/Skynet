/**
 * API 服务封装了 $http 服务，用于访问后台接口，
 * 它除了可以出发网络异常导致的错误外，还能识别业务异错误。
 */
angular.module('app.services', []).provider('apiService', function apiProvider() {
    var provider = this;

    provider.formatUrlParameter = function(obj) {
        var query = '', name, value, fullSubName, subName, subValue, innerObj, i;

        for(name in obj) {
            value = obj[name];

            if(value instanceof Array) {
                for(i=0; i<value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if(value instanceof Object) {
                for(subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if(value !== undefined && value !== null)
                query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
        }

        return query.length ? query.substr(0, query.length - 1) : query;
    };

    provider.serviceAddress = '';

    provider.$get = ['$http', '$q', function($http, $q) {
        var api = function(config) {
            var deferred = $q.defer(),
                promise = deferred.promise;

            promise.success = function(callback) {
                promise.then(function(response) {
                    callback(response.data, response.status, response.headers, config);
                });
                return promise;
            };

            promise.error = function(callback) {
                promise.then(null, function(response) {
                    callback(response.data, response.status, response.headers, config);
                });
                return promise;
            };

            config.url = provider.serviceAddress + config.url;

            $http(config).then(
                function(response) {
                    if (response.data && response.data.error) {
                        response.data.error.code = 1;
                        deferred.reject(response);
                    }
                    else {
                        response.data = response.data || {};
                        deferred.resolve(response);
                    }
                },
                function(response) {
                    response.data = angular.extend({
                        error: {
                            code: -1,
                            text: '网络异常'
                        }
                    }, response.data);
                    deferred.reject(response);
                }
            );

            return promise;
        };

        api.get = function(url, data, config) {
            var paramsIndex, dataStr;

            if (data) {
                dataStr = provider.formatUrlParameter(data);
                url += '?' + dataStr;
            }

            return api(angular.extend(config || {}, {
                method: 'get',
                url: url
            }));
        };

        api.post = function(url, data, config) {
            return api(angular.extend(config || {}, {
                method: 'post',
                url: url,
                data: data
            }));
        };

        return api;
    }];
});

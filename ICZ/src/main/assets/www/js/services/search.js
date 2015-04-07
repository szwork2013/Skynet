angular.module('app.services')
    .factory('searchService', function(apiService, localStorageService) {
        var searchHistoryKey = 'search-history';
    
        var searchService = {
            /**
             * 获取热门查询关键字
             */
            hotKeywords: function() {
                return apiService.get('/searchkeyword?type=1');
            },

            /**
             * 查询商品
             * @param keyword 查询关键字
             * @param page 页码
             * @param count 页数
             * @param order 排序规则
             */
            search: function(keyword, page, count, order) {
                return apiService.get(
                    '/productlist', {
                        page: page,
                        per_page: count,
                        keyword: keyword,
                        order: order
                    });
            },
            
            /**
             * 添加搜索历史
             * @param keyword 查询关键字
             */
            addSearchHistory: function (keyword) {
                var history = this.getSearchHistory();
                
                history.unshift(keyword);
                
                if (history.length > 20) {
                    history = history.slice(0, 20);
                }
                localStorageService.set(searchHistoryKey, history);
            },
            
            /**
             * 获取搜索历史
             */
            getSearchHistory: function () {
                return localStorageService.get(searchHistoryKey, [], true);
            }
        };

        return searchService;
    });

/**
 * 商品收藏相关业务
 */
angular.module('app.services').factory('subjectService', function($q, apiService, clientService) {
    var subjectService = {
        /**
         * 获取主题页信息
         * @param subjectId {int} 主题页ID
         */
        getSubject: function(subjectId) {
            var key = 'subject-' + subjectId,
                data = clientService.getCache(key);
            if (data) {
                return $q.when(data);
            }
            
            return apiService.get('/sale', {
                'id': subjectId
            }).then(
                function (object) {
                    data = object.data;
                    clientService.addCache(key, data);
                    return data;
                },
                function (object) {
                    return $q.reject(object.data);
                }
            );
        }
    };
    return subjectService;
});

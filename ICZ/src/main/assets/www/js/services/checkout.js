angular.module('app.services')

.factory('checkoutService',function($http, apiService){

    return {
    
        /** 订单确认 */
        checkout:function(cartId, data){
            if (!data) {
                data = '[]';
            }
            return apiService.post('/checkout', {
                cartid: cartId,
                data: data
            });
        },
        
        /** 更新订单信息 */
        updateCheckout:function(cartId, consigneeId, chooseCardIds, unChooseCardIds, prepaidCard){
            return apiService.post('/checkout/update', {
                tempcartid: cartId,
                consigneeid: consigneeId,
                chooseCardIds: chooseCardIds,
                unChooseCardIds: unChooseCardIds,
                prepaidCard: prepaidCard
            });
        },

        /** 提交订单 */
        submitOrder: function(cartId, tempCartId, consigneeId, timeOfReceipt, invoice, coupon, 
                              prepaidCard, remark, idCardNumber, invoiceTitle, invoiceContent) {
            var data = {
                cartId: cartId.toString(),
                tempCartId: tempCartId.toString(),
                addressid: consigneeId.toString(),
                payid: '-1',
                express_time: {
                    delivery_id: timeOfReceipt
                },
                invoice: invoice ? 'yes' : 'no',
                invoiceTitle: invoiceTitle,
                invoiceContent: invoiceContent,
                couponcard: [coupon ? coupon.toString() : ''],
                prepaidCard: prepaidCard,
                remark: remark,
                idcardnumber: idCardNumber
            }
            return apiService.post('/submit/order', {
                data: JSON.stringify(data)
            });
        },
        
        /**
         * 获取当前用户结算时可用的优惠券
         * @param cartId {Integer} 购物车 ID
         */
        couponsForCheckout: function(cartId) {
            return apiService.post('/submit/coupon', {
                cartid: cartId
            });
        },
        
        /**
         * 添加发票信息
         */
        addInvoice: function(title, context){
            return apiService.post('/invoice/add', {
                ititle: title,
                icontext: context
            });
        }

    };
});

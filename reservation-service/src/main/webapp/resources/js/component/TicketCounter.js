/**
 * TicketCounter, 성인, 청소년, 어린이, ...
 * 객체 하나당 하나의 티켓 카운터가 작동. ( 개별 )
 */
define('TicketCounter', ['jquery', 'utils', 'eg'], function($, utils, eg) {

    var TicketCounter = utils.extends(eg, {


        init : function(options) {
            this.options = {
                min : 0,
                index : 0,
                max : 10,
                totalPrice : 0
            };
            this.options = $.extend({}, this.options, options);
        },


        increment : function(index) {
            var options = this.options;
            var index = index * 1;

            (options.max > index) && index++;

            this.options.totalPrice = this.getPrice(index);
            this.options.index = index;

            this.trigger('afterIncrement', this.options);
        },


        decrement : function(index) {
            var options = this.options;
            var index = index * 1;

            (options.min < index) && index--;

            this.options.totalPrice = this.getPrice(index);
            this.options.index = index;

            this.trigger('afterDecrement', this.options);
        },


        getPrice : function (index) {
            var discountPrice = this.options.discountPrice;
            discountPrice = this.convertPriceRemoveComma(discountPrice);
            return this.convertPriceAddComma(index * discountPrice);
        },


        convertPriceAddComma : function(price) {
            if(typeof price !== 'string') price += '';
            return price.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },


        convertPriceRemoveComma : function(price) {
            if(typeof price !== 'string') price += '';
            return price.replace(/[^\d]+/g, '');
        }

    });

    return TicketCounter;
});

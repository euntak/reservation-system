define('templates/helpers/getPrice', ['hbs/handlebars'], function ( Handlebars ) {
    function getPrice(item) {
        if(item.priceType === 1) {
            return item.adultCost = '성인(만 19~64세) ' + item.price + '원';
        }
        else if(item.priceType === 2) {
            return item.teenagerCost = '<br> 청소년(만 13~18세) ' + item.price + '원';
        }
        else if(item.priceType === 3) {
            return item.kizCost = '<br> 어린이(만 4~12세) ' + item.price + '원';
        }
        else {
            return item.etcCost = '<br> 국가유공자, 장애인, 65세 이상 ' + item.price + '원';
        }
    }
    // Handlebars Convert Cost Helper
    Handlebars.registerHelper('getPrice', getPrice);
    return getPrice;
});


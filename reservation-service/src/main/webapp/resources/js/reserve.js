//requireJS 기본 설정 부분
requirejs.config({

    baseUrl: '/resources/js',

    paths:{
        'jquery': '../node_modules/jquery/dist/jquery.min',
        'eg': '../node_modules/@egjs/component/dist/component.min',
        'hbs': '../node_modules/require-handlebars-plugin/hbs',
        'utils': 'common/util',
        'TicketCounter': 'component/TicketCounter',

        'reserve/reserve-form': '../templates/reserve/reserve-form',
        'reserve/reserve-container': '../templates/reserve/reserve-container',
        'helpers/getPrice': '../templates/helpers/get-price'
    },

    shim:{
        'TicketCounter':{
            deps: ['jquery', 'eg', 'utils'],
            exports: 'TicketCounter'
        }
    }
});

//requireJS를 활용하여 모듈 로드
requirejs(['jquery',
    'hbs!reserve/reserve-container',
    'hbs!reserve/reserve-form',
    'helpers/getPrice'],
    function ($, reserveContainer, reserveForm) {

    $(function () {

        var Reserve = {

            product : {},
            options : {},


            init : function() {
                this.getReservableProduct();
                this.bindEvents();
            },


            bindEvents : function() {
                $('.ct_wrap').on('click', '.count_control .ico_plus3', this.updateTicket.bind(this, 'plus'));
                $('.ct_wrap').on('click', '.count_control .ico_minus3', this.updateTicket.bind(this, 'minus'));

                $('.agreement').on('click','.btn_agreement', this.toggleAgreement.bind(this));

                $('.booking_form_wrap').on('change keyup', 'input', CheckValidation.checkAll.bind(CheckValidation));
                $('.section_booking_agreement').on('click', 'input', CheckValidation.checkAll.bind(CheckValidation));

                $('.box_bk_btn').on('click', '.bk_btn', this.insertBooking.bind(this));
            },


            updateTicket : function(variation, e) {
                e.preventDefault();

                var $el = $(e.target).closest('.qty');
                var $counter = $el.find('input[type="tel"]');

                var priceType = $el.data('pricetype');
                var discountPrice = this.convertPriceRemoveComma(this.product.prices[priceType-1].discountPrice);

                requirejs(['TicketCounter'], function(TicketCounter) {
                    // new로 계속 생성하는데 메모리에 문제가 없을까? function scope안에 있어서 힙에 계속 쌓일까, ?
                    var ticketCounter = new TicketCounter({ min : 0, max : 10 , discountPrice : discountPrice});

                    ticketCounter.on({
                        afterIncrement : this.updateTicketUI.bind(this, ticketCounter.options, $el),
                        afterDecrement : this.updateTicketUI.bind(this, ticketCounter.options, $el)
                    });

                    (variation === 'plus') && ticketCounter.increment($counter.val());
                    (variation === 'minus') && ticketCounter.decrement($counter.val());
                }.bind(this));
                
                // product type에 따른 [예매 내용] 총 갯수 업데이트
                this.updateBookingInfo();

                // validation check ticket count
                CheckValidation.checkAll.call(CheckValidation);
            },


            toggleAgreement : function(e) {
                e.preventDefault();

                var $toggleBtn = $(e.target);
                var $btnText = $toggleBtn.find('.btn_text');
                var $toggleImg = $toggleBtn.find('.fn');

                $toggleBtn.closest('.agreement').toggleClass('open');

                if($btnText.text() === '보기') {
                    $btnText.text('닫기');
                    $toggleImg.removeClass('fn-down2').addClass('fn-up2');
                } else {
                    $btnText.text('보기');
                    $toggleImg.removeClass('fn-up2').addClass('fn-down2');
                }
            },


            updateTicketUI : function(options, $el) {

                var $counter = $el.find('input[type="tel"]');
                var $totalPrice = $el.find('.total_price');
                var $numTxt = $el.find('.individual_price');

                var $minusBtn = $counter.closest('.clearfix').find('.ico_minus3');
                var $plusBtn = $counter.closest('.clearfix').find('.ico_plus3');

                // counter, price update value
                $counter.val(options.index);
                $totalPrice.text(options.totalPrice);

                if(options.index === options.min) {
                    $counter.addClass('disabled');
                    $minusBtn.addClass('disabled');

                    //on_color 제거
                    $numTxt.removeClass('on_color');
                }

                if(options.index > options.min) {
                    $counter.removeClass('disabled');
                    $minusBtn.removeClass('disabled');
                    $plusBtn.removeClass('disabled');
                    //on_color추가
                    $numTxt.addClass('on_color');

                }

                if(options.index === options.max) {
                    $counter.addClass('disabled');
                    $plusBtn.addClass('disabled');
                }

            },


            updateBookingInfo : function() {
                var product = this.product;
                var $wrapper = $('.ticket_body');
                var $children = $wrapper.children('.qty');

                // $.map + reduce 를 통한 total 구하기
                var total = $.map($children, function(item) {
                    var $counter = $(item).find('input[type="tel"]');
                    return $counter.val() * 1;
                }).reduce(function(prev, next){
                    return prev + next;
                });

                var reservationInfo = product.salesStart + '~' + product.salesEnd + ', 총 ' + total + '매';

                $('.inline_txt').text(reservationInfo);
            },


            insertBooking : function(e) {
                e.preventDefault();

                var data = {};
                var $confirmBtn = $(e.target).closest('.bk_btn_wrap');

                if($confirmBtn.hasClass('disable')) return false;


                data.productId=$('.preview_txt').data('id');

                //data.userId=; userId는 서버에서 session에 있는 정보로 초기화

                data.generalTicketCount = $('.qty[data-priceType=1]').find('.count_control_input').val();
                data.youthTicketCount = $('.qty[data-priceType=2]').find('.count_control_input').val();
                data.childTicketCount = $('.qty[data-priceType=3]').find('.count_control_input').val();
                data.reservationName = $('input[name="name"]').val();
                data.reservationTel = $('input[name="tel"]').val();
                data.reservationEmail = $('input[name="email"]').val();


                var result = this.ajaxWrapper('POST', '/api/reservations', JSON.stringify(data));
                result.done(function() {
                    alert("예약 완료 :)");
                    location.href="/my"; //나의 예약페이지로 이동
                });
            },


            getReservableProduct : function() {
                var url = '/api' + window.location.pathname;
                var result = this.ajaxWrapper('GET', url, null);

                result.then(function(res) {
                    var product = this.product = res.product;
                    this.productInfoRendering(product)
                }.bind(this));
            },


            productInfoRendering : function(product) {

                this.product.deadLineTime = this.getDeadLineTime(product.salesEnd, 30);
                this.product.salesStart = this.convertDateFormattoISO(product.salesStart);
                this.product.salesEnd = this.convertDateFormattoISO(product.salesEnd);
                this.product.minCost = this.getMinCost(product.prices).price;
                this.product.prices = this.getDiscountPriceRate(product.prices);
                this.product.prices = this.mappingTicketType(product.prices);
                this.product.ticketCount = 3000;

                // 컨테이너 템플릿 등록
                $('.ct_wrap').prepend(reserveContainer(this.product));

                // 예약자 정보 및 예약 정보 템플릿 등록
                $('.form_horizontal').find('.last').append(reserveForm(this.product));
            },


            convertDateFormattoISO : function(date) {
                var isoDate = new Date(date); // 2017-02-12 Fri +9
                var dayLabel = ['(일)', '(월)', '(화)', '(수)', '(목)', '(금)', '(토)'];
                var label = dayLabel[isoDate.getDay()];
                return isoDate.toISOString().slice(0, 10).replace(/-/g, ".").concat('.' + label);
            },


            getDeadLineTime : function(endDate, minute) {
                var intervalTime = minute * 60 * 1000; // 30분 전 (millsecond) 60 * 30 * 1000
                var deadLineTime = new Date(endDate - intervalTime);
                return deadLineTime.getHours() + ":" + deadLineTime.getMinutes();
            },


            getMinCost : function(costs) {
                return costs.reduce(function(prev, curr) {
                    return (prev.price < curr.price ? prev : curr);
                });
            },


            initOptionObject : function(costs) {
                costs.forEach(function(item) {
                    if(item.priceType === 1) this.options.adultCost = item.price * 1;
                    else if(item.priceType === 2) this.options.teenagerCost = item.price * 1;
                    else if(item.priceType === 3) this.options.kizCost = item.price * 1;
                    else this.options.etcCost = item.price * 1;
                }, this);
            },


            getDiscountPriceRate : function(costs) {
                return costs.map(function(item) {
                    item.discountPrice = item.price - item.price * item.discountRate;
                    item.discountRate *= 100;
                    item.price = this.convertPriceAddComma(item.price);
                    item.discountPrice = this.convertPriceAddComma(item.discountPrice);
                    return item;
                }.bind(this));
            },


            convertPriceAddComma : function(price) {
                if(typeof price !== 'string') price += '';
                return price.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
            },


            convertPriceRemoveComma : function(price) {
                if(typeof price !== 'string') price += '';
                return price.replace(/[^\d]+/g, '');
            },


            //1 : 성인 2: 청소년 3: 어린이
            mappingTicketType : function(prices){
                for(var i=0; i<prices.length;i++){
                    if (prices[i].priceType === 1){
                        prices[i].age = '성인';
                    }else if(prices[i].priceType === 2){
                        prices[i].age = '청소년';
                    }else{
                        prices[i].age = '어린이';
                    }
                }
                return prices;
            },

            ajaxWrapper : function(method, url, data) {
                return $.ajax({
                    contentType : 'application/json; charset=UTF-8',
                    method : method,
                    url : url,
                    data : data,
                    dataType : 'json'
                });
            }

        };




        /**
         * 입력 값들 체크 , 약관 동의 체크 , 티켓 갯수 체크
         */

        var CheckValidation = {

            checkAll : function(){
                var $confirmButton = $('.box_bk_btn .bk_btn_wrap');

                if(this.checkUserInfo() && this.checkAgreement() && this.checkTicketCount()) {
                    $confirmButton.removeClass('disable')
                } else {
                    $confirmButton.addClass('disable');
                }
            },

            checkAgreement : function(){
                var $checkBtn = $('.chk_agree');
                var isChecked = false;
                $checkBtn.is(':checked') ? (isChecked = true) : (isChecked = false);

                return isChecked;
            },

            checkUserInfo : function(){

                var username = $('input[name="name"]').val();
                var tel = $('input[name="tel"]').val();
                var email = $('input[name="email"]').val();

                var emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
                var telRegex = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})([0-9]{3,4})([0-9]{4})$/;


                if(username !== "" && tel !== ""){
                    if(tel.length > 10 && tel.length < 12){
                        if(!telRegex.test(tel)){ // regex 맞지 않을 때
                            return false;
                        }
                    } else { // 10 ~ 11 자리가 아닐때
                        return false;
                    }
                } else { // 빈값
                    return false;
                }

                if(email !== ""){
                    if(!emailRegex.test(email)){ // regex 맞지 않을 때
                        return false;
                    }
                } else { // 빈값
                    return false;
                }

                return true;
            },

            checkTicketCount : function(){
                // $.map + reduce 를 통한 total 구하기
                var total = $.map($('.qty'), function(item) {
                    var $counter = $(item).find('input[type="tel"]')
                    return $counter.val() * 1;
                }).reduce(function(prev, next){
                    return prev + next
                });

                if(total) return true;
                else return false;
            }
        };

        Reserve.init();
    });

});
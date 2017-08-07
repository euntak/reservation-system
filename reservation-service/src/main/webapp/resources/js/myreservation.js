(function($) {

    // 0 - 전체, 1 - 예약 신청중, 2 - 예약 확정, 3 - 이용 완료, 4 - 취소
    var TICKET_INFO = [
        { 'status':'', 'type':'0', 'statusName':'전체' , 'statusClass':'' },
        { 'status':'', 'type':'1', 'statusName':'예약 신청중' , 'statusClass':'ico_clock' },
        { 'status':'confirmed', 'type':'2', 'statusName':'예약 확정' , 'statusClass':'ico_check2' },
        { 'status':'used', 'type':'3', 'statusName':'이용 완료', 'statusClass':'ico_check2' },
        { 'status':'used', 'type':'4', 'statusName':'취소된 예약', 'statusClass':'ico_cancel' }
    ];

    var SimpleTicket = {
        
        templates : {},
        tickets : [],         // 전체
        filteredTickets: [],  // filtering된 티켓들
        currentTab : 0,       // 현재 탭


        init : function() {
            this.initHandlebars();
            this.getMyReservations();
            this.bindEvents();
        },


        bindEvents : function() {
            $('.summary_board').on('click', '.item', this.changeTabByReservationType.bind(this));
            $('.list_cards').on('click', '.booking_cancel', this.openCancelPopup.bind(this));
            $('.popup_booking_wrapper').on('click', '.btn_gray, .popup_btn_close', this.closeCancelPopup.bind(this));
            $('.popup_booking_wrapper').on('click', '.btn_green', this.cancelTicket.bind(this));
        },

        initHandlebars : function() {
            // ticket item handlebar
            this.templates.simpleTicket = Handlebars.compile($('#simple_ticket_templ').html());

        },


        getMyReservations : function() {
            var result = this.ajaxWrapper({ method:'GET', url:'/api/reservations' });
            result.done(function(tickets) {
                this.initTicketTemplate(tickets);
            }.bind(this));
        },


        initTicketTemplate : function(tickets) {
            this.currentTab = 0;
            var template = this.templates;
            this.tickets = tickets;


            // [Handlebars] 를 위해서 , 객체에 details, totalPrice, salesStart, salesEnd등을 알맞게 변화시켜서
            // 넣어준다.
            this.filteredTickets = TICKET_INFO.map(function(status, i) {
                
                var workedTicket = TICKET_INFO[i];
                workedTicket.tickets = this.getSimpleTicketsByType(i);
                workedTicket.tickets.forEach(function(ticket) {
                    
                    (ticket.reservationType === 3)
                        && (ticket.buttonName = '예매자 리뷰 남기기')
                        || (ticket.buttonName = '취소');
                    (ticket.reservationType === 4)
                        && (ticket.isCanceled = true);

                    ticket.salesStart = this.convertDateFormattoISO(ticket.salesStart);
                    ticket.salesEnd = this.convertDateFormattoISO(ticket.salesEnd);
                    ticket.totalPrice = this.convertPriceAddComma(ticket.totalPrice);
                    ticket.details = this.convertReservationBreakDown(ticket);

                }.bind(this));
                
                return workedTicket;

            }.bind(this));

            // 기본 값은 전체로 초기 렌더링
            this.rendering(0);
        },


        initSummeryBoard : function() {
            var $wrapper = $('.summary_board');
            var $item = $wrapper.find('li');
            var $figures = $item.find('.figure');

            // item의 index에 따라서 각각의 array에 length별 업데이트
            $figures.map(function(i, figure) {
                $(figure).text(this.getFiguresCounter(i));
            }.bind(this));

        },


        getFiguresCounter : function(index) {
            // index === 0 [전체] 나머지는 trello reservation type에 준수
            if(index === 0) {
                return this.filteredTickets.map(function(item, i) {
                    return item.tickets.length;
                }).reduce(function(prev,next){
                    return prev + next;
                });
            } else {
                if(index === 2 ) index = 3; // 예약 확정 탭이.. 없음 확정 인덱스를 완료 인덱스로 대체
                else if(index === 3 ) index = 4; // 취소.. 인덱스.. 헷갈린다.
                return this.filteredTickets[index].tickets.length;
            }
        },


        convertDateFormattoISO : function(date) {
            var isoDate = new Date(date);
            var dayLabel = ['(일)', '(월)', '(화)', '(수)', '(목)', '(금)', '(토)'];
            var label = dayLabel[isoDate.getDay()];
            var convertedDate = isoDate.toISOString().slice(0,10).replace(/-/g, ".").concat('.' + label);
            return convertedDate;
        },


        convertPriceAddComma : function(price) {
            if(typeof price !== 'string') price += '';
            return price.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },


        /**
         * SimpleTicket에 내역 정보 converting 함수
         */
        convertReservationBreakDown: function(ticket) {
            var breakdown = '';
            ticket.generalTicketCount && (breakdown += '성인(' + ticket.generalTicketCount + ') ');
            ticket.youthTicketCount && (breakdown += '청소년(' + ticket.youthTicketCount + ') ');
            ticket.childTicketCount && (breakdown += '어린이(' + ticket.childTicketCount + ') ');
            ticket.totalTicketCount && (breakdown += '총(' + ticket.totalTicketCount+ ')');
            return breakdown;
        },


        getSimpleTicketsByType : function(type) {
            (typeof type === 'String') ? type : '' + type;

            // 전체
            if(type==='0') {
                return this.filteredTickets;
            }

            return this.tickets.filter(function(ticket) {
                return ticket.reservationType === type;
            });
        
        },


        rendering : function(targetType) {
            var template = this.templates;
            var $cardList = $('.list_cards');
            var $errBoard = $('.err');

            $cardList.html('');

            // 전체
            if(targetType === 0) {
                this.filteredTickets.map(function(ticket) {
                    $cardList.append(template.simpleTicket(ticket));
                });
            } else {
                $cardList.append(template.simpleTicket(this.filteredTickets[targetType]));
            }

            ($cardList.find('li').length === 0) && $errBoard.show() || $errBoard.hide();

            this.initSummeryBoard();
        },


        changeTabByReservationType : function(e) {
            e.preventDefault();
            
            var $target = $(e.target);
            var $targetItem = $target.closest('.item');
            var $nowTab = $target.closest('ul').find('.on');
            var $clickTab = $targetItem.find('.link_summary_board');

            var targetType = $targetItem.data('type');

            $nowTab.removeClass('on');
            $clickTab.addClass('on');
            
            this.currentTab = targetType;
            this.rendering(this.currentTab);
        },


        openCancelPopup : function(e) {
            e.preventDefault();

            var $wrapper = $(e.target).closest('.card_item');
            var $cancelPopup = $('.popup_booking_wrapper');
            var $popupTitle = $cancelPopup.find('.pop_tit span');
            var $popupContainer = $cancelPopup.find('.popup_booking');
            var $popupScedule = $cancelPopup.find('.pop_tit small.sm');

            var bookingId = $wrapper.data('booking-id');
            var name = $wrapper.find('h4.tit').text();
            var scedule = $($wrapper.find('.item .item_dsc')[0]).text();
            
            // 해당 booking id를 받아서 배열의 index 값을 리턴해준다.
            var ticket = this.getTicketOfFilteredList(bookingId);

            if(ticket.item.reservationType === 3) {
                location.href = '/reviewWrite/' + ticket.item.pid;
            } else {
                $popupTitle.text(name);
                $popupScedule.text(scedule);
                $cancelPopup.data('popup-id', ticket.item.rid);
                $cancelPopup.fadeIn(350);
            }
        },


        closeCancelPopup : function(e) {
            e.preventDefault();
            var $cancelPopup = $('.popup_booking_wrapper');
            $cancelPopup.fadeOut(350);
        },


        getTicketOfFilteredList : function(bookingId) {
            // 예약 신청중 -> 취소 가능 index 1
            // 예약 확정 -> 취소 가능  index 2
            var filertedListLength = this.filteredTickets.length;

            for(var i = 0; i < filertedListLength; i++) {
                
                var list = this.filteredTickets[i];
                var listLength = list.tickets.length;

                for(var j = 0; j < listLength; j++) {
                    var rid = list.tickets[j].rid;
                    if(bookingId === rid) {
                        return {
                            index : j,
                            item : list.tickets[j]
                        };
                    }
                }
            }
        },


        cancelTicket : function(e) {
            e.preventDefault();
            
            var $cancelPopup = $('.popup_booking_wrapper');
            var popupId = $cancelPopup.data('popup-id');

            var ticket = this.getTicketOfFilteredList(popupId);
            var ticketItem = ticket.item;
            var index = ticket.index;

            var type = ticketItem.reservationType;

            // update reservation type For DB
            var result = this.ajaxWrapper({ 
                method : 'PUT', 
                url : '/api/reservations', 
                data : {
                    rid : popupId,
                    reservationType : type
                }
            });

            result.done( function (res) {
                
                if(res) {
                    if(type === 1 || type === 2) {
                        this.filteredTickets[type].tickets.splice(ticket.index, 1);
                        ticketItem.reservationType = 4;
                        ticketItem.isCanceled = true;
                        this.filteredTickets[ticketItem.reservationType].tickets.push(ticketItem);
                    }

                    this.rendering(this.currentTab);
                    $cancelPopup.hide();
                    
                } else {
                    alert('예매 취소에 실패했습니다.');
                }
                
            }.bind(this));

            result.fail( function(err) {
                alert('예매 취소에 실패했습니다.');
            });

            
            
            
        },


        ajaxWrapper : function(options) {
            return $.ajax({
                contentType : 'application/json; charset=UTF-8',
                method : options.method,
                url : options.url,
                data : JSON.stringify(options.data) || null,
                dataType : 'json'
            });
        },


    };

    SimpleTicket.init();
    


})($)
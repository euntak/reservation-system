
define('Flicking', ['jquery','eg','utils'], function($, egComponent, utils) {
    var Flicking = utils.extends(egComponent, {

        /**
         * Default Options Object
         * @param viewTime : 슬라이드 애니메이션 시간
         * @param rollingTime : 슬라이드 자동 넘어가는 시간
         * @param restartTime : 슬라이드 재시작 대기 시간
         * @param autoStart : 슬라이드 자동 시작 여부
         * @param circulation : 순환 방식 적용 여부
         * @param intervalTimer : setInterval 변수
         * @param clickTimer : setTimeout 변수

         * @param index : 현재 슬라이드 위치
         * @param size  : 슬라이드 총 갯수
         *
         */

        _config : {
            viewTime: 500,
            rollingTime: 2000,
            restartTime: 2000,
            autoStart: true,
            circulation : true,
            flicking : false,
            direction : 'next',
            intervalTimer : null,
            clickTimer : null,
            index: 1,
            size: 0
        },


        init : function(element, options) {

            this.$wrapper = element;

            options && this._setOptions(options);
            options.circulation && this._isCirculation();
            options.autoStart && this._carouselStart();
            options.flicking && this._flickingInit();

        },


        _setOptions : function(options) {
            var $children = this.$wrapper.children('li');
            this._config.size = $children.length;

            $children.length && (options.$list = $children);

            this._config = $.extend({}, this._config, options);

        },


        _isCirculation : function() {
            var $slider = this.$wrapper;
            var $items = this.$wrapper.children('li');
            var options = this._config;
            var itemWidth = $items.width();

            // 순환 [적용] 일 경우
            if(options.circulation) {

                // 아이템의개수가 3이상이면, 마지막 슬라이드를 첫번째 앞에 붙이고 마지막 슬라이드를 삭제
                // 아이템 개수가 3개 미만일 경우에는 앞뒤에 붙여준다.
                if($items.length < 3) {
                    var cloneLast = $items.last().clone(true);
                    var cloneFirst = $items.first().clone(true);

                    $items.first().before(cloneLast);
                    $items.last().after(cloneFirst);
                } else {
                    $items.first().before($tems.last());
                }

                $slider.css({'left' : -itemWidth}); // css 초기화
            } else {
                $slider.css({'left' : 0});
            }
        },


        clear : function() {
            this._config = $.extend({}, this._config, {
                index : 1,
                size : 0
            });
            this.$wrapper.unbind().off();
        },


        _carouselStart: function() {
            var options = this._config;

            options.intervalTimer = setInterval(function(){
                this.move(options.viewTime, 'next', 0); // auto increment
            }.bind(this), options.rollingTime);
        },


        _carouselStop : function() {
            var options = this._config;

            options.intervalTimer != null
            && clearInterval(options.intervalTimer);

            // clear & reset timer
            clearTimeout(options.clickTimer);

            if(options.autoStart) {
                // 4초 아무 이벤트 없을 시 리스타트 캐러셀
                options.clickTimer = setTimeout(function() {
                    this._carouselStart();
                }.bind(this), options.restartTime);
            }
        },


        _flickingInit : function() {
            var options = this._config;
            var $slider = $(this.$wrapper);
            var startX = 0;
            var movePosition = 0;
            var itemWidth = $slider.find('li').width();
            var saveX = -itemWidth * (options.index-1);

            $slider.on('touchstart', function(e) {
                if ( e.type === 'touchstart' && e.touches.length === 1 ) {
                    startX = e.changedTouches[0].pageX;
                    saveX = parseInt($slider.css('left'));
                    e.preventDefault();
                }
            });

            $slider.on('touchmove', function(e) {
                e.preventDefault();
                var drag = 0;
                var scroll = 0;

                if ( e.type === 'touchmove' && e.touches.length === 1 ) {
                    drag = e.changedTouches[0].pageX - startX;
                    movePosition = ( drag / itemWidth ) * 100;

                    if(!this._isPossibleMove(options, movePosition)) return false;

                    if (Math.abs(drag) > Math.abs(scroll)) {

                        $slider.css({'left' : saveX + movePosition });
                        e.preventDefault();
                    }
                }
            }.bind(this));

            $slider.on('touchend', function(e) {
                if ( e.type === 'touchend' && e.touches.length === 0 ) {

                    if(!this._isPossibleMove(options, movePosition)) return false;

                    if (Math.abs( movePosition ) > 40 ) {

                        if(movePosition < 0) { // next
                            this.move('next', movePosition);
                        } else { // prev
                            this.move('prev', movePosition);
                        }

                    } else {
                        $slider.css({ 'left' : saveX }); // 초기화
                    }

                    startX = 0;
                    movePosition = 0;

                    e.preventDefault();
                }
            }.bind(this));
        },


        _isPossibleMove : function(options, movePosition) {
            var index = options.index;
            var size = options.size;
            var circulation = options.circulation;


            if(index === 1 && size === 1 && !circulation) {
                return false;
            }
            else if(index === 1 && !circulation) {
                // 첫번째 페이지에서 왼쪽으로 터치이벤트시 실행되지 않는다.
                if(movePosition >= 0) return false;
            }
            else if(index === size && !circulation) {
                // 마지막 페이지에서 오른쪽으로 터치이벤트시 실행되지 않는다.
                if(movePosition <= 0) return false;
            }

            return true;
        },


        _isReachedToEnd : function(direction, options) {
            var isReached = false;

            if(direction === 'prev') {
                if((options.index === 1) && !options.circulation) isReached = true;
            } else {
                if((options.index === options.size) && !options.circulation) isReached = true;
            }

            return isReached;
        },


        move : function(direction, swipe) {

            this.trigger('beforeMove', this._config);

            var options = this._config;
            var duration = options.viewTime;

            // autoStart == true 일 경우 carouselStop 호출
            options.autoStart && this._carouselStop();

            var $slider = $(this.$wrapper);
            var $item = $slider.find('li');
            var itemWidth = $item.width();
            var leftIndent = parseInt($slider.css('left'));
            var movePosition = parseInt(swipe) || 0;

            if(direction === 'prev') {

                if(this._isReachedToEnd(direction, options)) return false;

                $slider.filter(':not(:animated)').animate({ "left" : leftIndent + (itemWidth - movePosition) }, duration,
                    function complete() {
                        options.circulation && $item.first().before($item.last());
                        options.circulation && $slider.css({'left' : -itemWidth}); // 초기화
                        !options.circulation && $slider.css({'left' : -itemWidth * (options.index-1)});

                    });

                --options.index;

                if(options.index <= 0) {
                    options.index = options.size;
                }

            } else {

                if(this._isReachedToEnd(direction, options)) return false;

                $slider.filter(':not(:animated)').animate({ "left" : leftIndent - (itemWidth + movePosition) }, duration,
                    function complete() {
                        options.circulation && $item.last().after($item.first());
                        options.circulation && $slider.css({'left' : -itemWidth}); // 초기화
                        !options.circulation && $slider.css({'left' : -itemWidth * (options.index-1)});

                    });

                ++options.index;

                if(options.index > options.size) {
                    options.index = 1;
                }
            }

            this.trigger('afterMove', options);
        }

    });

    return Flicking;
});

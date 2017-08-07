//requireJS 기본 설정 부분
requirejs.config({

    baseUrl: '/resources',

    paths:{

        'jquery': 'node_modules/jquery/dist/jquery.min',
        'eg': 'node_modules/@egjs/component/dist/component',
        'hbs': 'node_modules/require-handlebars-plugin/hbs',
        'utils': 'js/common/util',
        'NaverMap': 'js/module/naverMap',
        'Flicking': 'js/module/flicking_component',

        'comments/wrapper': 'templates/comments/comments-wrapper',
        'comments/popupp-list': 'templates/comments/popup-photo-list',

        'detail/confirm-button': 'templates/detail/confirm-button',
        'detail/event-info': 'templates/detail/event-info',
        'detail/image-slider': 'templates/detail/image-slider',
        'detail/path-info': 'templates/detail/path-info',
        'detail/product-info': 'templates/detail/product-info',
        'detail/product-links': 'templates/detail/product-links',
        'detail/toggle-content': 'templates/detail/toggle-content'
    },

    shim:{
        'Flicking':{
            deps: ['jquery', 'eg', 'utils'],
            exports: 'Flicking'
        }
    }
});

//requireJS를 활용하여 모듈 로드
requirejs(['jquery',
        'hbs!comments/wrapper',
        'hbs!comments/popupp-list',
        'hbs!detail/confirm-button',
        'hbs!detail/event-info',
        'hbs!detail/image-slider',
        'hbs!detail/path-info',
        'hbs!detail/product-info',
        'hbs!detail/product-links',
        'hbs!detail/toggle-content'
    ],

    function ($,
              commentsWrapper, commentsPopupList,
              detailConfirmBtn, detailEventInfo, detailImageSlider,
              detailPathInfo, detailProductInfo, detailProductLinks,
              detailToggleContent) {

        'use strict';

        var DetailProduct = {

            comments : [],
            mainSlider : null,
            popupSlider : null,


            init : function () {
                this.getProduct();
                this.getPreviewComments();
                this.bindEvents();
                this.imageLazyLoad();
            },


            bindEvents : function () {
                $('.section_store_details').on('click', '._open, ._close', this.contentMoreToggle.bind(this));
                $('.info_tab_lst').on('click', '._detail, ._path', this.changeInfoTab.bind(this));
                $('.short_review_area').on('click', '.thumb', this.openPhotoViewer.bind(this));
                $('.popup').on('click', 'label', this.closePhotoViewer.bind(this));

                /* 예매하기 버튼 이벤트 추가 */
                $('.section_btn').on('click','.bk_btn',this.conformReservation.bind(this));

            },


            getProduct : function() {
                var url = '/api' + window.location.pathname;
                var result = this.ajaxWrapper('GET', url, null);

                result.then(function(res) {
                    var $slider = $('.visual_img');
                    var $btnContainer = $('.group_visual');

                    this.updateMainStatus({
                        index : 1,
                        size : res.product.files.length
                    });
                    this.titleAreaRendering(res);
                    this.detailInfoRendering(res.product);


                    requirejs(['Flicking'], function(Flicking) {

                        this.mainSlider = new Flicking($slider, {
                            autoStart: false,
                            circulation: false,
                            flicking: true,
                            viewTime: 300
                        }).on({
                            afterMove: this.updateMainStatus.bind(this.options)
                        });

                        $btnContainer.on('click', '.prev', this.mainSlider.move.bind(this.mainSlider, 'prev', 0));
                        $btnContainer.on('click', '.nxt', this.mainSlider.move.bind(this.mainSlider, 'next', 0));

                    }.bind(this));





                }.bind(this));

            },


            getPreviewComments : function() {
                var url = '/api' + window.location.pathname + '/comments?limit=3';
                var result = this.ajaxWrapper('GET', url, null);

                result.then(function(res) {

                    // 소수점 한자리 고정
                    this.comments = res.comments.map(function(item) {
                        item.score = item.score.toFixed(1);
                        return item;
                    });

                    this.previewCommentRendering(res);

                }.bind(this));
            },


            titleAreaRendering : function(res) {
                var product = res.product;
                var statusText = this.setConfirmButton(product.salesEnd, product.salesFlag);

                $('.visual_img').append(detailImageSlider(product));
                $('.group_btn_goto').append(detailProductLinks(product));
                $('.store_details').append(detailToggleContent(product));
                $('.section_event').append(detailEventInfo(product));
                $('.section_btn').append(detailConfirmBtn(statusText));
            },


            detailInfoRendering : function(product) {

                $('.detail_info_group').prepend(detailProductInfo(product));
                $('.box_store_info').append(detailPathInfo(product));

                requirejs(['NaverMap'], function(NaverMap) {
                    NaverMap.init();
                });
            },


            previewCommentRendering : function(res) {

                var rating = (res.total.average / 5.0) * 100;
                var $commentContainer = $('.short_review_area');
                var $reviewMoreBtn = $('.btn_review_more');

                $reviewMoreBtn.attr('href', window.location.pathname + '/reviews');
                $commentContainer.append(commentsWrapper(res));

                // comment total rating (%)
                $('.graph_value').css('width', rating + '%');
            },


            updateMainStatus : function (options) {

                var index = options.index;
                var size = options.size;
                var $container = $('.figure_pagination');
                var $indexElem = $container.find('span.num:first');
                var $totalElem = $container.find('span.num.off');
                var $prevBtn = $('.prev').find('i');

                $indexElem.text(index);
                $totalElem.text(' / ' + size);

                if(index === 1) {
                    $prevBtn.toggleClass('off');
                    return false;
                }

                $prevBtn.removeClass('off');

            },


            updatePreviewStatus : function (options) {
                var index = options.index;
                var size = options.size;

                var $container = $('.preview_center');
                var $indexElem = $container.find('span.index');
                var $totalElem = $container.find('span.size');

                $indexElem.text(index);
                $totalElem.text(size);
            },


            /**
             * 펼처보기 toggle
             */
            contentMoreToggle : function (e) {
                e.preventDefault();

                var $el = $(e.target);
                var $container = $el.closest('.section_store_details');
                var $content = $container.find('.store_details');
                var $openBtn = $container.find('._open');
                var $closeBtn = $container.find('._close');

                // 닫혀있음
                if($content.hasClass('close3')) {
                    $openBtn.hide();
                    $closeBtn.show();
                } else {
                    $openBtn.show();
                    $closeBtn.hide();
                }
                $content.toggleClass('close3');
            },


            /**
             * confirmButton statusText setting
             */
            setConfirmButton : function(endDate, isSoldout) {
                var statusText = '예매하기';
                this.productStatus = 1;

                if(isSoldout) {
                    statusText = '매진';
                    this.productStatus = 2;
                }

                if(this.isProductExpire(endDate)) {
                    statusText = '판매 종료';
                    this.productStatus = 3;
                }

                return {
                    statusText : statusText
                }
            },


            /**
             * Diff Date
             */
            isProductExpire : function(endDate) {
                var end = new Date(endDate);
                var now = new Date();

                return end < now;
            },


            /**
             * 상세정보 / 오시는길 Tab 전환
             */
            changeInfoTab : function(e) {
                e.preventDefault();
                e.stopPropagation();

                var $ele = $(e.target);
                var $container = $ele.closest('.section_info_tab');
                var $infoTab = $container.find('._detail .anchor');
                var $pathTab = $container.find('._path .anchor');

                var $infoContent = $container.find('.detail_area_wrap');
                var $pathContent = $container.find('.detail_location');

                if($ele.closest('li').hasClass('active')) return false;

                else {
                    $container.find('li').removeClass('active');
                    $ele.closest('li').addClass('active');

                    if($ele.closest('li').hasClass('_detail')) {
                        $infoTab.addClass('active');
                        $pathTab.removeClass('active');
                        $infoContent.removeClass('hide');
                        $pathContent.addClass('hide');

                    } else { // map setting
                        $infoTab.removeClass('active');
                        $pathTab.addClass('active');
                        $infoContent.addClass('hide');
                        $pathContent.removeClass('hide');
                    }
                }

            },


            /**
             * Popup 형식의 사진 뷰어 ( 댓글 단 사람들의 사진 ) Open
             */
            openPhotoViewer : function(e) {
                e.preventDefault();

                var cid = $(e.target).closest('li').data('cid');

                var $photoViewer = $('#photoviewer');
                var $photoList = $('.photo_list');
                var $popupTitle = $('.popup_title');

                var filteredComment = this.comments.filter(function(comment) {
                    if(comment.cid === cid) return comment;
                });

                var comment = filteredComment[0];
                var imageCount = comment.images.length;

                $photoList.append(commentsPopupList(comment.images));

                this.updatePreviewStatus({
                    index : 1,
                    size : imageCount
                });

                requirejs(['Flicking'], function(Flicking) {
                    this.popupSlider = new Flicking($photoList, {
                        autoStart: false,
                        circulation: false,
                        flicking: true,
                        viewTime: 300
                    }).on({
                        afterMove: this.updatePreviewStatus.bind(this.options)
                    });

                    $popupTitle.on('click', '.prev', this.popupSlider.move.bind(this.popupSlider, 'prev', 0));
                    $popupTitle.on('click', '.nxt', this.popupSlider.move.bind(this.popupSlider, 'next', 0));
                }.bind(this));




                $photoViewer.show();
            },


            /**
             * PhotoViewer Close
             */
            closePhotoViewer : function(e) {
                e.preventDefault();

                $('#photoviewer').hide();
                $('.photo_list li').remove();
                $('.photo_list').css('left' , 0);
                $('.popup_title').off('click');

                this.popupSlider.clear();
            },


            // 예매하기 버튼 클릭
            conformReservation : function(e){
                e.preventDefault();

                if(this.productStatus === 1) {
                    var productId = $('.visual_txt').data('id');
                    location.href='/products/' + productId + '/reserve';
                }

                return false;
            },


            ajaxWrapper : function(method, url, data) {
                return $.ajax({
                    contentType : 'application/json; charset=UTF-8',
                    method : method,
                    url : url,
                    data : data,
                    dataType : 'json'
                });
            },


            imageLazyLoad : function() {
                var $window = $(window);
                // event에 namespace를 걸 수 있다.
                // callback 함수를 등록할때에 on으로 붙일 수 있고, off로 땔 수 있다. .off('scroll', onScroll);
                var $image = $('img[data-lazy-image]');

                function imageLazyLoading() {
                    $.each($image, function() {
                        if ( $(this).data('lazy-image') && $(this).offset().top < ($window.scrollTop() + $window.height() + 100) ) {
                            var source = $(this).data('lazy-image');
                            $(this).attr('src', source);
                            $(this).removeAttr('data-lazy-image');
                        }
                    })
                }

                if($image.length > 0) {
                    $window.on('scroll.lazyloading', imageLazyLoading);
                } else {
                    $window.off('scroll.lazyloading', imageLazyLoading);
                }

            }


        };

        DetailProduct.init();
});

(function($) {

    var Review = extend(eg.Component, {


        init : function () {
            this.popupSlider = null;
            this.commentsRating = {};
            this.comments = [];
            this.Template = {};
            this.options = {
                limit : 10,
                offset : 0,
                pathname : ''
            };

            this.initialHandlebars();
            this.getInitComments();
            this.bindEvents();
        },


        bindEvents : function () {
            $('.short_review_area').on('click', '.thumb', this.openPhotoViewer.bind(this));
            $('.popup').on('click', 'label', this.closePhotoViewer.bind(this));
        },


        initialHandlebars : function() {

            this.Template.simpleProduct = Handlebars.compile($('#simple_product_templ').html());
            this.Template.commentRating = Handlebars.compile($('#comment_rating_templ').html());
            this.Template.comment = Handlebars.compile($('#comment_templ').html());
            this.Template.photoViewer = Handlebars.compile($('#comment_photo_templ').html());
        },


        getInitComments : function() {

            var url = this.getCommentsUrl(this.options);
            var result = this.ajaxWrapper('GET', url, null);

            result.then(function(res) {

                // 소수점 한자리 고정
                this.comments = this.comments.concat(res.comments.map(function(item) {
                    item.score = item.score.toFixed(1);
                    return item;
                }));

                this.simpleProductRendering(res);
                this.commentInitRendering(res);
                this.commentsRendering(res);

            }.bind(this));

        },

        simpleProductRendering : function(res) {
            var Templates = this.Template;
            var $container = $('.wrap_review_list');
            $container.prepend(Templates.simpleProduct(res.total));
        },


        getMoreComments : function(options) {

            if(this.totalCount === this.comments.length) {
                console.log('더 이상 불러 올 댓글이 없습니다.');
                return false;
            }

            if(options !== undefined) {
                this.options = $.extend({}, this.options, options);
            }

            var url = this.getCommentsUrl(this.options);
            var result = this.ajaxWrapper('GET', url, null);

            result.then(function(res) {

                // 소수점 한자리 고정
                this.comments = this.comments.concat(res.comments.map(function(item) {
                    item.score = item.score.toFixed(1);
                    return item;
                }));

                this.commentsRendering(res);

            }.bind(this));
        },



        getCommentsUrl : function(options) {
            var productId = window.location.pathname.split('/')[2];
            var pathname = this.options.pathname = '/products/' + productId;
            var baseUrl = '/api' + pathname + '/comments';

            var queryString = [];

            if(options.limit > 0) {
                queryString.push('?limit=' + options.limit);

                if(options.offset > 0) {
                    queryString.push('&offset=' + options.offset);
                }
            } else {
                if(queryString.length > 0) {
                    queryString.push('&offset=' + options.offset);
                } else {
                    queryString.push('?offset=' + options.offset);
                }
            }


            return (baseUrl += queryString.map(function(item) {
                return item;
            }).join(''));
        },



        commentInitRendering : function(res) {
            var Templates = this.Template;
            this.totalCount = res.total.totalCount;

            var rating = (res.total.average / 5.0) * 100;
            var $commentContainer = $('.short_review_area');

            $commentContainer.prepend(Templates.commentRating(res.total));

            // comment total rating (%)
            $('.graph_value').css('width', rating + '%');
        },


        commentsRendering : function(res) {
            var Templates = this.Template;

            var $commentList = $('.list_short_review');
            $commentList.append(Templates.comment(res));
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
         * Popup 형식의 사진 뷰어 ( 댓글 단 사람들의 사진 ) Open
         */
        openPhotoViewer : function(e) {
            e.preventDefault();

            var Templates = this.Template;
            var cid = $(e.target).data('cid');

            var $photoViewer = $('#photoviewer');
            var $photoList = $('.photo_list');

            var filteredComment = this.comments.filter(function(comment, i) {
                if(comment.cid === cid) return comment
            });

            var comment = filteredComment[0];
            var imageCount = comment.images.length;

            $photoList.append(Templates.photoViewer(comment.images));

            this.updatePreviewStatus({
                index : 1,
                size : imageCount
            });

            this.popupSlider = new Flicking($('.photo_list'), {
                autoStart: false,
                circulation: false,
                flicking: true,
                viewTime: 300,
            }).on({
                afterMove: this.updatePreviewStatus.bind(this.options)
            });

            $('.popup_title').on('click', '.prev', this.popupSlider.move.bind(this.popupSlider, 'prev', 0));
            $('.popup_title').on('click', '.nxt', this.popupSlider.move.bind(this.popupSlider, 'next', 0));

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

        },


        infiniteScroll : function(isActive) {
            var $window = $(window);
            console.log(this);

            if(isActive) {
                $(document).scroll(function() {
                    if ($(document).height() <=  $window.scrollTop() + $window.height()) {
                        this.getMoreComments.call(this, {
                            offset : this.options.offset + 10
                        });
                    }
                }.bind(this));
            }
        }


    });


    var review = new Review();
    review.infiniteScroll(true);
    
})($);
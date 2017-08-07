define('ReviewResultForm', ['jquery', 'utils', 'eg'], function($, utils, egComponent) {
    return utils.extends(egComponent, {
        init: function ($root) {
            this.$root = $root;
            this.score = 0;
            this.comment = "";
            this.images = [];
            this.productId = $root.data("id");
            this.formData = {};
            this.bindEvents();
        },


        bindEvents: function () {
            this.$root.on('click', '.bk_btn', this.registerReview.bind(this));
        },


        updateRate: function (event, score) {
            this.score = score;
        },


        updateComment: function (event, comment) {
            this.comment = comment;
        },


        updateImage: function (event, image) {
            this.images = image;
        },


        registerReview: function () {

            if (!this.checkInputValue(this.comment)) {
                alert('5글자 이상 작성해 주세요');
                return false;
            }

            this.formData = new FormData();
            this.formData.append('score', this.score);
            this.formData.append('comment', this.comment);
            this.formData.append('productId', this.productId);

            for (var i = 0; i < this.images.length; i++) {
                this.formData.append('file', this.images[i]);
            }

            var afterRegister = $.ajax({
                url: '/api/comments',
                processData: false,
                contentType: false,
                data: this.formData,
                method: 'POST'
            });

            afterRegister.done(function (res) {
                location.href = '/my';
            });

            afterRegister.fail(function (err) {
                console.error('fail! new review', err);
            })
        },

        checkInputValue: function (comment) {
            return comment.length >= 5;
        }

    });
});
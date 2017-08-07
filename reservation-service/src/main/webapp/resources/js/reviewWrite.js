requirejs.config({

    baseUrl: '/resources',

    paths:{

        'jquery': 'node_modules/jquery/dist/jquery.min',
        'eg': 'node_modules/@egjs/component/dist/component',
        'hbs': 'node_modules/require-handlebars-plugin/hbs',
        'utils': 'js/common/util',

        'Comment': 'js/component/Comment',
        'PreviewImage': 'js/component/PreviewImage',
        'ReviewRating': 'js/component/ReviewRating',
        'ReviewResultForm': 'js/component/ReviewResultForm',

        'review/thumbImage': 'templates/review/thumb-image'
    },
    shim:{
        'Comment': {
            deps: ['jquery', 'eg', 'utils'],
            exports: 'Comment'
        },
        'PreviewImage': {
            deps: ['jquery', 'eg', 'utils'],
            exports: 'PreviewImage'
        },
        'ReviewRating': {
            deps: ['jquery', 'eg', 'utils'],
            exports: 'ReviewRating'
        },
        'ReviewResultForm': {
            deps: ['jquery', 'eg', 'utils'],
            exports: 'ReviewResultForm'
        },
    }
});

requirejs(['jquery', 'Comment', 'PreviewImage', 'ReviewRating', 'ReviewResultForm'],

function ($, Comment, PreviewImage, ReviewRating, ReviewResultForm) {

    var rating = new ReviewRating($('.rating'));
    var comment = new Comment($('.review_contents.write'), $('.guide_review span').eq(0));
    var previewImage = new PreviewImage($('.review_write_footer_wrap'));
    var resultForm = new ReviewResultForm($('.box_bk_btn'));

    rating.on('afterChange', resultForm.updateRate.bind(resultForm));
    comment.on('afterChange', resultForm.updateComment.bind(resultForm));
    previewImage.on('afterChange', resultForm.updateImage.bind(resultForm));
});

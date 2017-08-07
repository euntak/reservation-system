define('ReviewRating', ['jquery', 'utils', 'eg'], function($, utils, egComponent) {
    return utils.extends(egComponent, {

        init : function ($root){
            this.$root=$root;
            this.$stars=$root.find('.rating_rdo');
            this.$score=$root.find('.star_rank');

            this.bindEvents();
        },


        bindEvents : function(){
            this.$stars.on('click',this.changeRate.bind(this));
        },


        changeRate : function(event){
            this.score = $(event.target).val();
            this.changeStar();
            this.changeScore();
            this.trigger('afterChange',event,this.getScore());
        },


        changeStar : function(event){
            this.$stars.prop('checked',false);
            for(var i = 0; i <= this.score ; i++){
                this.$stars.eq(i).prop('checked',true);
            }
        },


        changeScore : function(){
            if(this.score>0){
                this.$score.removeClass('gray_star');
            }
            this.$score.text(this.score);
        },


        getScore : function(){
            return this.score;
        }

    });
});


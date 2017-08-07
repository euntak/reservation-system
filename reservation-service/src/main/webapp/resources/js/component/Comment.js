define('Comment', ['jquery', 'utils', 'eg'], function($, utils, egComponent) {
     return utils.extends(egComponent,{


        init : function($root,$textCount){
            this.$root = $root;
            this.$infoText = $root.find('.review_write_info');
            this.$textArea = $root.find('.review_textarea');
            this.$textCount = $textCount;
            this.bindEvents();
        },


        bindEvents : function(){
            this.$infoText.on('click',this.startEdit.bind(this));
            this.$textArea.on('keyup',this.updateTextCount.bind(this));
        },


        startEdit : function (event){
            this.$infoText.addClass('hide');
            this.$textArea.focus();
        },


        updateTextCount : function(){
            this.textCount = this.$textArea.val().length;
            this.$textCount.text(this.textCount);
            this.comment = this.$textArea.val();

            this.trigger('afterChange',event,this.getComment());
        },


        getComment : function(){
            return this.comment;
        }


    });
});

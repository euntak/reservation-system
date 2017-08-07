define('PreviewImage', ['jquery', 'utils', 'eg', 'hbs!review/thumbImage'], function($, utils, egComponent, thumbImage) {
    return utils.extends(egComponent,{


        init : function($root){
            this.$root = $root;
            this.$inputBtn = $root.find('input[type=file]');
            this.$previewImage = $root.find('.lst_thumb');

            this.image = [];
            this.bindEvents();
        },


        bindEvents : function(){
            this.$inputBtn.on('change',this.loadImage.bind(this));
            this.$previewImage.on('click','.spr_book.ico_del', this.deleteImage.bind(this));
        },


        loadImage : function(event){
            var input =event.target;
            for(var i = 0; i < input.files.length ; i++){
                var reader=new FileReader();
                reader.onload = (function(e){
                    var result = {};
                    result.data= e.target.result;
                    this.$previewImage.append(thumbImage(result));
                }).bind(this);
                this.image.push(input.files[i]);
                reader.readAsDataURL(input.files[i]);
            }
            this.trigger('afterChange',event, this.getImage());
        },


        deleteImage : function(event){
            var $ul = $(event.target).closest('.lst_thumb').find('li');
            var $target = $(event.target).closest('.item');
            var index = $ul.index($target);

            $target.remove();
            this.image.splice(index,1);
            this.trigger('afterChange',event, this.getImage());
        },


        getImage: function (){
            return this.image;
        }


    });
});
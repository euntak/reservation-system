$(function(){
    var Rating = extend(eg.Component,{

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

    var Comment = extend(eg.Component,{


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

    var PreviewImage = extend(eg.Component,{


        init : function($root){
            this.$root = $root;
            this.$inputBtn = $root.find('input[type=file]');
            this.$previewImage = $root.find('.lst_thumb');
        
            this.imageTemplate =  Handlebars.compile($('#thumb_image_templ').html());
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
                    this.$previewImage.append(this.imageTemplate(result));
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

    var ResultForm = extend(eg.Component,{


        init : function ($root){
            this.$root = $root;
            this.score=0;
            this.comment="";
            this.images=[];
            this.productId = $root.data("id");
            this.formData ={};
            this.bindEvents();
        },


        bindEvents : function (){
            this.$root.on('click','.bk_btn',this.registerReview.bind(this));
        },


        updateRate : function (event, score){
            this.score = score;
        },


        updateComment : function(event, comment){
            this.comment = comment;
        },


        updateImage : function(event, image){
            this.images = image;
        },


        registerReview : function(){

            if(!this.checkInputValue(this.comment)){
                alert('5글자 이상 작성해 주세요');
                return false;
            }

            this.formData = new FormData();
            this.formData.append('score', this.score);
            this.formData.append('comment',this.comment);
            this.formData.append('productId',this.productId);
            
            for(var i = 0; i <this.images.length; i++){
                this.formData.append('file',this.images[i]);
            }

            var afterRegister = $.ajax({
                url : '/api/comments',
                processData : false,
                contentType : false,
                data : this.formData,
                method : 'POST'
            });

            afterRegister.done(function(res) {
                if(res > 0) location.href = '/my';
            });

            afterRegister.fail(function(err) {
                console.error('fail! new review', err);
            })
        },

        checkInputValue : function(comment) {
            return comment.length >= 5;
        }

    });


    var rating = new Rating($('.rating'));
    var comment = new Comment($('.review_contents.write'), $('.guide_review span').eq(0));
    var previewImage = new PreviewImage($('.review_write_footer_wrap'));
    var resultForm = new ResultForm($('.box_bk_btn'));
    
    rating.on('afterChange', resultForm.updateRate.bind(resultForm));
    comment.on('afterChange', resultForm.updateComment.bind(resultForm));
    previewImage.on('afterChange', resultForm.updateImage.bind(resultForm));

});

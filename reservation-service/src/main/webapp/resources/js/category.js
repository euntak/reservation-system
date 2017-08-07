/**
 * category solip~~~
 */
(function(window) {
	'use strict';

	var Category = {
		init : function() {
			this.bindEvents()
		},

		bindEvents : function() {
			$(".cate_wrapper").on("click", ".delete", this.deleteCategory);
			$(".cate_wrapper").on("click", ".update", this.openEditor);
			$(".cate_wrapper").on("click", ".complete", this.updateComplete);
		},

		deleteCategory : function(event) {
			var $category = $(this).closest("div");
			var id = $category.data("id");
			var url = '/category/' + id;

			var result = Category.cateApi('DELETE', url);

			result.done(function(res) {
				if (res)
					$category.remove();
			});
		},

		openEditor : function(event) {
			var $editor = $(this).prev("input[name='edit']");
			var category = $(this).closest("div").find("#name").html().trim();

			$(this).text("Complete");
			$(this).removeClass("update").addClass("complete");
			$editor.val(category).show().focus();
		},

		updateComplete : function(event) {
			var $category = $(this).closest("div");
			var $editor = $(this).prev("input[name='edit']");

			var id = $category.data("id");
			var newName = $editor.val().trim();

			$(this).text("Update");
			$(this).removeClass("complete").addClass("update");

			var result = Category.cateApi('PUT', '/category', JSON.stringify({
				id : id,
				name : newName
			}));

			result.done(function(res) {
				if (res) {
					$category.find("#name").html(newName.trim());
					$editor.val("").hide();
				}
			});
		},

		cateApi : function(method, url, data) {
			return $.ajax({
				contentType : 'application/json; charset=UTF-8',
				method : method,
				url : url,
				data : data,
				dataType : 'json',
			});
		}
	};

	Category.init();

})(window);

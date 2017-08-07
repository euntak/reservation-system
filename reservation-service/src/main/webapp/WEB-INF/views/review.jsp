<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">

<head>
    <meta charset="utf-8">
    <meta name="description" content="네이버 예약, 네이버 예약이 연동된 곳 어디서나 바로 예약하고, 네이버 예약 홈(나의예약)에서 모두 관리할 수 있습니다.">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
    <title>네이버 예약</title>
    <link href="/resources/css/style.css" rel="stylesheet">
    <link href="/resources/css/photoviewer.css" rel="stylesheet">
    <script src="/resources/lib/handlebars.min.js"></script>
</head>

<body>
    <div id="container">
		<!-- [D] 예약하기로 들어오면 header에 fade 클래스 추가로 숨김 -->
		<div class="header fade">
			<header class="header_tit">
				<h1 class="logo">
					<a href="#" class="lnk_logo" title="네이버"> <span class="spr_bi ico_n_logo">네이버</span> </a>
					<a href="#" class="lnk_logo" title="예약"> <span class="spr_bi ico_bk_logo">예약</span> </a>
				</h1>
				<a href="#" class="btn_my"> <span title="내 예약">MY</span> </a>
			</header>
		</div>
        <div class="ct">
            <div class="wrap_review_list">
                <script id="simple_product_templ" type="text/handlebars-template">
                    <div class="review_header">
                        <div class="top_title gr">
                            <a href="/products/{{pid}}" class="btn_back" title="이전 화면으로 이동"> <i class="fn fn-backward1"></i> </a>
                            <h2><a class="title" href="#">{{name}}</a></h2>
                        </div>
                    </div>
                </script>

                <div class="section_review_list">
                    <div class="review_box">
                        <h3 class="title_h3">예매자 한줄평</h3>
                        <div class="short_review_area">
                            <script id="comment_rating_templ" type="text/handlebars-template">
                                <div class="grade_area">
                                    <!-- [D] 별점 graph_value는 퍼센트 환산하여 width 값을 넣어줌 -->
                                    <span class="graph_mask"> <em class="graph_value" style="width: 84%;"></em> </span>
                                    <strong class="text_value"> <span>{{average}}</span> <em class="total">5.0</em> </strong>
                                    <span class="join_count"><em class="green">{{totalCount}}건</em> 등록</span>
                                </div>
                            </script>
                            <ul class="list_short_review">
                            <script id="comment_templ" type="text/x-handlebars-template">
                                {{#each comments}}
                                <li class="list_item">
                                    <div>
                                        <div class="review_area">
                                            {{#each images}}
                                                <div class="thumb_area">
                                                     {{#if @first}}
                                                    <a href="#" data-cid="{{../cid}}" class="thumb" title="이미지 크게 보기"> <img width="90" height="90" class="img_vertical_top" src="/files/{{fileId}}" alt="리뷰이미지"> </a>
                                                    <span class="img_count">{{../images.length}}</span>
                                                     {{/if}}
                                                </div>
                                            {{/each}}
                                            <h4 class="resoc_name">{{name}}</h4>
                                            <p class="review">{{comment}}</p>
                                        </div>
                                        <div class="info_area">
                                             <div class="review_info"> <span class="grade">{{score}}</span> <span class="name">{{nickname}}</span> <span class="date">{{modifyDate}}. 방문</span> </div>
                                        </div>
                                    </div>
                                </li>
                                {{/each}}
                            </script>
                            </ul>
                        </div>
                        <p class="guide"> <i class="spr_book2 ico_bell"></i> <span>네이버 예약을 통해 실제 방문한 이용자가 남긴 평가입니다.</span> </p>
                    </div>
                </div>
            </div>
        </div>
        <hr> </div>
		<footer>
	        <div class="gototop">
	            <a href="#" class="lnk_top"> <span class="lnk_top_text">TOP</span> </a>
	        </div>
	        <div id="footer" class="footer">
	            <p class="dsc_footer">네이버(주)는 통신판매의 당사자가 아니며, 상품의정보, 거래조건, 이용 및 환불 등과 관련한 의무와 책임은 각 회원에게 있습니다.</p>
	            <span class="copyright">© NAVER Corp.</span>
	        </div>
        </footer>
        <div id="photoviewer">
            <div class="popup">
                <label for="photoviewer"></label>
                <div class="popup_title">
                    <div class="prev">
                        <i class="spr_book2 arr_img_lt arr_img"></i>
                    </div>
                    
                    <div class="preview_center">
                        <h3>PHOTO VIEWER</h3>
                        <p><span class="index">1 </span><span> / <span class="size"> 0</span></span></p>
                    </div>
                    
                    <div class="nxt">
                        <i class="spr_book2 arr_img_rt arr_img"></i> 
                    </div>
                </div>
                <div class="popup_content">
                    <ul class="photo_list">
                        <script id="comment_photo_templ" type="text/x-handlebars-template">
                            {{#each this}}
                            <li class="item"> <img alt="" class="img_thumb" src="/files/{{fileId}}"></li>
                            {{/each}}
                        </script>
                    </ul>
                </div>
            </div>
        </div>
</body>
<script data-main="js/main" src="lib/require/require.js"></script>
</html>
define('NaverMap', ['jquery'], function($) {

    var NaverMap = (function($, naver) {

        function init() {
            settingByAddress();
        }

        function settingByAddress() {
            var $container = $('.store_addr_wrap');
            var $address = $container.find('.store_addr_bold');
            var address = $address.text();

            naver.maps.Service.geocode({ address: address }, function(status, response) {
                if (status === naver.maps.Service.Status.ERROR) {
                    return alert('Address is Wrong!');
                }

                // 성공시의 response 처리
                var result = response.result;   // 검색 결과의 컨테이너
                var item = result.items[0];     // 검색 결과의 배열에서 첫번째 값만 가져온다.
                var point = item.point;         // { x, y } 좌표 객체
                var address = item.address;     // 상세주소

                var mapOptions = {
                    center: new naver.maps.LatLng(point.y, point.x),
                    zoom: 11
                };

                var map = new naver.maps.Map('map', mapOptions); // initializing

                // marker
                var marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(point.y, point.x),
                    map: map
                });

                var linkUrl = 'http://map.naver.com/?lng=' + item.point.x + '&pinTitle=' + address +
                    '&level=2&pinType=SITE&lat=' + item.point.y + '&enc=utf8;'

                linkUpdate(linkUrl);

            });
        }

        function linkUpdate(linkUrl) {
            $('.store_location').attr('href', linkUrl);

            // 길찾기 버튼 링크 걸기
            $('.btn_path').attr('href', linkUrl);

            // 내비게이션 버튼 링크 걸기
            $('.btn_navigation').attr('href', 'http://map.naver.com/index.nhn?menu=route');
        }

        return {
            init: init
        }

    })($, naver);

    return NaverMap;
});

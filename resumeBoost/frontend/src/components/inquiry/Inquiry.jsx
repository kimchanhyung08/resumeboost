import React, { useEffect, useState } from 'react';

const Inquiry = () => {
  const [map, setMap] = useState(null);

  useEffect(() => {
    // 카카오 지도 API 스크립트 로드
    const script = document.createElement('script');
    script.async = true;
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=`;
    script.onload = () => {
      // 스크립트 로딩 후 지도 초기화
      if (window.kakao && !map) {
        const container = document.getElementById('map'); // 지도 div
        const options = {
          center: new window.kakao.maps.LatLng(37.657194, 127.062275), // 초기 위치 (위도, 경도)
          level: 3, // 줌 레벨
        };
        const kakaoMap = new window.kakao.maps.Map(container, options);
        setMap(kakaoMap);

        // 마커 생성
        const markerPosition = new window.kakao.maps.LatLng(37.657194, 127.062275); // 마커 위치 (위도, 경도)
        const marker = new window.kakao.maps.Marker({
          position: markerPosition, // 마커 위치 설정
        });
        marker.setMap(kakaoMap); // 지도에 마커를 표시
      }
    };
    document.head.appendChild(script);

    return () => {
      // 컴포넌트가 unmount 될 때 스크립트 제거
      document.head.removeChild(script);
    };
  }, [map]);

  return (
    <div className="inquiry">
      <div className="inquiry-con">
        <div className="inquiry-top">
          <div id="map"></div>
        </div>
        <div className="inquiry-bottom">
          <div className="inquiry-bottom-top">
            <h1>문의사항/회사정보</h1>
          </div>
          <div className="inquiry-bottom-bottom">
            <div className="inquiry-phone">
              <div className="phone-top">
                📞 전화주세요.
              </div>
              <div className="phone-bottom">
                010-0000-0000
              </div>
            </div>
            <div className="inquiry-email">
              <div className="email-top">
                📧 문의주세요.
              </div>
              <div className="email-bottom">
                resumeboost@gmail.com
              </div>
            </div>
            <div className="inquiry-address">
              <div className="address-top">
                📍 회사위치
              </div>
              <div className="address-bottom">
                서울특별시 노원구 상계동 593-1 <br />
                화일빌딩 6층 602호
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Inquiry;

import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import jwtAxios from '../../util/jwtUtils'
import { S3URL } from '../../util/constant'
import { useNavigate } from 'react-router-dom'
import { EC2_URL } from '../../constans'

const MyReview = () => {
  const isLogin = useSelector((state) => state.loginSlice)
  const [reviews, setReviews] = useState([])
  const navigate = useNavigate();
  console.log(isLogin)

  const reviewFn = async (mentorId) =>{
    try {
      const res = await jwtAxios.get(`http://${EC2_URL}:8090/review/mentorReview/${mentorId}`)
      
      if( res.data && res.data.review){
        const updatedReviews = await Promise.all(res.data.review.map(async (review) => {
          try{
            const memberRes = await jwtAxios.get(`http://${EC2_URL}:8090/member/memberDetail/${review.memberEntity.id}`)
            return { ...review, memberEntity: memberRes.data.member};
          } catch(err){
            console.error(err);
            return review
          }
        }))
        setReviews(updatedReviews)
        console.log(updatedReviews);
        
      }
    } catch (error) {
      console.error(error)
    }
  }

  const memberReviewFn = async (memberId) => {
    try {
      const res = await jwtAxios.get(`http://${EC2_URL}:8090/review/memberReview/${memberId}`);
      console.log('첫 번째 API 응답:', res);  // 응답 확인을 위한 로그 추가
  
      if (!res.data.review[0].memberEntity) {
        console.error('memberEntity가 없습니다.');
        return;  // 데이터가 없으면 더 이상 진행하지 않음
      }
  
      const memberRes = await jwtAxios.get(`http://${EC2_URL}:8090/member/memberDetail/${res.data.review[0].memberId}`);
      console.log('두 번째 API 응답:', memberRes);  // 두 번째 API 응답 확인
  
      const updatedReviews = res.data.review.map((review) => ({
        ...review,
        memberEntity: memberRes.data.member,  // memberRes의 데이터를 memberEntity에 추가
      }));
  
      setReviews(updatedReviews);
      console.log(updatedReviews);
    } catch (error) {
      console.error("Error fetching reviews:", error);
    }
  };
  
  

  useEffect(()=>{
    if(isLogin.role === "ROLE_MENTOR"){
      reviewFn(isLogin.id)
    }else {
      memberReviewFn(isLogin.id)
    }
  },[])

  function formatDate(dateString) {
    const date = new Date(dateString); // 날짜 문자열을 Date 객체로 변환
    const day = String(date.getDate()).padStart(2, '0'); // 날짜 (일자) 두 자리 숫자로
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (1부터 시작하므로 +1)
    const year = String(date.getFullYear()).slice(2); // 연도는 두 자리로
  
    return `${year}.${month}.${day}`; // 원하는 형식으로 리턴
  }

  return (
    <>
      {reviews.length !== 0?(
        <div className="review">
          <ul>
            {reviews.map((review) => (
              <li onClick={()=>navigate(`/member/mentorDetail/${review.mentorId}`)} className='review-click'>
                <div className="review-top">
                  <div className="review-top-left">
                    <div className="review-profile">
                      {review.memberEntity?.attachFile === 1 ? (
                        <img
                          src={`${S3URL}${review.memberEntity.newImgName}`}
                          alt="프로필 사진"
                          className="reply-profile-img"
                        />
                      ) : (
                        <img
                          src="/images/profile.png"
                          alt="프로필 사진"
                          className="reply-profile-img"
                        />
                      )}
                      <span>{review.memberEntity.nickName}</span>
                      <div className="v"></div>
                      <span>{review.memberEntity.address}</span>
                      <div className="v"></div>
                      <span>{review.memberEntity.age} 대</span>
                      <span className='review-last'>{review.mentorNickName}님의 리뷰</span>
                    </div>
                  </div>
                  <div className="review-top-right">
                    {formatDate(`${review.createTime}`)}
                  </div>
                </div>
                <div className="review-body">
                  <span
                    dangerouslySetInnerHTML={{
                      __html: review.content,
                    }}
                  />
                </div>
              </li>
            ))}
          </ ul>
        </ div>
      ):(
        <p>등록된 리뷰가 없어요...</p>
      )}
    </>
  )
}

export default MyReview
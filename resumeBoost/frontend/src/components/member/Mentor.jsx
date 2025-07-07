import React, { useEffect, useState, useRef } from "react"
import { useNavigate, useParams } from "react-router-dom"
import jwtAxios from "./../../util/jwtUtils"
import { useDispatch, useSelector } from "react-redux"
import { addItemCart } from "../../slice/cartSlice"
import { S3URL } from "../../util/constant"
import axios from "axios"
import { EC2_URL } from "../../constans"

const Mentor = () => {
  const { id: mentorId } = useParams()
  const loginState = useSelector((state) => state.loginSlice)
  const [mentor, setMentor] = useState({})
  const [items, setItems] = useState([])
  const [reviews, setReviews] = useState([])
  const [category, setCategory] = useState([])
  const [imgUrl, setImgUrl] = useState("/images/mentor.jpg")
  const [activeIndex, setActiveIndex] = useState(0)
  const sectionRefs = useRef([])
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림/닫힘 상태
  const [selectedReview, setSelectedReview] = useState(null); // 수정할 리뷰 정보
  const [updatedContent, setUpdatedContent] = useState(""); // 수정할 리뷰 내용

  const [detailsExpanded, setDetailsExpanded] = useState(false)

  useEffect(() => {
    const observerOptions = {
      root: null,
      rootMargin: "0px",
      threshold: 0.8,
    }

    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const index = sectionRefs.current.indexOf(entry.target)
          setActiveIndex(index)
        }
      })
    }, observerOptions)

    sectionRefs.current.forEach((section) => {
      if (section) observer.observe(section)
    })

    return () => {
      sectionRefs.current.forEach((section) => {
        if (section) observer.unobserve(section)
      })
    }
  }, [])

  const handleClick = (index) => {
    setActiveIndex(index)
    sectionRefs.current[index]?.scrollIntoView({
      behavior: "smooth",
      block: "center",
    })
  }

  const mentorAxiosFn = async (mentorId) => {
    try {
      const result = await jwtAxios.get(
        `http://${EC2_URL}:8090/member/mentorDetail/${mentorId}/${loginState.id}`
      )
      const mentorData = result.data.mentor
      setMentor(mentorData)
      setCategory(
        Array.from(
          new Set(result.data.mentor.itemEntities.map((item) => item.category))
        )
      )
      if (mentorData.attachFile === 1) {
        setImgUrl(`${S3URL}${mentorData.newImgName}`)
      }
    } catch (err) {
      console.log(err)
    }
  }

  const itemAxiosFn = async (mentorId) => {
    try {
      const result = await jwtAxios.get(
        `http://${EC2_URL}:8090/item/myItemList/${mentorId}`
      )
      
      setItems(result.data.itemList.content)
    } catch (error) {
      console.log(error)
    }
  }

  const addCartFn = async (item) => {
    console.log(item)
    if (!window.confirm("장바구니에 추가하시겠습니까?")) return
    try {
      const response = await jwtAxios.post(
        `http://${EC2_URL}:8090/cart/addCart/memberId/${loginState.id}/id/${item.id}`
      )
      dispatch(addItemCart(item))

      alert("장바구니에 추가되었습니다.")
    } catch (error) {
      console.log(error)
      alert("장바구니 추가 실패")
    }
  }

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
      }
    } catch (error) {
      console.error(error)
    }
  }
  
  useEffect(() => {
    mentorAxiosFn(mentorId)
    itemAxiosFn(mentorId)
    reviewFn(mentorId)
  }, [mentorId])

  function formatDate(dateString) {
    const date = new Date(dateString); // 날짜 문자열을 Date 객체로 변환
    const day = String(date.getDate()).padStart(2, '0'); // 날짜 (일자) 두 자리 숫자로
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (1부터 시작하므로 +1)
    const year = String(date.getFullYear()).slice(2); // 연도는 두 자리로
  
    return `${year}.${month}.${day}`; // 원하는 형식으로 리턴
  }

  const reviewDeleteFn = async (reviewId) =>{
    const bool = window.confirm("리뷰를 삭제 하시겠습니까? 삭제하시면 복구하지 못합니다.")
    if(bool === true){
      try {
        await jwtAxios.delete(`http://${EC2_URL}:8090/review/delete/${reviewId}`);
        reviewFn(mentorId)
      } catch (error) {
        console.error(error);
        alert("리뷰 삭제 실패")
      }
    }
    return
  }
  
  return (
    <div className='mentorDetail'>
      <div className='profileDiv' style={{ backgroundImage: `url(${imgUrl})` }}>
        <div className='profileImg'></div>
      </div>
      <div className='imgThum'>
        <img
          src={
            mentor.attachFile === 1
              ? `${S3URL}${mentor.newImgName}`
              : "/images/mentor.jpg"
          }
          alt='mentor'
        />
      </div>
      <div className='mentorDetail-con'>
        <div className='mentor-detail'>
          <h1>{mentor.nickName}</h1>
          <div>
            {category.map((e, index) => (
              <span key={index}>
                {e}
                {index !== category.length - 1 && " / "}
              </span>
            ))}
          </div>
          <span>경력 : {mentor.career}</span>

          {/* ✅ 클릭 시 activeIndex 변경 + 스크롤 이동 */}
          <div className='mentorInfo'>
            <ul className='info'>
              {[
                "멘토 정보",
                "멘토의 상품",
                "포트폴리오",
                "리뷰",
              ].map((text, index) => (
                <li
                  key={index}
                  className={activeIndex === index ? "active" : ""}
                  onClick={() => handleClick(index)}
                >
                  {text}
                </li>
              ))}
            </ul>

            {/* ✅ 해당 div만 보이도록 설정 + ref 연결 */}
            <div className='mentorDetails'>
              <div className="details-top"
              ref={(el) => (sectionRefs.current[0] = el)}>
                <h2>멘토 정보</h2>
                <ul>
                  <li>
                    <p>이름</p>
                    <span>{mentor.userName}</span>
                  </li>
                  <li>
                    <p>나이</p>
                    <span>{mentor.age}대</span>
                  </li>
                  <li>
                    <p>경력</p>
                    <span>{mentor.career}</span>
                  </li>
                </ul>
              </div>
              <div className="details-bottom">
                <h2>서비스 상세설명</h2>
                {mentor.detail === null?(
                  <span className="nopt">등록된 설명이 없어요...</span>
                ):(
                <>
                  <div>
                    <span dangerouslySetInnerHTML={{
                      __html: mentor.detail
                      ? detailsExpanded
                      ? mentor.detail
                      : mentor.detail.slice(0, 500)
                      : "로딩 중..."
                      }} />
                    {!detailsExpanded && <div className="details-show"></div>}
                  </div>
                  <button
                  className="detail-btn"
                  onClick={() => setDetailsExpanded(!detailsExpanded)}
                  >
                    {detailsExpanded ? "간략히 보기 △" : "상세설명 더보기 ▽"}
                  </button>
                </>
                )}
              </div>
            </div>

            <div
              ref={(el) => (sectionRefs.current[1] = el)}
              className='mentorItems'
            >
              <h2>멘토의 상품</h2>
              {items.length === 0?(
                <div className="nopt">등록된 상품이 없어요...</div>
              ):(
                <ul className='myItemList'>
                  {items.map((item) => (
                    <li key={item.id} onClick={() => addCartFn(item)}>
                      <div>
                        <p>
                          <span>카테고리 :</span> {item.category}
                        </p>
                        <p>
                          <span>상품가격 :</span> {item.itemPrice}원
                        </p>
                      </div>
                    </li>
                  ))}
                </ul>
              )}
            </div>

            <div
              ref={(el) => (sectionRefs.current[2] = el)}
              className='mentorPortfolios'
            >
              <h2>포트폴리오</h2>
              {mentor.newPtName === null ? (
                <span className="nopt">등록된 파일이 없어요... /(ㄒoㄒ)/~~</span>
              ):(
                <div>
                  <img src={`${S3URL}${mentor.newPtName}`} alt="ptimg" />
                </div>
              )}
            </div>

            {/* 리뷰 수정 모달 */}
            {isModalOpen && (
              <div className="modal-overlay">
                <div className="modal-content">
                  <h3>리뷰 수정</h3>
                  <textarea
                    value={updatedContent}
                    onChange={(e) => setUpdatedContent(e.target.value)}
                    rows="5"
                    placeholder="리뷰를 수정하세요..."
                  />
                  <div className="modal-actions">
                    <button
                      onClick={async () => {
                        if (updatedContent.trim()) {
                          // 리뷰 수정 API 호출
                          try {
                            const formattedContent = updatedContent.replace(/\n/g, '<br />'); // 엔터를 <br />로 변환
                            await jwtAxios.put(
                              `http://${EC2_URL}:8090/review/update/${selectedReview.id}`,
                              { content: formattedContent }
                            );
                            alert("리뷰가 수정되었습니다.");
                            setIsModalOpen(false); // 모달 닫기
                            reviewFn(mentorId); // 리뷰 새로 고침
                          } catch (err) {
                            console.error(err);
                            alert("리뷰 수정에 실패했습니다.");
                          }
                        } else {
                          alert("내용을 입력해주세요.");
                        }
                      }}
                    >
                      저장
                    </button>
                    <button onClick={() => setIsModalOpen(false)}>닫기</button>
                  </div>
                </div>
              </div>
            )}

            <div
              ref={(el) => (sectionRefs.current[3] = el)}
              className='mentorReview'
            >
              <h2>리뷰/후기</h2>
              <div className="review">
                <ul>
                {reviews.length === 0?(
                  <div className="nopt">등록된 리뷰가 없어요...</div>
                ):(
                  reviews.map((review) => (
                    <li>
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
                        {loginState.NickName === review.memberEntity.nickName?(
                          <div>
                            <span
                              onClick={() => {
                                setSelectedReview(review);
                                setUpdatedContent(review.content.replace(/<br\s*\/?>/g, '\n')); // 기존 내용으로 초기화
                                setIsModalOpen(true); // 모달 열기
                              }}
                            >
                              수정
                            </span>
                            <span onClick={()=>reviewDeleteFn(review.id)}>삭제</span>
                          </div>
                        ):(
                          <></>
                        )}
                      </div>
                    </li>
                  ))
                )}
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div className='mentor-pay'>
          <div className="mentor-text">
            {mentor.nickName} 님에게 원하는 서비스의 견적을 받아보세요
          </div>
          <div className='mentor-btn1'>상담 요청하기</div>
          <div
            className='mentor-btn2'
            onClick={() => navigate(`/cart/myCartList/${loginState.id}`)}
          >
            장바구니 보기
          </div>
        </div>
      </div>
    </div>
  )
}

export default Mentor

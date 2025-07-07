import axios from "axios"
import React, { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import { Link, useNavigate } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import { S3URL } from "../../util/constant"
import { EC2_URL } from "../../constans"

const Main = () => {
  const isLogin = useSelector((state) => state.loginSlice)

  const [reviews, setReviews] = useState([])

  useEffect(() => {
    const reviewFn = async () => {
      try {
        const url = `http://${EC2_URL}:8090/review/reviewList`
        const res = await axios.get(url)

        if (res.data && res.data.reviewList) {
          const updatedReplies = await Promise.all(
            res.data.reviewList.map(async (review) => {
              try {
                const memberRes = await axios.get(
                  `http://${EC2_URL}:8090/member/memberDetail/${review.memberId}`
                )

                return { ...review, memberEntity: memberRes.data.member } // 기존 reply에 상세 member 정보 추가
              } catch (error) {
                console.error("멤버 정보를 가져오는 중 오류 발생:", error)
                return review // 오류 발생 시 원래 데이터 유지
              }
            })
          )
          setReviews(updatedReplies)
        } else {
          setReviews([])
        }
      } catch (error) {
        console.error("데이터 불러오기 실패:", error)
      }
    }
    reviewFn()
  }, [])

  const navigate = useNavigate()

  const mentorDetailFn = (mentorId) => {
    // navigate(`/member/mentorDetail/${mentorId}`)
    navigate(`/member/mentorDetail/${mentorId}`)
  }

  return (
    <>
      <div className='main'>
        <div className='main-con'>
          <div className='slogan'>
            <img src='/images/main2.jpg' alt='슬로건' />
          </div>
          <div className='main-container'>
            <div className='main-section1'>
              <div className='section1-con'>
                <div className='section1-top'>
                  <h1>
                    멘토 • 취준생 회원수 <br /> 대한민국 1위
                  </h1>
                  <span>멘토 풀, 취준생 회원수 모두 압도적 1위</span>
                </div>
                <div className='section1-bottom'>
                  <ul>
                    <li>
                      <div className='bottom-top'>
                        <img src='/images/mentoricon.png' alt='main-icon' />
                      </div>
                      <div className='bottom-bottom'>
                        <span>멘토 회원</span>
                        <h1>592,313명</h1>
                      </div>
                    </li>
                    <li>
                      <div className='bottom-top'>
                        <img src='/images/membericon.png' alt='main-icon' />
                      </div>
                      <div className='bottom-bottom'>
                        <span>취준생 회원</span>
                        <h1>1,533,045명</h1>
                      </div>
                    </li>
                    <li>
                      <div className='bottom-top'>
                        <img src='/images/reviewicon.png' alt='main-icon' />
                      </div>
                      <div className='bottom-bottom'>
                        <span>고객 만족도</span>
                        <h1>97.2%</h1>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div className='main-section2'>
              <div className='section2-con'>
                <div className='section2-top'>
                  <h1>
                    153만 취준생 회원들이 <br />
                    직접 작성한 후기
                  </h1>
                  <span>
                    실제 컨설팅 후 작성된 100% 리얼 후기를 보고 <br />
                    원하는 멘토를 보다 쉽고, 빠르게 찾을 수 있어요.
                  </span>
                </div>
                <div className='section2-mid'>
                  <div className='review-list'>
                    {[...reviews, ...reviews].map((review, index) => (
                      <div
                        key={index}
                        className='review-item'
                        onClick={() => mentorDetailFn(review.mentorId)}
                      >
                        <div className='review-header'>
                          {review.memberEntity.attachFile == 1 ? (
                            <img
                              src={`${S3URL}${review.memberEntity.newImgName}`}
                              alt='프로필'
                              className='profile-img'
                            />
                          ) : (
                            <img
                              src='/images/profile.png'
                              alt='프로필 사진'
                              className='profile'
                            />
                          )}
                          <div className='review-info'>
                            <span className='review-author'>
                              {review.memberEntity.nickName}
                            </span>{" "}
                            <span className='line'>| </span>
                            <span className='review-location'>
                              {review.memberEntity.address}
                            </span>
                          </div>
                        </div>
                        <div className='review-mentor'>
                          <p>{review.mentorNickName}님의 리뷰</p>
                        </div>
                        <div className='review-content'>
                          {review.content.length > 250 ? (
                            <>
                              <span
                                dangerouslySetInnerHTML={{
                                  __html: `${review.content
                                    .slice(0, 250)
                                    .replace(/<br\s*\/?>/g, " ")} ...`,
                                }}
                              />
                              <div className='post-readmore'>멘토보기</div>
                            </>
                          ) : (
                            <>
                              <span
                                dangerouslySetInnerHTML={{
                                  __html: review.content.replace(
                                    /<br\s*\/?>/g,
                                    " "
                                  ),
                                }}
                              />
                              <div className='post-readmore'>멘토보기</div>
                            </>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
                <div className='section2-bottom'>
                  <span>
                    <img src='/images/!!!.png' alt='' /> 후기는 최신순으로
                    노출됩니다!
                  </span>
                </div>
              </div>
            </div>
            {isLogin.id ? (
              <div className='main-section3'>
                <h1 className='index-title-1'>RESUMEBOOST를 사용해 보세요!</h1>
                <div className='cards-container'>
                  {/* 왼쪽 카드 */}
                  <Link to={"/member/mentorList"} className='card card-green'>
                    <div className='card-content'>
                      <p className='card-subtitle-green'>
                        취업관련 컨설팅 멘토를 직접 찾아서 견적받고 싶다면?
                      </p>
                      <h2 className='card-title-green'>
                        1분만에 나에게 맞는 <br />
                        멘토 찾아보기
                      </h2>
                    </div>
                    <div className='card-footer'>
                      <div className='arrow-button-green'>
                        <span style={{ color: "#E0F8E0" }}>-</span>〉
                      </div>
                      <img
                        src='/images/index1.png'
                        alt='paper plane'
                        className='card-icon'
                      />
                    </div>
                  </Link>

                  {/* 오른쪽 카드 */}
                  <Link to={"/board"} className='card card-blue'>
                    <div className='card-content'>
                      <p className='card-subtitle-blue'>
                        전국 취준생들과 소통하는 커뮤니티
                      </p>
                      <h2 className='card-title-blue'>
                        RESUMEBOOST
                        <br /> 커뮤니티 둘러보기
                      </h2>
                    </div>
                    <div className='card-footer'>
                      <div className='arrow-button-blue'>
                        <span style={{ color: "#E0F0FF" }}>-</span>〉
                      </div>
                      <img
                        src='/images/index2.png'
                        alt='user folder'
                        className='card-icon'
                      />
                    </div>
                  </Link>
                </div>
              </div>
            ) : (
              <div className='main-section3'>
                <h1 className='index-title-1'>
                  이제, 1분만에 회원가입하고 <br /> 내게 딱 맞는 취업 멘토를
                  만나보세요!
                </h1>
                <div className='cards-container'>
                  {/* 왼쪽 카드 */}
                  <Link to={"/auth/join"} className='card card-green'>
                    <div className='card-content'>
                      <p className='card-subtitle-green'>
                        취업관련 컨설팅 멘토를 직접 찾아서 견적받고 싶다면?
                      </p>
                      <h2 className='card-title-green'>
                        1분만에 회원가입하고
                        <br />
                        나에게 맞는 멘토 찾아보기
                      </h2>
                    </div>
                    <div className='card-footer'>
                      <div className='arrow-button-green'>
                        <span style={{ color: "#E0F8E0" }}>-</span>〉
                      </div>
                      <img
                        src='/images/index1.png'
                        alt='paper plane'
                        className='card-icon'
                      />
                    </div>
                  </Link>

                  {/* 오른쪽 카드 */}
                  <Link to={"/auth/join/mentor"} className='card card-blue'>
                    <div className='card-content'>
                      <p className='card-subtitle-blue'>
                        취준 선배로써 or 경력자로써 부수입과 도움을 주고 싶다면?
                      </p>
                      <h2 className='card-title-blue'>
                        1분만에 멘토가입하고
                        <br />
                        컨설팅 해주기
                      </h2>
                    </div>
                    <div className='card-footer'>
                      <div className='arrow-button-blue'>
                        <span style={{ color: "#E0F0FF" }}>-</span>〉
                      </div>
                      <img
                        src='/images/index2.png'
                        alt='user folder'
                        className='card-icon'
                      />
                    </div>
                  </Link>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  )
}

export default Main

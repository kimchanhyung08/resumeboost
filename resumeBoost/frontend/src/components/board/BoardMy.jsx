import React, { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import { Link, useNavigate } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import { S3URL } from "../../util/constant"
import { EC2_URL } from "../../constans"

const BoardMy = () => {
  const isLogin = useSelector((state) => state.loginSlice)
  const [posts, setPosts] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [startPage, setStartPage] = useState(1)
  const [endPage, setEndPage] = useState(1)
  const [category, setCategory] = useState("myBoard")
  const [memberInfo, setMemberInfo] = useState({
    nickName: "",
    age: 0,
    address: "",
    myPostCount: 0,
    myReplyCount: 0,
    newImgName: "",
    attachFile: 0,
  })

  // 게시글 목록 조회 함수
  const fetchPosts = async (page = 0) => {
    try {
      let url = ""

      // 카테고리별 엔드포인트 설정
      switch (category) {
        case "myBoard":
          url = `http://${EC2_URL}:8090/board/boardList/my/${isLogin.id}`
          break
        case "myReply":
          url = `http://${EC2_URL}:8090/reply/replyList/my/${isLogin.id}`
          break
      }

      const response = await jwtAxios.get(url, {
        params: {
          page,
          size: 5,
          sort: "id,DESC",
        },
      })
      console.log(response.data)

      const { boardList, startPage: start, endPage: end } = response.data
      setPosts(boardList.content)
      setTotalPages(boardList.totalPages)
      setStartPage(start)
      setEndPage(end)
      setCurrentPage(page)
    } catch (error) {
      console.error("게시글 조회 중 오류 발생:", error)
    }
  }

  useEffect(() => {
    fetchPosts(currentPage)
  }, [category, currentPage])

  // 카테고리 변경 처리
  const handleCategoryChange = (newCategory) => {
    setCategory(newCategory)
    setCurrentPage(0)
    setPosts([]) // 기존 게시글 초기화
  }

  const fetchMemberInfo = async (id) => {
    try {
      const member = await jwtAxios.get(
        `http://${EC2_URL}:8090/member/memberDetail/${id}`
      )
      // console.log(member.data.member);
      setMemberInfo({
        nickName: member.data.member.nickName,
        age: member.data.member.age,
        address: member.data.member.address,
        myPostCount: member.data.member.myPostCount,
        myReplyCount: member.data.member.myReplyCount,
        newImgName: member.data.member.newImgName,
        attachFile: member.data.member.attachFile,
      })
    } catch (error) {
      console.error("멤버 정보 조회 중 오류 발생:", error)
    }
  }

  // 초기 데이터 로드
  useEffect(() => {
    if (isLogin === null) {
      return
    }
    fetchMemberInfo(isLogin.id)
  }, [])

  // LocalDateTime 포맷팅 함수
  const formatRelativeTime = (dateTimeStr) => {
    if (!dateTimeStr) return ""

    const date = new Date(dateTimeStr)
    if (isNaN(date.getTime())) {
      console.error("Invalid date time string:", dateTimeStr)
      return "Invalid Date"
    }

    const now = new Date()
    const diffInSeconds = Math.floor((now - date) / 1000)

    if (diffInSeconds < 60) {
      return "방금 전"
    }

    const diffInMinutes = Math.floor(diffInSeconds / 60)
    if (diffInMinutes < 60) {
      return `${diffInMinutes}분 전`
    }

    const diffInHours = Math.floor(diffInMinutes / 60)
    if (diffInHours < 24) {
      return `${diffInHours}시간 전`
    }

    const diffInDays = Math.floor(diffInHours / 24)
    if (diffInDays < 30) {
      return `${diffInDays}일 전`
    }

    const diffInMonths = Math.floor(diffInDays / 30)
    if (diffInMonths < 12) {
      return `${diffInMonths}개월 전`
    }

    const diffInYears = Math.floor(diffInMonths / 12)
    return `${diffInYears}년 전`
  }

  const navigate = useNavigate()

  const boardDetailFn = (id) => {
    if (isLogin === null) {
      window.confirm("로그인하세요")
    } else {
      navigate(`/board/detail/${id}`)
    }
  }

  return (
    <div className='board-container'>
      <div className='top'>
        <div className='top-con'>
          <Link to='/board'>자유 게시판</Link>
          <Link to='/board/my'>내 활동</Link>
        </div>
      </div>

      <div className='container'>
        {/* 왼쪽 프로필 영역 */}
        <div className='left'>
          {!isLogin.id ? (
            <div className='left-noLogin'>
              <div className='left-login'>
                <Link to={"/auth/login"}>로그인</Link>
              </div>
              <div className='left-join'>
                <Link to={"/auth/join"}>회원가입</Link>
              </div>
            </div>
          ):(
            <div className="left-con">
              {memberInfo.attachFile==1?(
                <img
                  src={`${S3URL}${memberInfo.newImgName}`}
                  alt="프로필 사진"
                  className="profile"
                  />
                ):( 
                  <img
                  src="/images/profile.png"
                  alt="프로필 사진"
                  className="profile"
                  />
                )}
              <h2 className="nickName">{memberInfo.nickName}님</h2>
              <div className="age-address">
                <div className="age">{memberInfo.age}대</div>
                <span className="vertical1"></span>
                <div className="address">{memberInfo.address}</div>
              </div>

              <div className='count'>
                <div className='first'>
                  <div>작성글</div>
                  <div>{memberInfo.myPostCount}</div>
                </div>
                <span className='vertical'></span>
                <div>
                  <div>댓글</div>
                  <div>{memberInfo.myReplyCount}</div>
                </div>
              </div>

              <div className='button'>
                <button>
                  <Link to='/board/write'>게시글 작성</Link>
                </button>
              </div>
            </div>
          )}
        </div>
        <div className='right'>
          <div className='right-top'>
            <div className='right-top-con'>
              <h1>내 활동</h1>
              <div
                onClick={() => handleCategoryChange("myBoard")}
                className={category === "myBoard" ? "active" : ""}
              >
                작성 글
              </div>
              <div
                onClick={() => handleCategoryChange("myReply")}
                className={category === "myReply" ? "active" : ""}
              >
                댓글
              </div>
            </div>
          </div>
          {category === "myBoard" ? (
            <div className='post-list'>
              {posts.length > 0 ? (
                posts.map((post) => (
                  <div
                    key={post.id}
                    className='post'
                    onClick={() => boardDetailFn(post.id)}
                  >
                    <div className='post-top'>
                      <div className='post-left'>
                        <div className='post-header'>
                          <span className='post-category'>{post.category}</span>
                        </div>
                        <div className='post-title'>{post.title}</div>
                        <div className='post-content'>
                          {post.content.length > 100 ? (
                            <span
                              dangerouslySetInnerHTML={{
                                __html: `${post.content
                                  .slice(0, 100)
                                  .replace(/<br\s*\/?>/g, " ")}....`,
                              }}
                            />
                          ) : (
                            <span
                              dangerouslySetInnerHTML={{
                                __html: post.content.replace(
                                  /<br\s*\/?>/g,
                                  " "
                                ),
                              }}
                            />
                          )}
                        </div>
                        {post.content.length > 100 && (
                          <div className='post-readmore'>전체보기</div>
                        )}
                      </div>
                      <div className='post-right'>
                        {post.attachFile === 1 && (
                          <img
                            src={`${S3URL}${post.newImgName}`}
                            alt='첨부 이미지'
                            onError={(e) => {
                              e.target.style.display = "none"
                            }}
                          />
                        )}
                      </div>
                    </div>
                    <div className='post-footer'>
                      <div className='post-footer-left'>
                        <span className='post-nickName'>
                          {post.memberEntity?.nickName}
                        </span>
                        <span className='post-time'>
                          {formatRelativeTime(post.createTime)}
                        </span>
                      </div>
                      <div className='post-footer-right'>
                        <span>조회 {post.viewCount}</span> |{" "}
                        <span>💬 {post.replyCount}</span>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <p className='no-board'>게시글이 없습니다.</p>
              )}
            </div>
          ) : (
            <div className='post-list'>
              {posts.length > 0 ? (
                posts.map((post) => (
                  <div
                    key={post.id}
                    className='post'
                    onClick={() => boardDetailFn(post.boardEntity.id)}
                  >
                    <div className='post-top'>
                      <div className='post-left'>
                        <div className='post-header'>
                          <span className='post-category'>
                            {post.boardEntity.category}
                          </span>
                        </div>
                        <div className='post-title'>댓글</div>
                        <div className='post-content'>
                          {post.content.length > 100
                            ? `${post.content.slice(0, 100)}....`
                            : post.content}
                        </div>
                        {post.content.length > 100 && (
                          <div className='post-readmore'>전체보기</div>
                        )}
                      </div>
                    </div>
                    <div className='post-footer'>
                      <div className='post-footer-left'>
                        <span className='post-nickName'>
                          {post.memberEntity?.nickName}
                        </span>
                        <span className='post-time'>
                          {formatRelativeTime(post.createTime)}
                        </span>
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <p className='no-board'>댓글이 없습니다.</p>
              )}
            </div>
          )}
          {posts.length > 0 ? (
            <>
              {/* 페이지네이션 */}
              <div className='pagination'>
                {/* 이전(왼쪽) 버튼 */}
                <button
                  onClick={() => setCurrentPage(currentPage - 1)}
                  disabled={currentPage === 0} // 첫 페이지일 때 비활성화
                >
                  &lt;
                </button>

                {/* 페이지 번호 버튼 */}
                {Array.from(
                  { length: endPage - startPage + 1 },
                  (_, i) => startPage + i
                ).map((page) => (
                  <button
                    key={page}
                    onClick={() => setCurrentPage(page - 1)}
                    className={currentPage === page - 1 ? "active" : ""}
                  >
                    {page}
                  </button>
                ))}

                {/* 다음(오른쪽) 버튼 */}
                <button
                  onClick={() => setCurrentPage(currentPage + 1)}
                  disabled={currentPage === totalPages - 1} // 마지막 페이지일 때 비활성화
                >
                  &gt;
                </button>
              </div>
            </>
          ) : (
            <></>
          )}
        </div>
      </div>
    </div>
  )
}

export default BoardMy

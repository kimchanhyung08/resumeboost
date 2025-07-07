import React, { useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import axios from "axios"
import { useSelector } from "react-redux"
import { S3URL } from "../../util/constant"
import { EC2_URL } from "../../constans"

const BoardDetail = (param) => {
  const isLogin = useSelector((state) => state.loginSlice)
  const navigate = useNavigate()

  const [content, setContent] = useState("")
  const [errorMessage, setErrorMessage] = useState("")
  const [replies, setReplies] = useState([]) // 빈 배열로 초기화
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [isLoading, setIsLoading] = useState(true)

  // 로컬 스토리지에서 role 정보를 가져옴
  const [role, setRole] = useState(localStorage.getItem("userRole") || null)

  // 로그인 상태가 변경될 때 로컬 스토리지에 role 정보 저장
  useEffect(() => {
    if (isLogin.role) {
      localStorage.setItem("userRole", isLogin.role)
      setRole(isLogin.role)
    } else {
      localStorage.removeItem("userRole")
      setRole(null)
    }
  }, [isLogin.role])

  const [boardDetail, setBoardDetail] = useState({
    id: 0,
    attachFile: 0,
    category: "",
    content: "",
    createTime: "",
    memberEntity: {
      nickName: "",
      age: 0,
      address: "",
      attachFile: 0,
      newImgName: "",
      id: null,
    },
    newImgName: "",
    replyCount: 0,
    replyEntities: [],
    title: "",
    viewCount: 0,
  })

  useEffect(() => {
    const detailFn = async () => {
      const boardId = param.param.id
      const url = `http://${EC2_URL}:8090/board/detail/${boardId}`
      try {
        const board = await jwtAxios.get(url)

        const memberRes = await jwtAxios.get(
          `http://${EC2_URL}:8090/member/memberDetail/${board.data.boardDetail.memberEntity.id}`
        )

        setBoardDetail({
          id: board.data.boardDetail.id,
          attachFile: board.data.boardDetail.attachFile,
          memberEntity: memberRes.data.member,
          category: board.data.boardDetail.category,
          content: board.data.boardDetail.content,
          title: board.data.boardDetail.title,
          viewCount: board.data.boardDetail.viewCount,
          createTime: board.data.boardDetail.createTime,
          newImgName: board.data.boardDetail.newImgName,
          replyEntities: board.data.boardDetail.replyEntities,
          replyCount: board.data.boardDetail.replyCount,
        })
      } catch (error) {
        console.log(error)
      }
    }
    detailFn()
  }, [param.param.id])

  useEffect(() => {
    fetchReplies()
  }, [currentPage, param.param.id])

  const fetchReplies = async () => {
    setIsLoading(true)
    const boardId = param.param.id
    const url = `http://${EC2_URL}:8090/reply/replyList/${boardId}?page=${currentPage}&size=6&sort=id,desc`
    try {
      const response = await jwtAxios.get(url)

      if (response.data && response.data.content) {
        const updatedReplies = await Promise.all(
          response.data.content.map(async (reply) => {
            try {
              const memberRes = await jwtAxios.get(
                `http://${EC2_URL}:8090/member/memberDetail/${reply.memberEntity.id}`
              )
              return { ...reply, memberEntity: memberRes.data.member } // 기존 reply에 상세 member 정보 추가
            } catch (error) {
              console.error("멤버 정보를 가져오는 중 오류 발생:", error)
              return reply // 오류 발생 시 원래 데이터 유지
            }
          })
        )
        setReplies(updatedReplies)
        setTotalPages(response.data.totalPages)
      } else {
        setReplies([])
        setTotalPages(0)
      }
    } catch (error) {
      console.log(error)
      setReplies([])
      setTotalPages(0)
    } finally {
      setIsLoading(false)
    }
  }

  const handleContentChange = (e) => {
    const value = e.target.value
    setContent(value)
    if (value.length <= 300) {
      setErrorMessage("")
    } else {
      setErrorMessage("내용은 300자를 넘길 수 없습니다.")
    }
  }

  const handleReplyCountChangePl = () => {
    setBoardDetail((prev) => ({ ...prev, replyCount: prev.replyCount + 1 }))
    // const replyCount = document.querySelector(".replyCount")
    // replyCount.innerText = `💬 ${boardDetail.replyCount +1}`
  }

  const handleSubmitReply = async () => {
    if (!content.trim()) {
      alert("댓글 내용을 입력해주세요.")
      return
    }

    try {
      await jwtAxios.post(`http://${EC2_URL}:8090/reply/insert`, {
        memberId: isLogin.id,
        boardId: param.param.id,
        content: content,
      })
      setContent("")
      fetchReplies()
      handleReplyCountChangePl()
    } catch (error) {
      console.log(error)
      alert("댓글 등록에 실패했습니다.")
    }
  }

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

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber)
  }

  const handleReplyCountChange = () => {
    setBoardDetail((prev) => ({ ...prev, replyCount: prev.replyCount - 1 }))
    // const replyCount = document.querySelector(".replyCount")
    // replyCount.innerText = `💬 ${boardDetail.replyCount -1}`
  }

  const deleteReplyFn = async (id) => {
    const bool = window.confirm("댓글 삭제 하심? 복구 못함")
    if (bool === true) {
      try {
        await jwtAxios.delete(`http://${EC2_URL}:8090/reply/delete/${id}`)
        fetchReplies()
        handleReplyCountChange()
      } catch (error) {
        console.log(error)
        alert("댓글 삭제 실패했습니다.")
      }
    }
    return
  }

  const deleteBoardFn = async (id) => {
    const bool = window.confirm("게시글 삭제 하심? 복구 못함")
    if (bool === true) {
      try {
        await jwtAxios.delete(`http://${EC2_URL}:8090/board/delete/${id}`)
        navigate("/board")
      } catch (error) {
        console.log(error)
        alert("게시글 삭제 실패했습니다.")
      }
    }
    return
  }

  let isOwnerOrAdmin =
    isLogin.userEmail === boardDetail.memberEntity.userEmail ||
    role === "ROLE_ADMIN"
  return (
    <div className='board-container'>
      <div className='top'>
        <div className='top-con'>
          <Link to='/board'>자유 게시판</Link>
          {isLogin.id && <Link to='/board/my'>내 활동</Link>}
        </div>
      </div>

      <div className='detail-container'>
        <div className='back' onClick={() => navigate("/board")}>
          <div className='back-con'>
            <img src='https://kimstudy.com/img/ic24/back.svg' alt='back' />
            <div>목록</div>
          </div>
        </div>
        <div className='detail-top'>
          <div className='detail-profile'>
            <div className='detail-profile-left'>
              {boardDetail.memberEntity.attachFile === 1 ? (
                <img
                  src={`${S3URL}${boardDetail.memberEntity.newImgName}`}
                  alt='프로필 사진'
                  className='detail-profile-img'
                />
              ) : (
                <img
                  src='/images/profile.png'
                  alt='프로필 사진'
                  className='detail-profile-img'
                />
              )}
            </div>
            <div className='detail-profile-right'>
              <div className='name'>{boardDetail.memberEntity.nickName}</div>
              <div className='detail-profile-bottom'>
                <div className='detail-bottom'>
                  <span>{boardDetail.memberEntity.age}대</span>
                  <span>{boardDetail.memberEntity.address}</span>
                </div>
                <div className='detail-bottom-right'>
                  {isLogin.userEmail === boardDetail.memberEntity.userEmail && (
                    <>
                      {/* 수정 버튼: 본인만 보이게 */}
                      <span
                        onClick={() =>
                          navigate(`/board/update/${boardDetail.id}`, {
                            state: { boardDetail },
                          })
                        }
                      >
                        수정
                      </span>
                    </>
                  )}
                  {/* 삭제 버튼: 본인 또는 ADMIN 역할을 가진 사람만 보이게 */}
                  {isOwnerOrAdmin && (
                    <span onClick={() => deleteBoardFn(boardDetail.id)}>
                      삭제
                    </span>
                  )}
                </div>
              </div>
            </div>
          </div>
          <div className='detail-main'>
            <div className='detail-title'>
              <h1>{boardDetail.title}</h1>
            </div>
            <div className='detail-content'>
              <span
                dangerouslySetInnerHTML={{
                  __html: boardDetail.content,
                }}
              />
            </div>
            {boardDetail.attachFile === 1 && (
              <div className='detail-board-img'>
                <img
                  src={`${S3URL}${boardDetail.newImgName}`}
                  alt='첨부 이미지'
                  onError={(e) => {
                    e.target.style.display = "none"
                  }}
                />
              </div>
            )}
            <div className='detail-main-footer'>
              <div className='detail-main-footer-left'>
                <span className='detail-time'>
                  {formatRelativeTime(boardDetail.createTime)}
                </span>
              </div>
              <div className='detail-main-footer-right'>
                <span>조회 {boardDetail.viewCount}</span>
                <span className='replyCount'>💬 {boardDetail.replyCount}</span>
              </div>
            </div>
          </div>

          <div className='detail-footer'>
            <div className='detail-reply'>
              <div className='detail-reply-header'>
                <div>댓글 입력</div>
                <textarea
                  id='reply-content'
                  value={content}
                  onChange={handleContentChange}
                  required
                  placeholder='댓글을 입력해 주세요'
                />
                <div className='reply-button'>
                  {errorMessage && (
                    <div className='error-message'>{errorMessage}</div>
                  )}
                  <div className='reply-button-right'>
                    <button
                      type='button'
                      onClick={handleSubmitReply}
                      disabled={!content.trim() || content.length > 300}
                    >
                      등록
                    </button>
                  </div>
                </div>
              </div>

              <div className='detail-reply-list'>
                {isLoading ? (
                  <div className='loading'>댓글을 불러오는 중...</div>
                ) : replies.length > 0 ? (
                  replies.map((reply) => (
                    <div key={reply.id} className='reply-item'>
                      <div className='reply-profile'>
                        {reply.memberEntity?.attachFile === 1 ? (
                          <img
                            src={`${S3URL}${reply.memberEntity.newImgName}`}
                            alt='프로필 사진'
                            className='reply-profile-img'
                          />
                        ) : (
                          <img
                            src='/images/profile.png'
                            alt='프로필 사진'
                            className='reply-profile-img'
                          />
                        )}
                        <div className='reply-info'>
                          <div className='reply-author'>
                            {reply.memberEntity?.nickName}
                          </div>
                          <div className='reply-metadata'>
                            <span>{reply.memberEntity?.age}대</span>
                            <span>{reply.memberEntity?.address}</span>
                            <span>{formatRelativeTime(reply.createTime)}</span>
                          </div>
                        </div>
                      </div>
                      <div className='reply-bottom'>
                        <div className='reply-content'>{reply.content}</div>
                        {isLogin.userEmail === reply.memberEntity.userEmail ||
                        role === "ROLE_ADMIN" ? (
                          <div
                            className='reply-delete'
                            onClick={() => deleteReplyFn(reply.id)}
                          >
                            삭제
                          </div>
                        ) : (
                          <></>
                        )}
                      </div>
                    </div>
                  ))
                ) : (
                  <div className='no-replies'>작성된 댓글이 없습니다.</div>
                )}

                {totalPages > 1 && (
                  <div className='pagination'>
                    {Array.from({ length: totalPages }, (_, i) => (
                      <button
                        key={i}
                        onClick={() => handlePageChange(i)}
                        className={currentPage === i ? "active" : ""}
                      >
                        {i + 1}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default BoardDetail

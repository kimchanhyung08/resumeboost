import React from "react"
import { useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"

const MyBoard = ({ boardList, setCategory, category }) => {
  const isLogin = useSelector((state) => state.loginSlice)
  const navigate = useNavigate()
  const boardDetailFn = (id) => {
    if (isLogin === null) {
      window.confirm("로그인하세요")
    } else {
      navigate(`/board/detail/${id}`)
    }
  }
  return (
    <div className='myBoardCon'>
      <div className='myBoardHead'>
        {boardList.length > 0 ? (
          <ul>
            <li
              className={category === "myBoard" ? "active" : ""}
              onClick={() => setCategory("myBoard")}
            >
              게시글
            </li>
            <li
              className={category === "myReply" ? "active" : ""}
              onClick={() => setCategory("myReply")}
            >
              덧글
            </li>
          </ul>
        ) : (
          <></>
        )}
      </div>
      <div className='myBoardList'>
        {boardList.length > 0 ? (
          <ul className='myItemList'>
            {boardList.map((board) => (
              <li
                key={board.id}
                onClick={() => {
                  category === "myBoard"
                    ? boardDetailFn(board.id)
                    : boardDetailFn(board.boardEntity.id)
                }}
              >
                {board.title ? (
                  <p>
                    <span>제목: </span> {board.title}
                  </p>
                ) : (
                  <p>
                    <span>게시글 제목: </span>
                    {board.boardEntity.title}
                  </p>
                )}
                {category === "myBoard" ? (
                  <p>
                    <span>내용: </span> 
                    <span
                      dangerouslySetInnerHTML={{
                        __html: board.content.replace(/<br\s*\/?>/g, " "),
                      }}
                    />
                  </p>
                ) : (
                  <p>
                    <span>덧글 내용: </span> {board.content}
                  </p>
                )}
              </li>
            ))}
          </ul>
        ) : (
          <p className='noItemText'>등록한 게시글, 덧글이 없습니다.</p>
        )}
      </div>
    </div>
  )
}

export default MyBoard

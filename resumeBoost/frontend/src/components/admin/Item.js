import React, { useEffect, useState } from "react"
import { getCookie } from "../../util/cookieUtil"
import jwtAxios from "../../util/jwtUtils"
import ItemInsert from "./modal/ItemInsert"
import PagingItem from "./paging/PagingItem"
import { useNavigate } from "react-router-dom"
import { EC2_URL } from "../../constans"

const Item = () => {
  const navigate = useNavigate()

  const [loginId, setLoginId] = useState("")

  const [pageData, setPageData] = useState({}) // 멘토 데이터

  const itemListFn = async () => {
    // 멘토 데이터 get

    const url = `http://${EC2_URL}:8090/admin/member/mentorList`

    const res = await jwtAxios.get(url)

    const data = res.data.mentor // 받은 모든 데이터
    // // 페이징 메서드
    const currentPage = data.number
    const totalPages = data.totalPages
    const blockNum = 3

    const mentorList = data.content

    const startPage =
      Math.floor(currentPage / blockNum) * blockNum + 1 <= totalPages
        ? Math.floor(currentPage / blockNum) * blockNum + 1
        : totalPages
    const endPage =
      startPage + blockNum - 1 < totalPages
        ? startPage + blockNum - 1
        : totalPages

    setPageData({
      startPage: startPage,
      endPage: endPage,
      mentorList: mentorList,
      currentPage: data.number,
      totalPages: totalPages,
    })
  }

  useEffect(() => {
    // const cookie = getCookie("member");
    // const id = cookie.id; // cookie 에 담긴 로그인한 사람 ID
    // setLoginId(id)

    itemListFn()
  }, [])

  // input: defaultValue 에 값 넣으면 set함수 안 먹음 // 값 안 바뀜
  // const detailValue = await detailItem(itemId); // async 쓸 경우 -> await 안 적으면 빈 값 옴

  const moveToItemDetail = (mentorId) => {
    navigate(`/admin/item/detail/${mentorId}`)
  }

  return (
    <>
      <div className='admin-item'>
        <div className='admin-item-con'>
          <h1>Item Page</h1>

          {/* 상품 등록 컴포넌트*/}
          {/* <ItemInsert loginId ={loginId} itemListFn={itemListFn}/>  */}

          <div className='admin-item-list'>
            {pageData.mentorList &&
              Object.values(pageData.mentorList).map((el, idx) => {
                return (
                  <div
                    className='item-pocket'
                    key={idx}
                    onClick={() => {
                      moveToItemDetail(el.id)
                    }}
                  >
                    <div>
                      {el.attachFile ? (
                        <div className='mentor-img'>
                          <img
                            src={`http://${EC2_URL}:8090/member/profile/${el.newImgName}`}
                            alt='image'
                          ></img>
                        </div>
                      ) : (
                        <div className='mentor-img'>
                          <img
                            src={`https://place-hold.it/100x100/666/fff/000?text= no Image`}
                          ></img>
                        </div>
                      )}
                    </div>
                    <ul>
                      <li>
                        <h3>닉네임 {el.nickName}</h3>
                      </li>
                      <li>멘토ID {el.id}</li>
                      <li>이름 {el.userName}</li>
                      <li>경력 {el.career}</li>
                    </ul>
                  </div>
                )
              })}
          </div>

          <div className='paging'>
            <PagingItem
              pageData={pageData}
              startPage={pageData.startPage}
              endPage={pageData.endPage}
              currentPage={pageData.currentPage}
              totalPages={pageData.totalPages}
              setPageData={setPageData}
            />
          </div>
        </div>
      </div>
    </>
  )
}

export default Item

import React, { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import { EC2_URL } from "../../constans"

const CartDetail = ({ param }) => {
  const navigate = useNavigate()

  const [pageData, setPageData] = useState({}) // 상품리스트 데이터

  const itemsListFn = async () => {
    const url = `http://${EC2_URL}:8090/admin/cart/itemsList/${param}`
    const res = await jwtAxios.get(url)
    const data = res.data.itemsList // 받은 모든 데이터

    // 페이징 메서드
    const currentPage = data.number
    const totalPages = data.totalPages
    const blockNum = 3

    const itemsList = data.content

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
      itemsList: itemsList,
      currentPage: data.number,
      totalPages: totalPages,
    })
  }

  useEffect(() => {
    itemsListFn()
  }, [])

  return (
    <>
      <div className='admin-cart-detail'>
        <div className='admin-cart-detail-con'>
          <h1>CartDetail Page</h1>

          <span
            className='admin-cart-detail-back'
            onClick={() => {
              navigate(-1)
            }}
          >
            뒤로가기
          </span>

          <div className='admin-itemsList-list'>
            {pageData.itemsList &&
              Object.values(pageData.itemsList).map((el, idx) => {
                return (
                  <div className='item-pocket' key={idx}>
                    <ul>
                      <li>{el.itemEntity.createTime}</li>
                      <li>상품리스트ID {el.id}</li>
                      <li>상품ID {el.itemEntity.id}</li>
                      <li>장바구니ID {el.cartEntity.id}</li>
                    </ul>
                  </div>
                )
              })}
          </div>

          {/* <div className="paging">
            <PagingCart startPage={pageData.startPage} endPage={pageData.endPage} currentPage={pageData.currentPage} totalPages={pageData.totalPages} setPageData={setPageData}/>
          </div> */}
        </div>
      </div>
    </>
  )
}

export default CartDetail

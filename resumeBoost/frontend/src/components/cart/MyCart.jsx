import React, { useEffect, useRef, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import "../../css/cart/mycart.css"
import { addItemCart, clearCart, removeItemCart } from "../../slice/cartSlice"
import { useDispatch, useSelector } from "react-redux"
import { S3URL } from "../../util/constant"
import { EC2_URL } from "../../constans"

const MyCart = () => {
  const param = useParams()
  const id = param.id
  // const [myItems, setMyItems] = useState([])
  const [memberInfo, setMemberInfo] = useState([])
  const [cartId, setCartId] = useState("")
  const intervalRef = useRef(null)
  const dispatch = useDispatch()
  const items = useSelector((state) => state.cartSlice.items)
  const navigate = useNavigate()

  const fetchMemberInfo = async (memberIds) => {
    try {
      const memberData = {}
      await Promise.all(
        memberIds.map(async (memberId) => {
          const res = await jwtAxios.get(
            `http://${EC2_URL}:8090/member/myDetail/${memberId}`
          )
          memberData[memberId] = res.data.member // 각 멤버 정보를 객체에 저장
        })
      )
      setMemberInfo(memberData) // 한 번에 상태 업데이트
    } catch (error) {
      console.log("멤버 정보 가져오기 실패:", error)
    }
  }

  const myCartItemsFn = async () => {
    try {
      const res = await jwtAxios.get(`http://${EC2_URL}:8090/cart/myCart/${id}`)
      const cartitems = res.data.cart.itemListEntities
      cartitems.forEach((item) => {
        dispatch(addItemCart(item.itemEntity))
      })

      const memberIds = [
        ...new Set(cartitems.map((item) => item.itemEntity.memberEntity.id)),
      ] // 중복 제거
      fetchMemberInfo(memberIds)
      setCartId(res.data.cart.id)
    } catch (error) {
      console.log(error)
    }
  }

  const deleteCartItemFn = async (itemId) => {
    if (!window.confirm("상품을 삭제하시겠습니까?")) return

    try {
      await jwtAxios.get(
        `http://${EC2_URL}:8090/cart/deleteCartItem/memberId/${id}/itemId/${itemId}`
      )

      // setMyItems((prevItems) =>
      //   prevItems.filter((item) => item.itemEntity.id !== itemId)
      // )
      dispatch(removeItemCart(itemId))
    } catch (error) {
      console.log(error)
      alert("삭제 실패")
    }
  }

  const deleteAllCartItemFn = async () => {
    if (!window.confirm("장바구니의 모든 상품을 삭제하시겠습니까?")) return

    try {
      const res = await jwtAxios.get(
        `http://${EC2_URL}:8090/cart/deleteAllCartItems/${id}`
      )

      // 모든 아이템 삭제 후 상태 갱신
      // setMyItems([])
      dispatch(clearCart())
    } catch (error) {
      console.log(error)
      alert("전체 삭제 실패")
    }
  }

  const goToPayFn = () => {
    if (!window.confirm("결제하시겠습니까?")) return
    navigate(`/pay/addPay/${id}?cartId=${cartId}`)
  }

  useEffect(() => {
    myCartItemsFn() // 초기 데이터 가져오기

    const handleFocus = () => {
      myCartItemsFn() // 페이지가 포커스를 받을 때 데이터 새로고침
    }

    const startInterval = () => {
      intervalRef.current = setInterval(() => {
        myCartItemsFn() // 10초마다 데이터 새로고침
      }, 30000)
    }

    window.addEventListener("focus", handleFocus) // 페이지 포커스 시 새로고침
    startInterval() // interval 시작

    return () => {
      window.removeEventListener("focus", handleFocus) // focus 이벤트 제거
      if (intervalRef.current) {
        clearInterval(intervalRef.current) // 컴포넌트 언마운트 시 interval 제거
      }
    }
  }, [id, dispatch])

  return (
    <div className='cart-container'>
      {items.length > 0 ? (
        <h1 className='cart-title'>장바구니 목록</h1>
      ) : (
        <h1 className='cart-title'>장바구니가 비었습니다.</h1>
      )}
      <div className='cart-list'>
        {items.length > 0 ? (
          items.map((item) => (
            <div key={item.id} className='cart-item'>
              <div className='cart-item-details'>
                <img
                  src={
                    memberInfo[item.memberEntity.id]?.newImgName
                      ? `${S3URL}${memberInfo[item.memberEntity.id].newImgName}`
                      : "/images/profile.png"
                  }
                  alt='item'
                  className='cart-item-image'
                />
                <div className='cart-item-info'>
                  <span>{item.id}</span>
                  <span>{item.category}</span>
                  <span className='cart-item-price'>{item.itemPrice} 원</span>
                  <span>{item.memberEntity.nickName}</span>
                </div>
              </div>
              <button
                className='cart-remove-btn'
                onClick={() => deleteCartItemFn(item.id)}
              >
                X
              </button>
            </div>
          ))
        ) : (
          <></>
        )}
      </div>

      {/* 주문 요약 */}
      {items.length > 0 && (
        <div className='cart-summary'>
          <h2 className='cart-summary-title'>주문 정보</h2>
          <div className='cart-summary-details'>
            <span>총 수량: {items.length} 개</span>
            <span>
              총 상품 금액:{" "}
              {items.reduce((total, item) => total + item.itemPrice, 0)} 원
            </span>
            <span>
              수수료:{" "}
              {Math.floor(
                items.reduce((total, item) => total + item.itemPrice, 0) * 0.05
              )}{" "}
              원
            </span>
            <span>
              총 주문금액:{" "}
              {Math.floor(
                items.reduce((total, item) => total + item.itemPrice, 0) * 1.05
              )}{" "}
              원
            </span>
          </div>
          <div className='cart-actions'>
            <button className='cart-order-btn' onClick={() => goToPayFn()}>
              결제하기
            </button>
            <button
              className='cart-delete-btn'
              onClick={() => deleteAllCartItemFn()}
            >
              전체삭제
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default MyCart

import React, { useEffect, useState } from "react"
import { Link, useParams } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import "../../css/pay/myPay.css"
import { useSelector } from "react-redux"
import { S3URL } from "../../util/constant"
import { EC2_URL } from "../../constans"

const Mypay = () => {
  const param = useParams()
  const id = param.id
  const isLogin = useSelector((state) => state.loginSlice)
  const [memberInfo, setMemberInfo] = useState({})
  const [myPayList, setMyPayList] = useState([])
  const [filteredPayList, setFilteredPayList] = useState([]) // 필터링된 리스트

  // 필터링 & 정렬 상태 관리
  const [filterPeriod, setFilterPeriod] = useState("all") // 기간 필터
  const [filterPayment, setFilterPayment] = useState("all") // 결제수단 필터
  const [sortOrder, setSortOrder] = useState("newest") // 정렬 (newest: 최신순, oldest: 오래된순)

  // 결제 내역 조회
  const myPayFn = async (id) => {
    try {
      const res = await jwtAxios.get(`http://${EC2_URL}:8090/pay/myPay/${id}`)
      setMyPayList(res.data.payList)
    } catch (error) {
      console.log(error)
    }
  }

  // 회원 정보 조회
  const memberFn = async (id) => {
    try {
      const res = await jwtAxios.get(
        `http://${EC2_URL}:8090/member/memberDetail/${id}`
      )
      setMemberInfo(res.data.member)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    myPayFn(id)
    memberFn(id)
  }, [id])

  // 📌 필터링 & 정렬 함수
  useEffect(() => {
    let filteredList = [...myPayList]

    // 1️⃣ 기간 필터 적용
    if (filterPeriod !== "all") {
      const now = new Date()
      const filteredDate = new Date()
      if (filterPeriod === "1month") filteredDate.setMonth(now.getMonth() - 1)
      if (filterPeriod === "3months") filteredDate.setMonth(now.getMonth() - 3)
      if (filterPeriod === "6months") filteredDate.setMonth(now.getMonth() - 6)
      if (filterPeriod === "1year")
        filteredDate.setFullYear(now.getFullYear() - 1)

      filteredList = filteredList.filter(
        (pay) => new Date(pay.createTime) >= filteredDate
      )
    }

    // 2️⃣ 결제 수단 필터 적용
    if (filterPayment !== "all") {
      filteredList = filteredList.filter(
        (pay) => pay.paymentType === filterPayment
      )
    }

    // 3️⃣ 정렬 적용
    if (sortOrder === "newest") {
      filteredList.sort(
        (a, b) => new Date(b.createTime) - new Date(a.createTime)
      )
    } else {
      filteredList.sort(
        (a, b) => new Date(a.createTime) - new Date(b.createTime)
      )
    }

    setFilteredPayList(filteredList)
  }, [myPayList, filterPeriod, filterPayment, sortOrder])

  return (
    <div className='myPay'>
      <div className='myPay-con'>
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
          ) : (
            <div className='left-con'>
              {memberInfo.attachFile === 1 ? (
                <div className='profile'>
                  <img
                    src={`${S3URL}${memberInfo.newImgName}`}
                    alt='프로필 사진'
                  />
                </div>
              ) : (
                <div className='profile'>
                  <img src='/images/profile.png' alt='프로필 사진' />
                </div>
              )}
              <h2 className='nickName'>{memberInfo.nickName}님</h2>
              <div className='age-address'>
                <div className='age'>{memberInfo.age}대</div>
                <span className='vertical1'></span>
                <div className='address'>{memberInfo.address}</div>
              </div>

              <div className='count'>
                <div className='first'>
                  <div>결제 내역</div>
                </div>
                <div>{filteredPayList.length}건</div>
              </div>
            </div>
          )}
        </div>

        <div className='right'>
          {/* 📌 필터링 & 정렬 UI 추가 */}
          <div className='filter-sort'>
            <select
              value={filterPeriod}
              onChange={(e) => setFilterPeriod(e.target.value)}
            >
              <option value='all'>전체 기간</option>
              <option value='1month'>최근 1개월</option>
              <option value='3months'>최근 3개월</option>
              <option value='6months'>최근 6개월</option>
              <option value='1year'>최근 1년</option>
            </select>

            <select
              value={filterPayment}
              onChange={(e) => setFilterPayment(e.target.value)}
            >
              <option value='all'>전체 결제 수단</option>
              <option value='카드'>카드</option>
              <option value='카카오페이'>카카오페이</option>
            </select>

            <select
              value={sortOrder}
              onChange={(e) => setSortOrder(e.target.value)}
            >
              <option value='newest'>최신순</option>
              <option value='oldest'>오래된순</option>
            </select>
          </div>

          <div className='myPayList'>
            {filteredPayList.length > 0 ? (
              filteredPayList.map((pay) => (
                <div className='pay' key={pay.id}>
                  <div className='pay-items'>
                    {pay.orderItemEntities?.length > 0 ? (
                      pay.orderItemEntities.map((item) => (
                        <div className='pay-item' key={item.id}>
                          <span className='itemCategory'>
                            {item.itemCategory}
                          </span>
                          <span className='itemPrice'>
                            {item.itemPrice.toLocaleString()}원
                          </span>
                        </div>
                      ))
                    ) : (
                      <p>구매한 상품 없음</p>
                    )}
                  </div>

                  <div className='pay-footer'>
                    <span className='paymentType'>
                      결제 수단: {pay.paymentType}
                    </span>
                    <span className='totalPrice'>
                      총 결제 금액: {pay.totalPrice.toLocaleString()}원
                    </span>
                  </div>

                  <div className='pay-date'>
                    <span>{new Date(pay.createTime).toLocaleString()}</span>
                  </div>
                </div>
              ))
            ) : (
              <p>결제목록 없음</p>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default Mypay

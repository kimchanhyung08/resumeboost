import React from "react"
import "../../css/pay/myPayList.css"

const MyPayList = ({ myPayList }) => {
  return (
    <div className='pay-list'>
      {myPayList.length === 0 ? (
        <p>구매 내역이 없습니다.</p>
      ) : (
        myPayList.map((pay) => (
          <div key={pay.id} className='pay-card'>
            <div className='pay-content'>
              {/* 구매한 상품 (왼쪽) */}
              <ul className='pay-items'>
                {pay.orderItemEntities.map((item) => (
                  <li key={item.id} className='pay-item'>
                    {item.itemCategory} - {item.itemPrice.toLocaleString()}원
                  </li>
                ))}
              </ul>
              {/* 결제 정보 (오른쪽) */}
              <div className='pay-info'>
                <span className='payment-type'>{pay.paymentType}</span>
                <span className='total-price'>
                  {pay.totalPrice.toLocaleString()}원
                </span>
                <span className='pay-date'>
                  {new Date(pay.createTime).toLocaleDateString()}
                </span>
              </div>
            </div>
          </div>
        ))
      )}
    </div>
  )
}

export default MyPayList

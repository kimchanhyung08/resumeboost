import React, { useEffect, useState } from 'react'
import jwtAxios from '../../util/jwtUtils';
import { useNavigate } from 'react-router-dom';
import { EC2_URL } from '../../constans';

const PayDetail = ({param}) => {

  const navigate = useNavigate();

  const [detail, setDetail] = useState({});

  const payDetailFn = async (payId) => {
      
    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/pay/detail/${payId}`);

    const data = res.data.pay;

    console.log(data);

    setDetail(data);

  }

  useEffect(()=>{

    payDetailFn(param)


  }, [])






  return (
    <div className='admin-pay-detail'>
      <div className='admin-pay-detail-con'>

        <span onClick={() => {
          navigate(-1)
        }}>뒤로 가기</span>
       

        <div className='admin-pay-detail-left'>
          <h1>구매한 상품</h1>
          <ul>
            {detail.orderItemEntities && detail.orderItemEntities.map((el,idx) => {
              return(
                <li key={idx}>
                  <div className='pay-detail-pocket'>
                    <ul>
                      <li>
                        <span>결제상품ID</span>
                        <span>{el.id}</span>
                      </li>
                      <li>
                        <span>결제시간</span>
                        <span>{el.createTime}</span>
                      </li>
                      <li>
                        <span>카테고리</span>
                        <span>{el.itemCategory}</span>
                      </li>
                      <li>
                        <span>결제금액</span>
                        <span>{el.itemPrice}</span>
                      </li>
                    </ul>
                  </div>
                </li>
              )
            })}
          </ul>
        </div>

        <div className='admin-pay-detail-right'>
          <div className='right-pay-info'>
            <h1>결제 정보</h1>
            <ul className='info-pay'>
              <li>
                <span>결제ID</span>
                <span>{detail.id}</span>
              </li>
              <li>
                <span>결제금액</span>
                <span>{detail.totalPrice}</span>
              </li>
              <li>
                <span>결제방식</span>
                <span>{detail.paymentType}</span>
              </li>
              <li>
                <span>결제자ID</span>
                <span>{detail.memberId}</span>
              </li>
            </ul>
            <ul className='info-member'>
              {detail.memberEntity && 
              <>
                <li>
                  <span>이름</span>
                  <span>{detail.memberEntity.userName}</span>
                </li>
                <li>
                  <span>주소</span>
                  <span>{detail.memberEntity.address}</span>
                </li>
                <li>
                  <span>전화번호</span>
                  <span>{detail.memberEntity.phone}</span>
                </li>
                <li>
                  <span>권한</span>
                  <span>{detail.memberEntity.role}</span>
                </li>
                <li>
                  <span>이메일</span>
                  <span>{detail.memberEntity.userEmail}</span>
                </li>
              </>
              }
              
            </ul>
          </div>
        </div>


      </div>
    </div>
  )




}

export default PayDetail
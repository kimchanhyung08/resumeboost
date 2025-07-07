import React, { useEffect, useState } from 'react'
import jwtAxios from '../../../util/jwtUtils';
import { EC2_URL } from '../../../constans';

const CartModalA = ({setIsModal, itemData, cartListFn}) => {



  const [item, setItem] = useState({}); // item 1개

  const [disable, setDisable] = useState(false); // 버튼 활성화 / 비활성화


  
  const selectItem = async (itemId) => {
    const url = `http://${EC2_URL}:8090/admin/item/detail/${itemId}`;

    const res = await jwtAxios.get(url);

    console.log(res.data.item);

    setItem(res.data.item);


    setDisable(true);    
    
  }



  // 수정할 값 state 에 저장
  const handleChange = (e) => {


    item[e.target.name] = e.target.value

    setItem({
      ...item
    })

  }


  
  // 장바구니 담기
  const addCartFn = async () => {

    const isAdd = window.confirm("상품을 담으시겠습니까?");
    if (!isAdd) {
      return;
    }


    const url = `http://${EC2_URL}:8090/admin/cart/addCart`;
    const header = {
      headers: {
          "Content-Type": "application/json"
      }
    }

    const res = await jwtAxios.post(url, item, header);

    console.log(res.data)


    if (res.data) {
      window.alert("이미 담긴 상품 입니다.");
    }


    setDisable(false);

    cartListFn();
  }





  const closeBtn = () => {
    setIsModal(false);
  }


  console.log(item)
 

  return (
    <>
      <div className='admin-cart-modal'>
        <div className='admin-cart-modal-con'>
          <span className='close' onClick={() => {closeBtn()}}>x</span>
          
          <div className='admin-cart-modal-wrap'>

            <div className='item-select'>
              <h2>상품 선택</h2>
              <ul>
                {itemData && Object.values(itemData).map(el => {
                  return(
                    <li className='admin-cart-modal-pocket' onClick={()=>{selectItem(el.id)}}>
                      <ul>
                        <li>상품ID {el.id}</li>
                        <li>멘토ID {el.memberId}</li>
                        <li>카테고리 {el.category}</li>
                        <li>가격 {el.itemPrice}</li>
                      </ul>
                    </li>
                  )
                })}
              </ul>
            </div>
            
            <div className='cart-insert'>
              <h2>장바구니 담기</h2>

              <label htmlFor="memberId">회원ID</label>
              <input type="text" name="memberId" id="memberId" onChange={(e)=>{handleChange(e)}}/>
              

              <button id="addCartBtn" disabled={!disable} onClick={()=>{addCartFn()}}>추가</button>


              <div className='select-item'>
                <h4>선택된 상품</h4>
                {item && 
                  <div>
                    <ul>
                      <li>
                        {item.id}
                      </li>
                      <li>
                        {item.category}
                      </li>
                      <li>
                        {item.itemPrice}
                      </li>
                    </ul>
                  </div>  
                }
              </div>


            </div>

          </div>
            
        </div>
      </div>

      
    </>
  )
}

export default CartModalA
import React, { Fragment, useEffect, useState } from 'react'
import jwtAxios from '../../util/jwtUtils';
import { useNavigate } from 'react-router-dom';
import PagingCart from './paging/PagingCart';
import CartModalA from './modal/CartModalA';
import { EC2_URL } from '../../constans';

const Cart = () => {

  const navigate = useNavigate();

  const [pageData, setPageData] = useState({}); // 장바구니 데이터

  const [itemData, setItemData] = useState({}); // 상품들 데이터


  const [isModal, setIsModal] = useState(false);


  const cartListFn = async () => {
    const url = `http://${EC2_URL}:8090/admin/cart`;
    const res = await jwtAxios.get(url);
    const data = res.data.cart; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const cartList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    console.log(res.data.cart);
    setPageData({
      startPage: startPage,
      endPage: endPage,
      cartList: cartList,
      currentPage: data.number,
      totalPages: totalPages
    })

  }


  const itemListFn = async () => {

    const url = `http://${EC2_URL}:8090/admin/item/itemList`;
    const res = await jwtAxios.get(url);
    const data = res.data.item; // 받은 모든 데이터
    
    setItemData(data);

  }



  useEffect(()=> {
   
    cartListFn();

    itemListFn();

  }, [])




  const onModal = () => {
    setIsModal(true);
  }






  const moveToCartDetail = (cartId) => {

    navigate(`/admin/cart/detail/${cartId}`);

  }


  






  return (

    <>
      <div className='admin-cart'>
        <div className='admin-cart-con'>
          {isModal && <CartModalA setIsModal={setIsModal} itemData={itemData} cartListFn={cartListFn} />}
      
          <h1>Cart Page</h1>  

          
          <span onClick={() => {onModal()}} className='admin-cart-insert'>장바구니 담기</span>

          

          <div className='admin-cart-list'>
            {pageData.cartList && Object.values(pageData.cartList).map((el, idx) => {
                  return(
                    <div className='item-pocket' key={idx} onClick={()=>{moveToCartDetail(el.id)}}>
                      <ul>
                        <li><h1>{el.memberEntity.nickName}</h1></li>
                        <li>장바구니ID {el.id}</li>
                        <li>회원ID {el.memberId}</li>
                      </ul>

                      {Object.values(el.itemListEntities).map((item, itemIdx) => {
                        return (
                          <Fragment key={itemIdx}> {/* <></> 와 같다. */}
                            <ul className='item-pocket-parts'>
                              <li>{item.createTime}</li>
                              <li>상품목록ID: {item.id}</li>
                              <li>상품ID {item.itemEntity.id}</li>
                            </ul>
                          </Fragment>
                        )
                      })}
                      
                    </div>
                  )
                })}
          </div>

          <div className="paging">
          
            <PagingCart startPage={pageData.startPage} endPage={pageData.endPage} currentPage={pageData.currentPage} totalPages={pageData.totalPages} setPageData={setPageData}/>
          </div>


        </div>
      </div>
    </>





  )
}

export default Cart
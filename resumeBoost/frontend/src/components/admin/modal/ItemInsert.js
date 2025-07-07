import React, { useState } from 'react'
import jwtAxios from '../../../util/jwtUtils';
import { EC2_URL } from '../../../constans';

const ItemInsert = ({loginId, itemListFn}) => {

  
  const [insert, setInsert] = useState({
    memberId: '',
    category: '',
    itemPrice: ''
  });



  const insertItem = async (e) => {
    e.preventDefault();

    const isInsert =  window.confirm('상품을 등록 하시겠습니까?');

    if (!isInsert) {
      return;
    }



    insert['memberId'] = loginId
    setInsert({
      ...insert
    })

    
    const url = `http://${EC2_URL}:8090/admin/item/insert`;
    const header = {
      headers: {
        "Content-Type": "application/json"
      }
    }
    await jwtAxios.post(url, insert, header);
    


    itemListFn(); // 최신화된 상품들 다시 get // 불러오기 

    console.log("item 등록!")
  }


  const handleChange = (e) => {

    insert[e.target.name] = e.target.value

    console.log(insert)

    setInsert({
      ...insert
    })

  }

  

  return (
    <>
      <form className='item-control'>
        <select name='category' id='category' onChange={(e)=>{handleChange(e)}}>
          <option name={"이력서"} value={"이력서"}>이력서</option>
          <option name={"자기소개서"} value={"자기소개서"}>자기소개서</option>
          <option name={"면접"} value={"면접"}>면접</option>
        </select>

        <label htmlFor="itemPrice">상품가격</label>
        <input type="text" name="itemPrice" id="itemPrice" onChange={(e)=>{handleChange(e)}}/>
        
        <button onClick={(e)=> {insertItem(e)}}>상품 등록하기</button>
      </form>
    </>
  )
}

export default ItemInsert
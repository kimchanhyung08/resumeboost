import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import jwtAxios from '../../util/jwtUtils';
import { getCookie } from '../../util/cookieUtil';
import { EC2_URL } from '../../constans';
import { S3URL } from '../../util/constant';

const ItemDetail = ({param}) => {

  const navigate = useNavigate();

  // const [loginId, setLoginId] = useState(''); // 로그인한 사람 ID

  const [detail, setDetail] = useState({}); // 멘토
  const [items, setItems] = useState({}); // 아이템
  const [update, setUpdate] = useState({}); // 업데이트

  const [insert, setInsert] = useState({ // 상품 추가
    itemPrice: '',
    category: '이력서'
  }); 


  // 멘토 데이터 가져오기 // 상품 데이터 가져오기
  const detailItem = async (mentorId) => {
    const url = `http://${EC2_URL}:8090/admin/member/detail/${mentorId}`;

    const res = await jwtAxios.get(url);

    console.log(res.data.member);

    setDetail(res.data.member);

  }

  
  const getItems = async (mentorId) => {
    const itemUrl = `http://${EC2_URL}:8090/admin/item/details/${mentorId}`;
    const itemRes = await jwtAxios.get(itemUrl);
    console.log(itemRes.data.item);
    setItems(itemRes.data.item);
  }



  useEffect(() => {

    detailItem(param);

    getItems(param);

    // const cookie = getCookie("member");

    // setLoginId(cookie.id);

  }, []);



  // 상품 삭제
  const deleteItem = async (itemId) => {

    const isDelete = window.confirm("상품을 수정하시겠습니끼?")
    if (!isDelete) {
      return;
    }


    const url = `http://${EC2_URL}:8090/admin/item/delete/${itemId}`;
    
    await jwtAxios.delete(url);

    getItems(param);
  }


  // 수정할 값 state 에 저장
  const handleChange = (e, itemId) => {

    const data = Object.values(items).filter(el => el.id === itemId)[0];

    data[e.target.name] = e.target.value

    setUpdate(data)

  }

  const handleChangeInsert = (e) => {

    insert[e.target.name] = e.target.value
    setInsert({
      ...insert
    })

  }

 


  // 상품 수정
  const itemUpdate = async (itemId) => {
    

    const isUpdate = window.confirm("상품을 수정하시겠습니끼?")
    if (!isUpdate) {
      return;
    }


    console.log(update)


    const url = `http://${EC2_URL}:8090/admin/item/update`;
    const header = {
        headers: {
            "Content-Type": "application/json"
      }
    }
  

    await jwtAxios.put(url, update, header);


    setUpdate({})

    // itemListFn();
  }

  const insertItem = async () => {
    console.log(insert)
    const isInsert = window.confirm("상품을 등록하시겠습니끼?")
    if (!isInsert) {
      return;
    }


    insert['memberId'] = param
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

    getItems(param);
  }




  return (
    <div className='admin-item-detail'>
      <div className='admin-item-detail-con'>

        <span className='admin-item-detail-back' onClick={() => {
          navigate(-1);
        }}>뒤로가기</span>


        <h1>item detail</h1>

        <div className='admin-item-detail-wrap'>
          <div className='admin-item-detail-left'>

            <div className='admin-item-detail-info'>
              <ul>
                <li><h3>{detail.nickName}</h3></li>
                
                <li>이름: {detail.userName}</li>
                <li>멘토ID: {detail.id}</li>
                <li>경력: {detail.career}</li>
                <li>주소: {detail.address}</li>
                <li>전화번호: {detail.phone}</li>
                <li>나이: {detail.age}</li>
                <li>이메일: {detail.userEmail}</li>
              </ul>

              <div className='admin-item-detail-img'>
                <h1>
                  {detail.attachFile ? 
                    <div className='mentor-img'>
                      <img src={`${S3URL}${detail.newImgName}`} alt='image'></img>
                    </div> :
                    <div className='mentor-img'>
                      <img src={`https://place-hold.it/200x200/666/fff/000?text= no Image`}></img>
                    </div>
                  }    
                </h1>
              </div>
            </div>

            <div className='admin-item-detail-insert'>
              <h1>상품 등록</h1>
              <select name="category" onChange={(e)=>{handleChangeInsert(e)}}>
                <option value="이력서">이력서</option>
                <option value="자기소개서">자기소개서</option>
                <option value="면접">면접</option>
              </select>

              <label htmlFor="itemPrice">가격</label>
              <input type='text' name='itemPrice' id='itemPrice' onChange={(e)=>{handleChangeInsert(e)}}/>
              
              <button onClick={()=>{insertItem()}}>상품 추가</button>
            </div>      

          </div>


          <div className='admin-item-detail-right'>
            <div className='item-list'>
              {items &&  Object.values(items).map((el, idx) => {
                
                return(
                  <div className='item-pocket' key={idx}>
                    <ul>
                      <li>
                        <label htmlFor="id">상품ID</label>
                        <input type="text" name="id" id="id" defaultValue={el.id} readOnly/>
                      </li>
                      <li>
                        <label htmlFor='memberId'>멘토ID</label>
                        <input type="text" name="memberId" id="memberId" defaultValue={el.memberId} onChange={(e)=>{handleChange(e, el.id)}}/>
                      </li>
                      <li>
                        <label htmlFor='category'>카테고리</label>
                        <input type="text" name="category" id="category" defaultValue={el.category} onChange={(e)=>{handleChange(e, el.id)}}/>
                      </li>
                      <li>
                        <label htmlFor='itemPrice'>상품가격</label>
                        <input type="text" name="itemPrice" id="itemPrice" defaultValue={el.itemPrice} onChange={(e)=>{handleChange(e, el.id)}}/>
                      </li>
                      <li>
                        <span>{el.createTime}</span>
                        <span>{el.updateTime}</span>
                      </li>
                      <li><button onClick={()=>{itemUpdate(el.id)}}>수정하기</button></li>
                      <li><button onClick={()=>{deleteItem(el.id)}}>삭제하기</button></li>
                    </ul>
                  </div>
                )})
                
              }

              {(items &&  Object.values(items).length === 0) && <h1>상품이 존재하지 않습니다.</h1>} 
            </div>
          </div>


        </div>
      </div>
    </div>
  )
}

export default ItemDetail
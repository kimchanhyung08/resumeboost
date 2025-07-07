import React, { useEffect, useState } from 'react'
import jwtAxios from '../../util/jwtUtils';
import PagingPay from './paging/PagingPay';
import { useNavigate } from 'react-router-dom';
import { EC2_URL } from '../../constans';

const Pay = () => {

  const navigate = useNavigate();


  const [pageData, setPageData] = useState({});

  const [searchData, setSearchData] = useState({ // search 키워드 값
    subject: '',
    search: ''
  }); 


  const payList = async () => {

    const url = `http://${EC2_URL}:8090/admin/pay`;

    const res = await jwtAxios.get(url);

    console.log(res);


    // exception 처리
    if (typeof(res.data) === "string") {
      
      const errorMessage = res.data;

      window.alert(errorMessage);

      return 
    }



    const data = res.data.pay

    const newCurrentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;
    const payList = data.content;
    const startPage = ((Math.floor(newCurrentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(newCurrentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;


    setPageData({
      startPage: startPage,
      endPage: endPage,
      payList: payList,
      currentPage: data.number,
      totalPages: totalPages
    })

   
  }



  const insertPay = async () => {

    const data = {
      memberId: 2,
      totalPrice: 10000,
      paymentType: 'kakao'
    }

    const url = `http://${EC2_URL}:8090/admin/pay/insert`;

    const header = {
      headers: {
          "Content-Type": "application/json"
      }    
    }
  
    
    await jwtAxios.post(url, data, header);

    
  }

  useEffect(() => {

    payList();    

  }, [])



  const handleChange = (e) => { // search 키워드 값 저장
    
    searchData[e.target.name] = e.target.value
    console.log(searchData)
    setSearchData({
      ...searchData
    })

  }


  const searchFn = async (e) => {
    e.preventDefault();

    console.log("search!!")

    const url = `http://${EC2_URL}:8090/admin/pay?subject=${searchData.subject}&search=${searchData.search}`;

    const res = await jwtAxios.get(url);


    // exception 처리
    if (typeof(res.data) === "string") {
      
      const errorMessage = res.data;

      window.alert(errorMessage);

      return 
    }
      

    const data = res.data.pay; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const payList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    setPageData({
      startPage: startPage,
      endPage: endPage,
      payList: payList,
      currentPage: data.number,
      totalPages: totalPages
    })


  }


  const payDetail = (payId) => {
    navigate(`/admin/pay/detail/${payId}`);
  }



 
  return (
    <>
      <div className='admin-pay'>
        <div className='admin-pay-con'>
          
          {/* <div className='admin-pay-insert'>
            <button onClick={()=>{insertPay()}} disabled={true}>결제하기</button>
          </div> */}

          <div className='admin-pay-search'>
            <form action="/noEffect" method="post">
              <select name="subject" id="subject" onChange={(e) => {handleChange(e)}}>
                <option value={""}>선택</option>
                <option value={"paymentType"}>결제방식</option>
                <option value={"memberId"}>결제자ID</option>
              </select> 
              <input type='text' name='search' id='search' placeholder='검색어 입력' onChange={(e) => {handleChange(e)}}></input>
              <button onClick={(e) => {searchFn(e)}}>검색</button>
            </form>
          </div>



          <div className='admin-pay-list'>

            <div className='admin-pay-head'>
              <ul>
                <li>결제 ID</li>
                <li>결제자 ID</li>
                <li>결제시간</li>
                <li>결제방식</li>
                <li>결제금액</li>
              </ul>
            </div>


            <div className='admin-pay-body'>
              {pageData.payList && Object.values(pageData.payList).map((el, idx) => {
                return(
                  <div className='pay-pocket' key={idx} onClick={()=>{payDetail(el.id)}} >
                    <ul>
                      <li>{el.id}</li>
                      <li>{el.memberEntity.id}</li>
                      <li>{el.createTime}</li>
                      <li>{el.paymentType}</li>
                      <li>{el.totalPrice}</li>
                    </ul>
                  </div>
                )
              })}
            </div>

          </div>

          <div className='paging'>
            <PagingPay startPage={pageData.startPage} endPage={pageData.endPage} currentPage={pageData.currentPage} totalPages={pageData.totalPages} setPageData={setPageData}/>
          </div>

        </div>
      </div>
    </>
  )
}

export default Pay
import axios from 'axios'
import React, { useEffect, useState } from 'react'
import jwtAxios from '../../util/jwtUtils'
import { Link } from 'react-router-dom';
import MemberModalA from './modal/MemberModalA';
import Paging from './paging/Paging';
import { EC2_URL } from '../../constans';
import { S3URL } from '../../util/constant';

const Member = () => {


  

  const [pageData, setPageData] = useState({});

  // 모달 
  const [isModal, setIsModal] = useState(false)
  const [memberId, setMemberId] = useState("")

  // search 키워드 값
  const [searchData, setSearchData] = useState({ 
    subject: '',
    search: ''
  }); 


  useEffect(()=> {

    const memberList = async () => {
      const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/member`);
      
      console.log(res.data); 
      
      const data = res.data.member; // 받은 모든 데이터
      
      // 페이징 메서드 
      const currentPage = data.number;
      const totalPages = data.totalPages;
      const blockNum = 3;

      const memberList = data.content;
      
      const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
      const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;


      setPageData({
        startPage: startPage,
        endPage: endPage,
        memberList: memberList,
        currentPage: data.number,
        totalPages: totalPages
      }) 
      
    }
    
    memberList();
    
  }, [isModal]); // isModal -> 모달창이 켜지고 꺼질 때 마다 작동 // 수정 & 삭제 시 맴버 리스트 다시 받아와서 랜더
  
  console.log(pageData) // 필요한 데이터 



  console.log(pageData.memberList)
  console.log(typeof(pageData.memberList))


  

  const memberDetail = (id) => {
    console.log("hhahahahahh")
    console.log(id)

    setMemberId(id)

    setIsModal(true)
  }



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

    const url = `http://${EC2_URL}:8090/admin/member?subject=${searchData.subject}&search=${searchData.search}`;

    const res = await jwtAxios.get(url);

    const data = res.data.member; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const memberList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    setPageData({
      startPage: startPage,
      endPage: endPage,
      memberList: memberList,
      currentPage: data.number,
      totalPages: totalPages
    })


  }


  const clickSearchFn = async (e) => {

    const role = e.target.getAttribute('name');

    console.log(role)

    const url = `http://${EC2_URL}:8090/admin/member?subject=role&search=${role}`;

    const res = await jwtAxios.get(url);

    const data = res.data.member; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const memberList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    setPageData({
      startPage: startPage,
      endPage: endPage,
      memberList: memberList,
      currentPage: data.number,
      totalPages: totalPages
    })


  }




  return (
    <>
      <div className='admin-member'>
        <div className='admin-member-con'>
          {isModal && <MemberModalA memberId={memberId} setIsModal={setIsModal}/>}

          <div className='admin-member-search'>

            <form action="/noEffect" method="post">
              <select name="subject" id="subject" onChange={(e) => {handleChange(e)}}>
                <option value={""}>선택</option>

                <option value={"userName"}>이름</option>

                <option value={"id"}>회원ID</option>

                <option value={"nickName"}>닉네임</option>
              </select> 
              <input type='text' name='search' id='search' placeholder='검색어 입력' onChange={(e) => {handleChange(e)}}></input>
              <button onClick={(e) => {searchFn(e)}}>검색</button>
            </form>

          </div>

          <div className='admin-member-click-search'>
            <span onClick={(e)=>{clickSearchFn(e)}} name='MEMBER'>일반회원</span>
            <span onClick={(e)=>{clickSearchFn(e)}} name='MENTOR'>멘토</span>
            <span onClick={(e)=>{clickSearchFn(e)}} name='ADMIN'>관리자</span>
          </div>


          <div className='admin-member-list'>
            {pageData.memberList && Object.values(pageData.memberList).map((el, idx) => {
              return(
                <div className='member-pocket' key={idx} onClick={()=>{memberDetail(el.id)}}>
                  <div>
                    {el.attachFile ? 
                      <div className='member-img'>
                        <img src={`${S3URL}${el.newImgName}`} alt='image'></img>
                      </div> :
                      <div className='member-img'>
                        <img src={`https://place-hold.it/100x100/666/fff/000?text= no Image`}></img>
                      </div>
                    }                 
                  </div>
                  <ul>
                    <li>
                      <h3>닉네임 {el.nickName}</h3>
                    </li>
                    <li>회원ID {el.id}</li>
                    <li>이름 {el.userName}</li>
                    <li>권한 {el.role}</li>
                  </ul>
                </div>
              )
            })}
          </div>
          
        
          <div className="paging">
            <Paging startPage={pageData.startPage} endPage={pageData.endPage} currentPage={pageData.currentPage} totalPages={pageData.totalPages} setPageData={setPageData}/>
          </div>

        </div>
      </div>
    </>
  )
}

export default Member
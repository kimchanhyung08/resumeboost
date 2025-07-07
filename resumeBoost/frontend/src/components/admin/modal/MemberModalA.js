import axios from 'axios'
import React, { useEffect, useState } from 'react'
import jwtAxios from '../../../util/jwtUtils'
import { EC2_URL } from '../../../constans'
import { S3URL } from '../../../util/constant'

const MemberModalA = ({memberId, setIsModal}) => {

  const [modal, setModal] = useState({})

  const [update, setUpdate] = useState({})

  const memberDetail = async () => {
    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/member/detail/${memberId}`);

    const data = res.data.member;

    console.log(data)
    // console.log(typeof(data.role))

    setModal(data)

    setUpdate(data)
  }    


  useEffect(()=> { // 페이지 접속 시 모달 창에 데이터 setting

    memberDetail()

  },[])


  // 회원 삭제
  const memberDelete = async (memberId) => {

    const isDelete = window.confirm("회원을 삭제하시겠습니까?")

    if (!isDelete) {
      return 
    }

    
    await jwtAxios.delete(`http://${EC2_URL}:8090/admin/member/delete/${memberId}`);


    console.log("delete Ok")

    setIsModal(false); // 삭제 후 모달 끄기

  }



  // 수정 -> 변경 값 저장
  const handleChange = (e) => {
    
    update[e.target.name] = e.target.value
    console.log(update)
    setUpdate({
      ...update
    })

  }

  // 파일 담기
  const handleFile = (e) => {
    // const data = e.target.value

    console.log(e.target.files[0])
    // const file = new Blob([data], {type: 'multipart/form-data'})
    const file = e.target.files[0]

    update[e.target.name] = file
    
    setUpdate({
      ...update
    })

  }


  // 회원 수정
  const memberUpdate = async () => { 
    console.log(update)
    const isUdate  = window.confirm("회원정보를 수정하시겠습니까?")

    if (!isUdate) {
      return 
    }

    const header = {
      headers: {
        "Content-Type":"application/json"
      }
    }


    // 할 거 board
    // 서칭
    // UI

    // 수정한 항목
    //yml)
    //-username
    //-url
    // file.path
    

    const body = new URLSearchParams(update) // 객체를 FormData 로 바꿔 줌

    const form = new FormData();

    form.append("userName", update.userName)
    form.append("userEmail", update.userEmail)
    form.append("userPw", update.userPw)
    form.append("phone", update.phone)
    form.append("nickName", update.nickName)
    form.append("role", update.role)
    form.append("id", update.id)
    form.append("career", update.career)
    form.append("age", update.age)
    form.append("address", update.address)

    if (update.profileFile !== null) {
      form.append("profileFile", update.profileFile)
    }

    await jwtAxios.put(`http://${EC2_URL}:8090/member/modifyA`, form, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });

    memberDetail(); // 수정 후 다시 get

    // await jwtAxios.put(`http://${EC2_URL}:8090/admin/member/update`, update, header);

  }

  


  // 모달 창 끄기
  const closeBtn = () => {
    
    setIsModal(false)
    
  }

 
  return (
    <div className='admin-member-modal'>
      <div className='admin-member-modal-con'>
        <span className='close' onClick={closeBtn}>X</span>


        <h1>회원 상세 정보</h1>
        <form>
          <ul>
            <li>
              
              
              <label htmlFor="userName">이름</label>
              <input type='text' id = 'userName' name='userName' defaultValue={modal.userName} onChange={handleChange}></input>
             
            </li>
            <li>
              <label htmlFor="userEmail">이메일</label>
              <input type='text' id = 'userEmail' name='userEmail' defaultValue={modal.userEmail} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="userPw">비밀번호</label>
              <input type='text' id = 'userPw' name='userPw' defaultValue={modal.userPw} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="phone">전화번호</label>
              <input type='text' id = 'phone' name='phone' defaultValue={modal.phone} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="nickName">닉네임</label>
              <input type='text' id = 'nickName' name='nickName' defaultValue={modal.nickName} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="role">권한</label>
              <input type='text' id = 'role' name='role' defaultValue={modal.role} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="id">id</label>
              <input type='text' id = 'id' name='id' defaultValue={modal.id} readOnly></input>
            </li>
            <li>
              <label htmlFor="career">career</label>
              <input type='text' id = 'career' name='career' defaultValue={modal.career} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="age">age</label>
              <input type='text' id = 'age' name='age' defaultValue={modal.age} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="address">address</label>
              <input type='text' id = 'address' name='address' defaultValue={modal.address} onChange={handleChange}></input>
            </li>
            <li>
              <label htmlFor="profileFile">프로필 사진</label>
              <input type="file" name="profileFile" id="profileFile" onChange={handleFile}/>
            </li>

            <li>
              <span onClick={() => {memberUpdate()}}>수정</span>
              <span onClick={() => {memberDelete(modal.id)}}>삭제</span>
            </li>
            
          </ul>

          <div className='admin-member-modal-img'>
            {modal.attachFile ? 
              <img src={`${S3URL}${modal.newImgName}`} alt='image'></img>
              :
              <img src={`https://place-hold.it/100x100/666/fff/000?text= no Image`}></img>
            }
          </div>

        </form>


      </div>
    </div>
  )
}

export default MemberModalA
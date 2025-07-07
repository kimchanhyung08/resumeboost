import React, { useEffect, useState } from 'react'
import jwtAxios from '../../../util/jwtUtils';
import { EC2_URL } from '../../../constans';

const ReplyModalA = ({boardId, setIsModal}) => {

  const [reply, setReply] = useState([]);


  const replyAll = async () => {
    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/reply/${boardId}`);

    const data = res.data.reply;

    console.log(data)
    // console.log(typeof(data.role))

    setReply(data);

  }    

  

  useEffect(()=>{

    replyAll();

  }, []);




  // 모달 창 끄기
  const closeBtn = () => {
    
    setIsModal(false)
    
  }


  const deleteFn = async (replyId) => {

    const isDelete = window.confirm("덧글을 삭제하시겠습니까?");
    
    if (!isDelete) {
      return
    }


    await jwtAxios.delete(`http://${EC2_URL}:8090/admin/reply/delete/${replyId}`);

    window.alert("덧글이 삭제되었습니다.")

    replyAll();

  }




  return (
    <div className='admin-reply-modal'>
      <div className='admin-reply-modal-con'>
        <span className='close' onClick={closeBtn}>X</span>

       
        <h1>게시글 덧글 정보</h1>

        <div className='admin-reply-contents'>
            {reply && reply.map(el => {{
              return (
                <>
                  <div className='reply-keeps'>
                    <ul>
                      <li>
                        <label htmlFor='id'>덧글ID</label>
                        <input type='text' id='id' defaultValue={el.id} readOnly></input>
                      </li>
                      <li>
                        <label htmlFor='memberId'>작성자ID</label>
                        <input type='text' id='memberId' defaultValue={el.memberEntity.id} readOnly></input>
                      </li>
                      <li>
                        <label htmlFor='createTime'>생성시간</label>
                        <input type='text' id='createTime' defaultValue={el.createTime} readOnly></input>
                      </li>

                    </ul>
                  </div>

                  <div className='reply-updates'>
                    <textarea defaultValue={el.content}>
                      
                    </textarea>
                  </div>

                  <div className='reply-btns'>
                    <span>수정</span>
                    <span onClick={()=>deleteFn(el.id)}>삭제</span>
                  </div>

                </>
              
              )
            }})}
        </div>
      </div>
    </div>
  )
}

export default ReplyModalA
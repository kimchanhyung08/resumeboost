import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import jwtAxios from '../../util/jwtUtils';
import ReplyModalA from './modal/ReplyModalA';
import { EC2_URL } from '../../constans';
import { S3URL } from '../../util/constant';

const BoardDetail = ({param}) => {

  const navigate = useNavigate();

  const [detail, setDetail] = useState({});

   // 모달 
  const [isModal, setIsModal] = useState(false)




  const boardDetailFn = async (boardId) => {
      
    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/board/detail/${boardId}`);

    const data = res.data.board;

    console.log(data);

    setDetail(data);

  }


  useEffect(() => {

    boardDetailFn(param);
    console.log(param)

  }, [])



  const boardDeleteFn = async (boardId) => {
  
      const isDelete = window.confirm("게시글을 삭제하시겠습니까?")
  
      if (!isDelete) {
        return
      }
  
      await jwtAxios.delete(`http://${EC2_URL}:8090/board/delete/${boardId}`); // 게시글 삭제
  
      console.log("delete Ok")

      navigate({pathname: '/admin/board'}, {replace: true}); // 삭제 후 리스트로 & 뒤로 가기 x
      
    }



   // 수정 -> 변경 값 저장
   const handleChange = (e) => {
    
    detail[e.target.name] = e.target.value
    console.log(detail)
    setDetail({
      ...detail
    })

  }


  // 파일 담기
  const handleFile = (e) => {
    
    console.log(e.target.files[0])

    const file = e.target.files[0]

    detail[e.target.name] = file
    
    setDetail({
      ...detail
    })

  }


  const boardUpdateFn = async () => {

    console.log(detail)

    const boardId = detail.id;

    const isUdate  = window.confirm("회원정보를 수정하시겠습니까?")

    if (!isUdate) {
      return 
    }

    const form = new FormData();

    form.append("id", detail.id)
    form.append("title", detail.title)
    form.append("content", detail.content)
    form.append("category", detail.category)
    form.append("viewCount", detail.viewCount)
    form.append("attachFile", detail.attachFile)
    form.append("memberId", detail.memberId)
    form.append("replyCount", detail.replyCount)
    form.append("writer", detail.writer)

    if (detail.boardImgFile !== null) {
      form.append("boardImgFile", detail.boardImgFile)
    }

    await jwtAxios.put(`http://${EC2_URL}:8090/board/update`, form, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });

    
    boardDetailFn(param); // 수정 후 다시 get

  }

  const replyModalOn = () => {
    setIsModal(true);
  }




  return (
    <>
      <div className='board-detail'>
        <div className='board-detail-con'>
          {isModal && <ReplyModalA boardId={param} setIsModal={setIsModal}/>}


          {detail && 
          <form>

            <div className='board-detail-head'>
              <div className='admin-board-detail-btns'>
                <span onClick={() => {
                  navigate(-1)
                }}>뒤로 가기</span>
                <span onClick={() => {boardUpdateFn()}}>수정</span>
                <span onClick={() => {boardDeleteFn(detail.id)}}>삭제</span>
              </div>
            </div>

            
            <div className='board-detail-body'>
              <div className='board-detail-update'>

                <div className='board-detail-updates'>
                  <label htmlFor="title">제목</label>
                  <input type="text" name="title" id="title" defaultValue={detail.title} onChange={handleChange}/>
                  <label htmlFor="category">카테고리</label>
                  <input type="text" name="category" id="category" defaultValue={detail.category} onChange={handleChange}/>
                  <label htmlFor="boardImgFile">사진 첨부</label>
                  <input type="file" name="boardImgFile" id="boardImgFile" onChange={handleFile}/>
                </div>
                
                <span className='admin-reply-btn' onClick={()=>{replyModalOn()}}>댓글 관리</span>
                 
                {/* textarea 글 작성 칸 */}
                <textarea name='content' defaultValue={detail.content} onChange={handleChange}></textarea> 
              </div>

              <div className='board-detail-infos'>
                <h1>INFO</h1>
                <ul>
                  <li>
                    <label htmlFor="id">게시글 ID</label>
                    <input type="text" name="id" id="id" defaultValue={detail.id} readOnly/>
                  </li>
                  <li>
                    <label htmlFor="viewCount">조회수</label>
                    <input type="text" name="viewCount" id="viewCount" defaultValue={detail.viewCount} readOnly/>
                  </li>
                  <li>
                    <label htmlFor="attachFile">파일유무</label>
                    <input type="text" name="attachFile" id="attachFile" defaultValue={detail.attachFile} readOnly/>
                  </li>
                  <li>
                    <label htmlFor="memberId">작성자 ID</label>
                    <input type="text" name="memberId" id="memberId" defaultValue={detail.memberId} readOnly/>
                  </li>
                  <li>
                    <label htmlFor="replyCount">덧글 수 </label>
                    <input type="text" name="replyCount" id="replyCount" defaultValue={detail.replyCount} readOnly/>
                  </li>
                  <li>
                    <label htmlFor="writer">작성자</label>
                    <input type="text" name="writer" id="writer" defaultValue={detail.writer} readOnly/>
                  </li>
                  <li>
                    {detail.attachFile ?
                      <div className='admin-board-img'>
                        <img src={`${S3URL}${detail.newImgName}`} alt='image'></img>
                      </div> :
                      <div>
                        <img src={`https://place-hold.it/150x150/666/fff/000?text= no Image`}></img>
                      </div>
                    }
                  </li>




                </ul>
              </div>
            </div>

            







           



           

             
                

                
              
            




          </form>
          }


        </div>
      </div>
    </>
  )
}

export default BoardDetail
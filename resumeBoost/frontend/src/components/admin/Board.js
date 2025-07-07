import React, { useEffect, useState } from 'react'
import jwtAxios from '../../util/jwtUtils'
import Paging from './paging/Paging';
import { useNavigate } from 'react-router-dom';
import { EC2_URL } from '../../constans';

const Board = () => {

  const navigate = useNavigate();

  const [pageData, setPageData] = useState({}); // board list

  const [searchData, setSearchData] = useState({ // search 키워드 값
    subject: '',
    search: ''
  }); 
  

  const boardList = async () => {

    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/board`);
    
    console.log(res.data);

    const data = res.data.board; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const boardList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    setPageData({
      startPage: startPage,
      endPage: endPage,
      boardList: boardList,
      currentPage: data.number,
      totalPages: totalPages
    })

  }


  useEffect(() => {

    boardList()

  }, [])


  // console.log(pageData) // 필요한 데이터


  const moveToDetailFn = (boardId) => {
    navigate(`/admin/board/detail/${boardId}`);
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

    const url = `http://${EC2_URL}:8090/admin/board?subject=${searchData.subject}&search=${searchData.search}`;

    const res = await jwtAxios.get(url);

    const data = res.data.board; // 받은 모든 데이터

    // 페이징 메서드 
    const currentPage = data.number;
    const totalPages = data.totalPages;
    const blockNum = 3;

    const boardList = data.content;
    
    const startPage = ((Math.floor(currentPage/blockNum) * blockNum) + 1 <= totalPages ? (Math.floor(currentPage/blockNum) * blockNum) + 1 : totalPages);
    const endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

    setPageData({
      startPage: startPage,
      endPage: endPage,
      boardList: boardList,
      currentPage: data.number,
      totalPages: totalPages
    })


  }

  

  return (
    <>
      <div className='admin-board'>
        <div className='admin-board-con'>
          
        
          <div className='admin-board-search'>
            <form action="/noEffect" method="post">
              <select name="subject" id="subject" onChange={(e) => {handleChange(e)}}>
                <option value={""}>선택</option>
                <option value={"writer"}>작성자</option>
                <option value={"title"}>제목</option>
              </select> 
              <input type='text' name='search' id='search' placeholder='검색어 입력' onChange={(e) => {handleChange(e)}}></input>
              <button onClick={(e) => {searchFn(e)}}>검색</button>
            </form>
          </div>


          <div className='admin-board-list'>

            <div className='admin-board-head'>
              <ul>
                <li>게시글 ID</li>
                <li>작성자 ID</li>
                <li>제목</li>
                <li>카테고리</li>
                <li>작성자</li>
                <li>조회수</li>
                <li>덧글수</li>
                <li>파일유무</li>
              </ul>
            </div>


            <div className='admin-board-body'>

              {pageData.boardList && Object.values(pageData.boardList).map((el, idx) => {
                return(
                  <div className='board-pocket' key={idx} onClick={() => {moveToDetailFn(el.id)}}>
                    <ul>
                      <li>{el.id}</li>
                      <li>{el.memberId}</li>
                      <li>{el.title}</li>
                      <li>{el.category}</li>
                      <li>{el.writer}</li>
                      <li>{el.viewCount}</li>
                      <li>{el.replyCount}</li>
                      {el.attachFile === 1 ? <li>♥</li> : <li></li>}
                    </ul>
                  </div>
                )
              })}
                

            </div>
          </div>

          <div className='paging'>
            <Paging startPage={pageData.startPage} endPage={pageData.endPage} currentPage={pageData.currentPage} totalPages={pageData.totalPages} setPageData={setPageData}/>
          </div>

        </div>
      </div>
    </>
  )
}

export default Board
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { EC2_URL } from '../../constans';

const Work24 = () => {
  const [work, setWork] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [startPage, setStartPage] = useState(1);
  const [endPage, setEndPage] = useState(1);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchWorkData = async () => {
      setLoading(true);
      try {
        const res = await axios.get(`http://${EC2_URL}:8090/api/work24?startPage=${page}`);
        const data = res.data;
        setWork(data.dhsOpenEmpInfo);
        console.log(data.dhsOpenEmpInfo);
        
        const total = parseInt(data.total, 10);
        const display = parseInt(data.display, 10);
        const totalPageCount = Math.ceil(total / display);
        setTotalPages(totalPageCount);
        
        const block = 5;
        const currentBlock = Math.floor((page - 1) / block);
        const newStartPage = currentBlock * block + 1;
        const newEndPage = Math.min(newStartPage + block - 1, totalPageCount);
        
        setStartPage(newStartPage);
        setEndPage(newEndPage);
      } catch (error) {
        console.error("데이터를 불러오는 중 오류 발생: ", error);
      }
      setLoading(false);
    };
    
    fetchWorkData();
  }, [page]);

  const highlightText = (text, highlight) => {
    const regex = new RegExp(`(${highlight})`, 'gi'); // 대소문자 구분 없이 검색
    return text.split(regex).map((part, index) =>
      part.toLowerCase() === highlight.toLowerCase() ? (
        <span key={index} className='vertical'>
          {part}
        </span>
      ) : (
        part
      )
    );
  };

  const formatEndDate = (endDate) => {
    const currentDate = new Date(); // 현재 날짜
    const endYear = endDate.substring(0, 4); // 연도 추출
    const endMonth = endDate.substring(4, 6); // 월 추출
    const endDay = endDate.substring(6, 8); // 일 추출
    
    const endDateObj = new Date(`${endYear}-${endMonth}-${endDay}`); // 끝 날짜 객체 생성
  
    const diffTime = endDateObj - currentDate; // 남은 시간 차이 (밀리초 단위)
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 밀리초를 일(day) 단위로 변환
  
    if (diffDays < 0) {
      return `${endMonth}.${endDay}(${getDayOfWeek(endDateObj)})`; // 이미 지난 날짜는 년도 빼고, 월일 요일 형식으로 출력
    }
  
    if (diffDays <= 5) {
      return `D-${diffDays}`; // 남은 5일 이내는 D-XX 형식으로 출력
    }
  
    return `~${endMonth}.${endDay}(${getDayOfWeek(endDateObj)})`; // 그 외의 날짜는 월일 요일 형식으로 출력
  };
  
  // 요일을 얻는 함수
  const getDayOfWeek = (date) => {
    const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
    return daysOfWeek[date.getDay()]; // 요일 숫자에 해당하는 한글 요일 반환
  };

  // 페이지가 변경될 때마다 맨 위로 스크롤
  const handlePageChange = (newPage) => {
    setPage(newPage);
    window.scrollTo(0, 0);  // 화면을 맨 위로 스크롤
  };

  return (
    <div className='work24'>
      <div className="work24-con">
        <div className="work24-header">
          <h1>공채 속보</h1>
          <span>RESUMEBOOST는 고용24의 공채속보를 실시간으로 띄워 드립니다.</span>
        </div>
        <div className="work24-body">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ul>
              {work && work.length > 0 ? (
                work.map(job => (
                  <li key={job.empWantedTitle}>
                    <a href={job.empWantedHomepgDetail} target="_blank" rel="noopener noreferrer">
                      <div className="job-logo">
                        <img src={job.regLogImgNm} alt="job-logo" />
                      </div>
                      <div className="job-title">{job.empWantedTitle}</div>
                      <div className="job-corp">{job.empBusiNm}</div>
                      <div className="job-desc">
                        <div className="desc-left">
                          <img src="/images/jobicon.png" alt="job-icon" />
                          <span className='job-emp'>{highlightText(job.empWantedTypeNm, "|")}</span>
                        </div>
                        <span className="job-time">{formatEndDate(job.empWantedEndt)}</span>
                      </div>
                    </a>
                  </li>
                ))
              ) : (
                <p>일자리 정보가 없습니다.</p>
              )}
            </ul>
          )}
        </div>
      </div>
      {work.length > 0 && (
        <div className='pagination'>
          <button onClick={() => handlePageChange(1)} disabled={page === 1}>
            &lt;&lt;
          </button>
          <button onClick={() => handlePageChange(page - 1)} disabled={page === 1}>
            &lt;
          </button>
          {Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i).map((p) => (
            <button key={p} onClick={() => handlePageChange(p)} className={page === p ? "active" : ""}>
              {p}
            </button>
          ))}
          <button onClick={() => handlePageChange(page + 1)} disabled={page === totalPages}>
            &gt;
          </button>
          <button onClick={() => handlePageChange(totalPages)} disabled={page === totalPages}>
            &gt;&gt;
          </button>
        </div>
      )}
    </div>
  );
};

export default Work24;

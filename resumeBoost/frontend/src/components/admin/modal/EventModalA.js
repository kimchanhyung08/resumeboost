import React, { useEffect, useState } from 'react'
import { getCookie } from '../../../util/cookieUtil';
import jwtAxios from '../../../util/jwtUtils';
import { EC2_URL } from '../../../constans';

const EventModalA = ({setIsModal}) => {

  const [loginId, setLoginId] = useState({});

  const [start, setStart] = useState(''); // ex '2025-03-23'
  const [end, setEnd] = useState(''); 


  const [schedule, setSchedule] = useState({ 
    start: '',
    end: '',
    title: '',
    content: '',
    memberId: ''
  });


  const closeBtn = () => {
    setIsModal(false);
  }

  const changeHandler = (e) => {

    schedule[e.target.name] = e.target.value
    setSchedule({
      ...schedule
    })
  }

  const changeTimeHandler = (e) => {

    const inputVal = e.target.value // 2020-03-23
    const eventDate = new Date(inputVal) // Date 객체 값 비교하는 법: <, >, ===
   
    // schedule[e.target.name] = eventDate

    schedule[e.target.name] = e.target.value

    setSchedule({
      ...schedule
    })
    
  }



  const addSchedule = async () => {

    // start <= end
    // 빈값 x


    schedule['memberId'] = loginId;
    setSchedule({
      ...schedule
    })


    
    await jwtAxios.post(`http://${EC2_URL}:8090/admin/event/insert`, schedule, {
      headers: {
        "Content-Type": "application/json"
      }
    });

    window.alert("일정이 추가되었습니다")

    setIsModal(false);

  }


  useEffect(()=>{

    const cookieData = getCookie("member");
    const id = cookieData.id;
    
    setLoginId(id);

  },[])


  console.log(schedule)

  return (

    <>
      <div className='event-modal'>
        <div className='event-modal-con'>

          <div className='event-modal-title'>
            <input type="text" name="title" id="title" defaultValue={"제목을 입력해주세요."} onChange={(e)=>{changeHandler(e)}}/>
          </div>

          <div className='event-time-select'>
            <input type="datetime-local" name="start" id="date" onChange={(e)=>{changeTimeHandler(e)}}/>
            ~
            <input type="datetime-local" name="end" id="date" onChange={(e)=>{changeTimeHandler(e)}}/>
          </div>

          

          <textarea name='content' defaultValue={"내용을 입력해주세요."} onChange={(e)=>{changeHandler(e)}}>

          </textarea>


          <div className='event-modal-btns'>
            <span onClick={()=>{addSchedule()}}>추가</span>
            <span onClick={()=>{closeBtn()}}>닫기</span>
          </div>
        </div>
      </div>
    </>
  )
}

export default EventModalA
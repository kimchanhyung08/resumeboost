import React, { useEffect, useState } from 'react'
import { getCookie } from '../../../util/cookieUtil';
import jwtAxios from '../../../util/jwtUtils';
import { EC2_URL } from '../../../constans';

const EventUpdateModal = ({setIsUpdateModal, updateItemId}) => {

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
    setIsUpdateModal(false);
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
   
    schedule[e.target.name] = eventDate
    setSchedule({
      ...schedule
    })
    
  }



  const updateSchedule = async () => {

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

    setIsUpdateModal(false);

  }

  const detailSchedule = async (itemId) => {

    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/event/detail/${itemId}`);
    const data = res.data.event
   
    console.log(data)

    setSchedule(data);

  }

  console.log(schedule)

  useEffect(()=>{

    const cookieData = getCookie("member");
    const id = cookieData.id;
    
    setLoginId(id);

    detailSchedule(updateItemId);

  },[])


  console.log(schedule)

  return (

    <>
      <div className='event-modal'>
        <div className='event-modal-con'>

          <div className='event-modal-title'>
            <input type="text" name="title" id="title" defaultValue={schedule.title} onChange={(e)=>{changeHandler(e)}}/>
          </div>

          <div className='event-time-select'>
            <input type="datetime-local" name="start" id="date" onChange={(e)=>{changeTimeHandler(e)}} defaultValue={schedule.start}/>
            ~
            <input type="datetime-local" name="end" id="date" onChange={(e)=>{changeTimeHandler(e)}} defaultValue={schedule.end}/>
          </div>

          

          <textarea name='content' defaultValue={schedule.content} onChange={(e)=>{changeHandler(e)}}>

          </textarea>


          <div className='event-modal-btns'>
            <span onClick={()=>{updateSchedule()}}>수정</span>
            <span onClick={()=>{closeBtn()}}>닫기</span>
          </div>
        </div>
      </div>
    </>
  )
}

export default EventUpdateModal
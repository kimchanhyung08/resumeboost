import React, { useEffect, useState } from 'react'
import { getCookie } from '../../util/cookieUtil';

import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from "@fullcalendar/interaction"
import EventModalA from './modal/EventModalA'
import jwtAxios from '../../util/jwtUtils';
import EventUpdateModal from './modal/EventUpdateModal';
import { EC2_URL } from '../../constans';

const CalendarCompo = () => {

  const [schedule, setSchedule] = useState([]);

  const [isModal, setIsModal] = useState(false);

  const [showData, setShowData] = useState({});

  const [isInfo, setIsInfo] = useState(false);

  const [isUpdateModal, setIsUpdateModal] = useState(false);  

  const [updateItemId, setUpdateItemId] = useState("");





  const calendarGet = async () => {

    const cookieData = getCookie("member")
    const loginId = cookieData.id;

    const url = `http://${EC2_URL}:8090/admin/event/eventList/${loginId}`;
    const res = await jwtAxios.get(url);

    const data = res.data.event;
    
    setSchedule(data)

  }


  
  useEffect(()=>{

    calendarGet()

  },[isModal])


  const handleDateClick = (arg) => {

    console.log(arg)
  }



  const showContent = (arg) => {
    console.log(arg)
    const eventData = arg.el.fcSeg.eventRange.def.extendedProps // content, memberId 내가 넣어놓은 event 객체의 필드값들
    
    const title = arg.el.fcSeg.eventRange.def.title

    const start = arg.el.fcSeg.eventRange.range.start

    const end = arg.el.fcSeg.eventRange.range.end

    const eventId = arg.el.fcSeg.eventRange.def.publicId


    setShowData({

      eventId: eventId,
      title: title,
      content: eventData.content,
      start: start.toLocaleString(),
      end: end.toLocaleString()

    })

    setIsInfo(true)


  }



  const infoUpdate = (eventId) => {
    
    setUpdateItemId(eventId);

    setIsUpdateModal(true);

  }



  const infoDelete = async (eventId) => {

    const isDelete = window.confirm("일정을 삭제하시겠습니까?");

    if (!isDelete) {
      return;
    }


    const url = `http://${EC2_URL}:8090/admin/event/delete/${eventId}`;

    await jwtAxios.delete(url);
  
    calendarGet()
  }


  console.log(schedule)
  console.log(showData)


  return (

    <>
      <div className='admin-event'>
        <div className='admin-event-con'>

          {isModal && <EventModalA setIsModal={setIsModal}/>}

          {isUpdateModal && <EventUpdateModal setIsUpdateModal={setIsUpdateModal} updateItemId={updateItemId}/>}


          <div id='calendar-container'>
            <FullCalendar 
              initialView='dayGridMonth' // 달력
              plugins={[ dayGridPlugin, interactionPlugin ]} // 설정 // 플러그 추가로 필요한 기능 사용
              weekends={true} // 주말 포함/비포함
              
              locale={'ko'} // 언어

              // 달력에 표시
              events={schedule} // title, date 외의 객체의 다른 필드값들은 알아서 무시 되는 것 같다.
              // events={[ 
              //   { title: 'event 1', date: '2025-03-06' },
              //   { title: 'event 2', date: '2025-03-08' }
              // ]}
              
              // 날짜 칸 click event
              dateClick={(arg)=>{handleDateClick(arg)}} // arg // date 객체 

              // 이벤트(달력에 표시된 줄) 클릭 이벤트
              eventClick={(arg)=>{showContent(arg)}}


              // 달력 상단 바 커스텀
              headerToolbar={{
                start: "addSchedule", // 버튼들 추가는 띄어쓰기로 구분 및 추가 ->  : "버튼1 버튼2 버튼3"
                center: "title",
                // end: "dayGridMonth dayGridWeek",
                // start end center // 버튼 위치
              }}

              // 버튼 커스텀하기
              customButtons={{
                addSchedule: {
                  text: "일정 등록",
                  click: () => {
                    setIsModal(true)
                  }
                }
              }}
              
              
              height={"600px"}
            />
          </div>

          
          <div className='calendar-day-info'>
            {isInfo && 
              <>
                <div className='calendar-info-head'>
                  <h3>{showData.title}</h3>
                  <span>
                    {showData.start} ~ {showData.end}
                  </span>
                </div>
                
                <p>
                  {showData.content}
                </p>


                <div className='calendar-info-btns'>
                  <button onClick={()=>{infoUpdate(showData.eventId)}}>수정</button>
                  <button onClick={()=>{infoDelete(showData.eventId)}}>삭제</button>
                </div>
              </>  
            }
          </div>
          
          
          
        </div>
      </div>
    </>
  )
}

export default CalendarCompo
import { Outlet } from 'react-router-dom';
import Header from '../../components/basic/Header';
import Footer from '../../components/basic/Footer';
import { useState, useEffect, useRef } from 'react'; // useRef 추가
import { useSelector } from 'react-redux';
import jwtAxios from '../../util/jwtUtils';
import ScrollTop from '../../util/ScrollTop';
import { S3URL } from '../../util/constant';
import { EC2_URL } from '../../constans';

const DefaultLayout = () => {
  const [chatbotModal, setChatbotModal] = useState(false);
  const [messages, setMessages] = useState([]); // 채팅 메시지 목록
  const [question, setQuestion] = useState(""); // 입력 필드 값
  const isLogin = useSelector((state) => state.loginSlice);
  const chatContentRef = useRef(null); // 메시지 컨테이너 참조

  const chatbotModalFn = async () => {
    // 모달을 여는 과정에서 상태를 먼저 변경
    const newChatbotModalState = !chatbotModal;
    setChatbotModal(newChatbotModalState);
  
    // 만약 모달이 열릴 때만 서버 요청을 보내도록 수정
    if (newChatbotModalState) {
      try {
        // 서버로 "안녕" 메시지 전송
        const response = await jwtAxios(`http://${EC2_URL}:8090/chatbot`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          data: JSON.stringify({message:"안녕"}), // "안녕" 메시지를 서버로 전송
        });
  
        // 챗봇의 응답을 받아서 화면에 표시
        const initialBotMessage = `
          <div class='head-img'><img src='/images/favicon3.png'></div>
          <div class='message'>${response.data.message.answer.content.replace(/\\n/g, "<br>")}</div>
          <div class='time'>${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
        `;
        setMessages((prev) => [...prev, { text: initialBotMessage, sender: 'bot' }]);
      } catch (error) {
        console.error("Error fetching chatbot response:", error);
      }
    }
  };

  // 메시지 전송
  const sendMessage = async (message) => {
    if (!message || message.trim().length < 2) return;

    // 사용자 메시지 추가
    const userMessage = `
        <div class='head-img'>${isLogin.NickName}</div>
        <div class='msg'>
          <div class='time'>${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
          <div class='message'>${message}</div>
        </div>
        `;
    setMessages((prev) => [...prev, { text: userMessage, sender: 'user' }]);

    // 서버로 메시지 전송
    try {
      const response = await jwtAxios(`http://${EC2_URL}:8090/chatbot`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        data: JSON.stringify({ message }), // message를 JSON 형식으로 전송
      });
    
      const responseText = response.data.message.answer; // 수정된 응답에서 메시지 가져오기
      console.log(responseText)
      let botMessage= ``;
      if(responseText.mentor === null && responseText.mentorList === null){
        botMessage += `
          <div class='head-img'><img src='/images/favicon3.png'></div>
          <div class='message'>${responseText.content.replace(/\\n/g, "<br>")}</div>
          <div class='time'>${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
          `;
      }else if(responseText.mentor === null){
        botMessage += `
            <div class='head-img'><img src='/images/favicon3.png'></div>
            <div class='message'>
              <h1>${responseText.content.replace(/\\n/g, "<br>")}</h1>
              <div class='message-mentorList'>
              ${responseText.mentorList.map((el, index) => {
                let rankClass = 'black'; // 기본 색상
                if (index === 0) {
                    rankClass = 'gold'; // 1등
                } else if (index === 1) {
                    rankClass = 'silver'; // 2등
                } else if (index === 2) {
                    rankClass = 'bronze'; // 3등
                }
                return `
                    <div class="mentorList-langk">
                        <span class="${rankClass}">${index + 1}등</span>
                        <div class="mentorList-langk-top">
                            <div class="mentorList-langk-img">
                                ${el.attachFile === 1 ? (
                                    `<img 
                                    src="${S3URL}${el.newImgName}"
                                    alt="프로필" class="profile" />`
                                ) : (
                                    `<img 
                                    src="/images/profile.png"
                                    alt="프로필" class="profile" />`
                                )}
                            </div>
                            <div class="mentorList-langk-nickName">${el.nickName}</div>
                        </div>
                        <div class="mentorList-langk-bottom">
                            <span>리뷰: ${el.replyCount}</span>
                            <span>조회: ${el.viewCount}</span>
                        </div>
                    </div>
                `;
              }).join('')}
              </div>
            </div>
            <div class='time'>${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
        `
      }else if(responseText.mentorList === null){
        const mentor = responseText.mentor
        botMessage += `
            <div class='head-img'><img src='/images/favicon3.png'></div>
            <div class='message'>
              <h1>${responseText.content.replace(/\\n/g, "<br>")}</h1>
              <div class='message-mentor'>
                <div class='message-mentor-top'>
                    <div class='message-left'>
                    ${mentor.attachFile === 1 ? (
                        `<img 
                        src="${S3URL}${mentor.newImgName}"
                        alt="프로필" class="profile" />`
                    ) : (
                        `<img 
                        src="/images/profile.png"
                        alt="프로필" class="profile" />`
                    )}
                    </div>
                    <div class='message-mentor-nickName'>${mentor.nickName}</div>
                </div>
                <div class='message-mentor-body'>
                  <span>이름: ${mentor.userName}</span>
                  <span>경력: ${mentor.career}</span>
                  <span>리뷰: ${mentor.replyCount}</span>
                  <span>조회: ${mentor.viewCount}</span>
                </div>
                <div class='message-mentor-item'>
                    ${mentor.itemEntities.length === 0 ? (
                      ``
                    ):(
                      `<span>멘토의 등록 상품</span>`
                    )}
                  ${mentor.itemEntities.map(el => 
                    `<div>
                      <span>${el.category}:</span>
                      <span>${el.itemPrice}원</span>
                    </div>
                    `
                  ).join('')}
                </div>
                <div class='message-mentor-bottom'>
                    <div>
                      <span>Mail.</span>
                      <span>${mentor.userEmail}</span>
                    </div>
                    <div>
                      <span>Tel.</span>
                      <span>${mentor.phone}</span>
                    </div>
                </div>
              </div>
            </div>
            <div class='time'>${new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
        `
      }
    
      // 챗봇 응답 추가
      setMessages((prev) => [...prev, { text: botMessage, sender: 'bot' }]);
    } catch (error) {
      console.error("메시지 전송 실패:", error);
    }

    setQuestion(""); // 입력 필드 리셋
  };

  // 엔터 키 입력 처리
  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      sendMessage(question);
    }
  };

  // 메시지 추가 시 자동 스크롤
  useEffect(() => {
    if (chatContentRef.current) {
      chatContentRef.current.scrollTop = chatContentRef.current.scrollHeight;
    }
  }, [messages]); // messages가 변경될 때마다 실행

  return (
    <div className='wrapper'>
      <Header />
      <ScrollTop />
      <div className='default-layout'>
        <div className='default-layout-con'>
          <Outlet />
          {isLogin.id&&(
            <div className="chatbot" onClick={()=>{
              chatbotModalFn();
            }}>
              <div className="chatbot-icon">💬</div>
            </div>
          )}

          {/* 챗봇 창 */}
          {chatbotModal && (
            <div className="chatbot-con">
              <div id="chat-disp">
                <div id="chat-disp-con">
                  <div id="chat-header">
                    <span>
                      RESUMEBOOST
                    </span>
                    <span className="btn-wrap">
                      <button type="button" id="close" onClick={() => { 
                        chatbotModalFn(); 
                        setMessages([]); // X 버튼 클릭 시 메시지 초기화
                      }}>X</button>
                    </span>
                  </div>
                  <div id="chat-content" ref={chatContentRef}> {/* ref 추가 */}
                    {messages.map((msg, index) => (
                      <div key={index} className={`data-con ${msg.sender}`}>
                        <div dangerouslySetInnerHTML={{ __html: msg.text }} />
                      </div>
                    ))}
                  </div>
                  <div id="chat-question" className="flex between">
                    <input type="text" id="question" 
                      value={question} 
                      onChange={(e) => setQuestion(e.target.value)} 
                      onKeyDown={handleKeyDown}
                      placeholder="질문을 입력하세요" 
                    />
                    <button id="btn-msg-send" onClick={() => sendMessage(question)}>
                      전송
                    </button>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default DefaultLayout;
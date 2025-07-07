import React from 'react'
import { Link } from 'react-router-dom'

const Index = () => {
  return (
    <>
      <div className="index-container">
        <h1 className="index-title">
          이제, 1분만에 회원가입하고 <br /> 내게 딱 맞는 취업 멘토를 만나보세요!
        </h1>
        <div className="cards-container">
          {/* 왼쪽 카드 */}
          <Link to={'/auth/join'} className="card card-green">
            <div className="card-content">
              <p className="card-subtitle-green">취업관련 컨설팅 멘토를 직접 찾아서 견적받고 싶다면?</p>
              <h2 className="card-title-green">1분만에 회원가입하고<br />나에게 맞는 멘토 찾아보기</h2>
            </div>
            <div className="card-footer">
              <div className="arrow-button-green"><span style={{color: '#E0F8E0'}}>-</span>〉</div>
              <img src="/images/index1.png" alt="paper plane" className="card-icon" />
            </div>
          </Link>

          {/* 오른쪽 카드 */}
          <Link to={'/auth/join/mentor'} className="card card-blue">
            <div className="card-content">
              <p className="card-subtitle-blue">취준 선배로써 or 경력자로써 부수입과 도움을 주고 싶다면?</p>
              <h2 className="card-title-blue">1분만에 멘토가입하고<br />컨설팅 해주기</h2>
            </div>
            <div className="card-footer">
              <div className="arrow-button-blue"><span style={{color: '#E0F0FF'}}>-</span>〉</div>
              <img src="/images/index2.png" alt="user folder" className="card-icon" />
            </div>
          </Link>
        </div>
        <Link to={'/main'} className='index-button'>
          <span>RESUMEBOOST 둘러보기</span>
        </Link>
      </div>
    </>
  )
}

export default Index
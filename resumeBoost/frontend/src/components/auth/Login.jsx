import React, { useState } from "react"
import { useNavigate } from "react-router-dom"
import useCustomLogin from "./../../hook/useCustomLogin"
import KakaoLogin from "./KakaoLogin"
import { cartData } from "../../slice/cartSlice"
import { useDispatch } from "react-redux"

const initState = {
  userEmail: "",
  userPw: "",
}

const Login = () => {
  const [loginParam, setLoginParam] = useState({ ...initState })
  const [errorMessage, setErrorMessage] = useState("") // 에러 메시지 상태 추가
  const { doLogin, moveToPath } = useCustomLogin()
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const handleChange = (e) => {
    loginParam[e.target.name] = e.target.value
    setLoginParam({ ...loginParam })
  }

  const handleClickLogin = (e) => {
    doLogin(loginParam).then((data) => {
      if (data.error) {
        setErrorMessage("이메일과 비밀번호를 확인해주세요") // 에러 메시지 설정
      } else {
        setErrorMessage("") // 성공 시 에러 메시지 초기화
        moveToPath("/main")
        dispatch(cartData(data.id))
      }
    })
  }

  // 엔터 키 입력 처리
  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleClickLogin(e) // 엔터 키로 로그인 실행
    }
  }

  return (
    <div className='login'>
      <div className='login-header'>
        <h1>
          <img
            src='/images/logo2.jpg'
            alt=''
            onClick={() => navigate("/main")}
          />
        </h1>
        <h3>로그인</h3>
        <button
          type='button'
          onClick={() => navigate(-1)}
          className='back-button'
        >
          〈
        </button>
      </div>
      <div className='login-con'>
        <div className='login-container'>
          <div>
            <label>이메일</label>
            <input
              type='email'
              name='userEmail'
              required
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              value={loginParam.userEmail}
            />
          </div>
          <div>
            <label>비밀번호</label>
            <input
              type='password'
              name='userPw'
              required
              onChange={handleChange}
              onKeyDown={handleKeyDown}
              value={loginParam.userPw}
            />
          </div>
        </div>
        {errorMessage && (
          <div className='error-message'>
            <span>{errorMessage}</span> {/* 에러 메시지 출력 */}
          </div>
        )}
        <button className='login-footer' onClick={handleClickLogin}>
          로그인
        </button>
        <button className='login-footer-kakao'>
          <KakaoLogin />
        </button>
      </div>
      <div className='join-navigate'>
        <span>회원가입이 필요하신가요?</span>
        <span
          className='join-navigate-span'
          onClick={() => navigate("/auth/join")}
        >
          회원가입
        </span>
      </div>
    </div>
  )
}

export default Login

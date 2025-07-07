import axios from "axios"
import React, { useState, useEffect } from "react"
import { useLocation, useNavigate } from "react-router-dom"
import { EC2_URL } from "../../constans"

const Join = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const [isMentor, setIsMentor] = useState(
    location.pathname.includes("/mentor")
  )

  const [formData, setFormData] = useState({
    userEmail: "",
    userPw: "",
    userName: "",
    nickName: "",
    address: "서울",
    age: 10,
    phone: "",
  })

  const [emailStatus, setEmailStatus] = useState("")
  const [nickNameStatus, setNickNameStatus] = useState("")
  const [isEmailChecked, setIsEmailChecked] = useState(false) // 이메일 중복 확인 상태
  const [isNickNameChecked, setIsNickNameChecked] = useState(false) // 닉네임 중복 확인 상태
  const [phoneStatus, setPhoneStatus] = useState("") // 전화번호 상태 추가
  const [careerStatus, setCareerStatus] = useState("") // 경력 상태 추가

  useEffect(() => {
    setIsMentor(location.pathname.includes("/mentor"))
    setFormData((prev) => ({
      ...prev,
      career: location.pathname.includes("/mentor") ? prev.career : "",
    }))
  }, [location.pathname])

  const handleInputChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))

    if (name === "userEmail") {
      setEmailStatus("")
      setIsEmailChecked(false)
    }

    if (name === "nickName") {
      setNickNameStatus("")
      setIsNickNameChecked(false)
    }

    if (name === "phone") {
      const phoneRegex = /^010-\d{4}-\d{4}$/
      if (!phoneRegex.test(value)) {
        setPhoneStatus("올바른 전화번호 형식이 아닙니다. (예: 010-0000-0000)")
      } else {
        setPhoneStatus("")
      }
    }

    // 경력 입력에 대한 유효성 검사
    if (name === "career") {
      const careerRegex = /^(?:[1-5]?\d|60)년$/
      if (!careerRegex.test(value)) {
        setCareerStatus(
          '경력은 "숫자 + 년" 형식이어야 하며, 최대 60년까지 입력할 수 있습니다. (예: 10년)'
        )
      } else {
        setCareerStatus("")
      }
    }
  }

  const toggleUserType = () => {
    setIsMentor(!isMentor)
    setFormData((prev) => ({
      ...prev,
      career: !isMentor ? "" : prev.career,
    }))
  }

  const handleEmailCheck = async () => {
    if (!formData.userEmail) {
      setEmailStatus("이메일을 입력해 주세요.")
      setIsEmailChecked(false)
      return // 이메일이 비어있으면 함수 종료
    }
    // 이메일 형식이 올바른지 확인
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    if (!emailRegex.test(formData.userEmail)) {
      setEmailStatus("올바른 이메일 형식이 아닙니다. (예: example@example.com)")
      setIsEmailChecked(false)
      return // 이메일 형식이 잘못되었으면 함수 종료
    }
    try {
      const response = await axios.post(
        `http://${EC2_URL}:8090/member/checkEmail`,
        formData
      )
      if (response.data.exists) {
        setEmailStatus("이미 존재하는 이메일입니다.")
        setIsEmailChecked(false)
      } else {
        setEmailStatus("사용 가능한 이메일입니다.")
        setIsEmailChecked(true) // 이메일 중복 확인 완료
      }
    } catch (error) {
      console.error("Email check error:", error)
    }
  }

  const handleNickNameCheck = async () => {
    if (!formData.nickName) {
      setNickNameStatus("닉네임을 입력해 주세요.")
      setIsNickNameChecked(false)
      return // 닉네임이 비어있으면 함수 종료
    }
    try {
      const response = await axios.post(
        `http://${EC2_URL}:8090/member/checkNickName`,
        formData
      )
      if (response.data.exists) {
        setNickNameStatus("이미 존재하는 닉네임입니다.")
        setIsNickNameChecked(false)
      } else {
        setNickNameStatus("사용 가능한 닉네임입니다.")
        setIsNickNameChecked(true) // 닉네임 중복 확인 완료
      }
    } catch (error) {
      console.error("Nickname check error:", error)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (phoneStatus) {
      setPhoneStatus("전화번호 형식을 확인해주세요.")
      return
    }

    if (isMentor && !formData.career.trim()) {
      alert("멘토 회원은 경력 입력이 필수입니다.")
      return
    }

    const endpoint = isMentor
      ? `http://${EC2_URL}:8090/member/insert/mentor`
      : `http://${EC2_URL}:8090/member/insert`

    try {
      const response = await axios.post(endpoint, formData)

      if (response.status === 200) {
        alert("회원가입이 완료되었습니다.")
        navigate("/auth/login")
      }
    } catch (error) {
      alert("회원가입 중 오류가 발생했습니다.")
      console.error("Error:", error)
    }
  }

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleEmailCheck(e)
    }
  }

  const nickHandleKeyDown = (e) => {
    if (e.key === "Enter") {
      handleNickNameCheck(e)
    }
  }

  return (
    <div className='join'>
      <div className='join-header'>
        <h1>
          <img
            src='/images/logo2.jpg'
            alt=''
            onClick={() => navigate("/main")}
          />
        </h1>
        <button
          type='button'
          onClick={() => navigate(-1)}
          className='back-button'
        >
          〈
        </button>
        <button
          type='button'
          onClick={toggleUserType}
          className='transfer-button'
        >
          {isMentor ? "일반 회원가입하기" : "멘토로 회원가입하기"}
        </button>
      </div>
      <div className='join-con'>
        <form onSubmit={handleSubmit}>
          <div className='join-conteiner'>
            <div>
              <label>이메일</label>
              <input
                type='email'
                name='userEmail'
                value={formData.userEmail}
                onChange={handleInputChange}
                onKeyDown={handleKeyDown}
                required
              />
              <div className='status'>
                <div
                  className={`status-message ${
                    emailStatus === "사용 가능한 이메일입니다."
                      ? "success"
                      : "error"
                  }`}
                >
                  {emailStatus && <span>{emailStatus}</span>}
                </div>
                <button type='button' onClick={handleEmailCheck}>
                  중복 확인
                </button>
              </div>
            </div>
            <div>
              <label>비밀번호</label>
              <input
                type='password'
                name='userPw'
                value={formData.userPw}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>이름</label>
              <input
                type='text'
                name='userName'
                value={formData.userName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label>닉네임</label>
              <input
                type='text'
                name='nickName'
                value={formData.nickName}
                onChange={handleInputChange}
                onKeyDown={nickHandleKeyDown}
                required
              />
              <div className='status'>
                <div
                  className={`status-message ${
                    nickNameStatus === "사용 가능한 닉네임입니다."
                      ? "success"
                      : "error"
                  }`}
                >
                  {nickNameStatus && <span>{nickNameStatus}</span>}
                </div>
                <button type='button' onClick={handleNickNameCheck}>
                  중복 확인
                </button>
              </div>
            </div>
            <div>
              <label>지역</label>
              <select
                name='address'
                value={formData.address}
                onChange={handleInputChange}
                required
              >
                <option value='서울'>서울</option>
                <option value='경기도'>경기도</option>
                <option value='강원도'>강원도</option>
                <option value='충청북도'>충청북도</option>
                <option value='충청남도'>충청남도</option>
                <option value='경상북도'>경상북도</option>
                <option value='경상남도'>경상남도</option>
                <option value='전라남도'>전라남도</option>
                <option value='전라남도'>전라남도</option>
                <option value='제주도'>제주도</option>
              </select>
            </div>
            <div>
              <label>나이</label>
              <select
                name='age'
                id='age-select'
                value={formData.age}
                onChange={handleInputChange}
              >
                <option value='10'>10대</option>
                <option value='20'>20대</option>
                <option value='30'>30대</option>
                <option value='40'>40대</option>
                <option value='50'>50대</option>
                <option value='60'>60대</option>
                <option value='70'>70대</option>
              </select>
            </div>
            <div>
              <label>전화번호</label>
              <input
                type='text'
                name='phone'
                value={formData.phone}
                onChange={handleInputChange}
                required
                placeholder='010-0000-0000'
              />
              <div className='status-message'>
                {phoneStatus && <span>{phoneStatus}</span>}
              </div>
            </div>
            {isMentor && (
              <div>
                <label>경력</label>
                <input
                  type='text'
                  name='career'
                  value={formData.career}
                  onChange={handleInputChange}
                  required
                  placeholder='경력을 입력해주세요.'
                />
                <div className='status-message'>
                  {careerStatus && <span>{careerStatus}</span>}
                </div>
              </div>
            )}
            <button
              type='submit'
              className='join-footer'
              disabled={!isEmailChecked || !isNickNameChecked} // 이메일과 닉네임 중복 확인이 완료되어야만 버튼 활성화
            >
              {isMentor ? "멘토 회원가입" : "일반 회원가입"}
            </button>
          </div>
        </form>
      </div>
      <div className='join-navigate'>
        <span>이미 회원이신가요?</span>
        <span
          className='join-navigate-span'
          onClick={() => navigate("/auth/login")}
        >
          로그인
        </span>
      </div>
    </div>
  )
}

export default Join

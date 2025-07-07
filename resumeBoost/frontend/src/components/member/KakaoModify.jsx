import React, { useState, useEffect } from "react"
import { useDispatch, useSelector } from "react-redux"
import { useLocation, useNavigate } from "react-router-dom"
import jwtAxios from "../../util/jwtUtils"
import { login } from "../../slice/loginSlice"
import { EC2_URL } from "../../constans"

const KakaoModify = () => {
  const isLogin = useSelector((state) => state.loginSlice)
  const navigate = useNavigate()
  const location = useLocation()
  const dispatch = useDispatch()
  const [isMentor, setIsMentor] = useState(
    location.pathname.includes("/mentor")
  )

  const [formData, setFormData] = useState({
    userEmail: isLogin.userEmail,
    userPw: "",
    userName: "",
    nickName: "",
    address: "",
    age: 0,
    phone: "",
  })

  const [nickNameStatus, setNickNameStatus] = useState("")
  const [isNickNameChecked, setIsNickNameChecked] = useState(false) // 닉네임 중복 확인 상태
  const [phoneStatus, setPhoneStatus] = useState("") // 전화번호 상태 추가

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
  }

  const toggleUserType = () => {
    setIsMentor(!isMentor)
    setFormData((prev) => ({
      ...prev,
      career: !isMentor ? "" : prev.career,
    }))
  }

  const handleNickNameCheck = async () => {
    try {
      const response = await jwtAxios.post(
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

    const updatedFormData = {
      ...formData,
      role: isMentor ? "MENTOR" : "MEMBER", // isMentor가 true이면 MENTOR, false이면 MEMBER
    }

    const url = `http://${EC2_URL}:8090/member/kakaoJoin`

    try {
      console.log("Form Data:", updatedFormData)
      const response = await jwtAxios.post(url, updatedFormData)

      if (response.status === 200) {
        alert("회원수정이 완료되었습니다.")
        navigate("/main")
        const updatedMember = {
          NickName: formData.nickName, // member의 nickName
          id: isLogin.id, // member의 id
          social: false, // member의 social
          userEmail: formData.userEmail, // member의 userEmail
          role: "ROLE_" + formData.role, // member의 role
          accessToken: isLogin.accessToken,
          refreshToken: isLogin.refreshToken,
        }
        dispatch(login(updatedMember))
      }
    } catch (error) {
      alert("회원수정 중 오류가 발생했습니다.")
      console.error("Error:", error)
    }
  }

  return (
    <div className='modify'>
      <div className='modify-header'>
        <h3 className='join-choice'>회원정보 추가작성</h3>
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
                id='userEmail'
                type='email'
                name='userEmail'
                value={isLogin.userEmail}
                onChange={handleInputChange}
                required
                readOnly
              />
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
              <label>주소</label>
              <input
                type='text'
                name='address'
                value={formData.address}
                onChange={handleInputChange}
                required
              />
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
                  placeholder='경력을 입력해주세요'
                />
              </div>
            )}
            <button
              type='submit'
              className='join-footer'
              disabled={!isNickNameChecked} // 이메일과 닉네임 중복 확인이 완료되어야만 버튼 활성화
            >
              회원가입
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default KakaoModify

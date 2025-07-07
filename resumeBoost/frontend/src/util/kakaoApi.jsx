import axios from "axios"
import { EC2_URL } from "../constans"

// REST API 키
const rest_api_key = `0eaf07321c1e0fbf250dd675816c0407`
// redirect 주소
const redirect_uri = `http://${EC2_URL}/member/kakao`

// 인가 코드 받는 경로
const auth_code_path = `https://kauth.kakao.com/oauth/authorize`

const access_token_url = `https://kauth.kakao.com/oauth/token`

export const getKakaoLoginLink = () => {
  const kakaoURL = `${auth_code_path}?client_id=${rest_api_key}&redirect_uri=${redirect_uri}&response_type=code`

  return kakaoURL
}

export const getAccessToken = async (authCode) => {
  const header = {
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
  }
  const params = {
    grant_type: "authorization_code",
    client_id: rest_api_key,
    redirect_uri: redirect_uri,
    code: authCode,
  }

  const res = await axios.post(access_token_url, params, header)

  const accessToken = res.data.access_token

  return accessToken
}

export const getMemberWithAccessToken = async (accessToken) => {
  const res = await axios.get(
    `http://${EC2_URL}:8090/api/member/kakao?accessToken=${accessToken}`
  )

  return res.data
}

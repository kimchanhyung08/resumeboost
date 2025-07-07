import axios from "axios"
import { getCookie, setCookie } from "./cookieUtil"
import { EC2_URL } from "../constans"

// axios 대신 jwtAxios 사용해서 토큰검증 후 서버에 요청,응답 처리
const jwtAxios = axios.create()

const host = `http://${EC2_URL}:8090`

// JWT 토큰 재발급 함수
const refreshJWT = async (accessToken, refreshToken) => {
  const header = { headers: { "Authorization": `Bearer ${accessToken}` } }
  const res = await axios.get(
    `${host}/api/member/refresh?refreshToken=${refreshToken}`,
    header
  )

  return res.data
}

//before request
const beforeReq = (config) => {
  // console.log("before request...")

  const memberInfo = getCookie("member")
  if (!memberInfo) {
    console.log("MEMBER NOT FOUND")
    return Promise.reject({
      response: { data: { error: "REQUIRE_LOGIN" } },
    })
  }
  const { accessToken } = memberInfo

  // Authorization 헤더 처리
  config.headers.Authorization = `Bearer ${accessToken}`
  return config
}

//fail request
const requestFail = (err) => {
  console.log("requesr error...")
  return Promise.reject(err)
}

//before return response
const beforeRes = async (res) => {
  // console.log("before return response...")

  const data = res.data

  // console.log(data)

  if (data && data.error === "ERROR_ACCESS_TOKEN") {
    const memberCookieValue = getCookie("member")

    const result = await refreshJWT(
      memberCookieValue.accessToken,
      memberCookieValue.refreshToken
    )

    memberCookieValue.accessToken = result.accessToken
    memberCookieValue.refreshToken = result.refreshToken

    setCookie("member", JSON.stringify(memberCookieValue), 1)

    //원래의 호출
    const originalRequest = res.config
    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`

    return await axios(originalRequest)
  }
  return res
}

//fail response
const responseFail = (err) => {
  console.log("response fail error...")
  return Promise.reject(err)
}

jwtAxios.interceptors.request.use(beforeReq, requestFail)
jwtAxios.interceptors.response.use(beforeRes, responseFail)

export default jwtAxios

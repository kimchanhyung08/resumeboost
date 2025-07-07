import React, { useEffect } from "react"
import { useSearchParams } from "react-router-dom"
import { getAccessToken, getMemberWithAccessToken } from "../../util/kakaoApi"
import { useDispatch } from "react-redux"
import { login } from "./../../slice/loginSlice"
import useCustomLogin from "../../hook/useCustomLogin"

const KakaoRedirectPage = () => {
  const [searchParams] = useSearchParams()
  const dispatch = useDispatch()
  const { moveToPath } = useCustomLogin()

  const authCode = searchParams.get("code")

  useEffect(() => {
    getAccessToken(authCode).then((accessToken) => {
      console.log(accessToken)

      getMemberWithAccessToken(accessToken).then((memberInfo) => {
        console.log(memberInfo)
        dispatch(login(memberInfo))

        if (memberInfo && !memberInfo.social) {
          moveToPath("/main")
        } else {
          moveToPath("/member/modify")
        }
      })
    })
  }, [authCode])

  return (
    <div>
      <div>Kakao Login Redirect</div>
      <div>{authCode}</div>
    </div>
  )
}

export default KakaoRedirectPage

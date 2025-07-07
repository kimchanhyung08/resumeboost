import React, { Suspense } from "react"
import MemberDetailPage from "../pages/member/MemberDetailPage"
import MentorDetailPage from "./../pages/member/MentorDetailPage"
import Index from "../components/member/Index"
import MentorListPage from "./../pages/member/MentorListPage"
import KakaoRedirectPage from "../components/member/KakaoRedirectPage"
import KakaoModify from "../components/member/KakaoModify"
import { useSelector } from "react-redux"
import { Navigate, useParams } from "react-router-dom"

const Loading = <div className='loading'>Loading...</div>

const ProtectedRoute = ({ children, requiredId }) => {
  const isLogin = useSelector((state) => state.loginSlice)
  console.log(isLogin.id)
  console.log(requiredId)
  console.log(isLogin.id !== requiredId)

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to='/auth/login' replace />
  }

  if (
    requiredId &&
    String(isLogin.id) !== requiredId
  ) {
    return <Navigate to='/main' replace />
  }

  return children
}

// useParams 훅을 사용하는 컴포넌트
const MemberDetailWrapper = () => {
  const { id } = useParams()

  return (
    <Suspense fallback={Loading}>
      <ProtectedRoute requiredId={id}>
        <MemberDetailPage />
      </ProtectedRoute>
    </Suspense>
  )
}

const toMemberRouter = () => {
  return [
    {
      path: "",
      element: (
        <Suspense fallback={Loading}>
          <Index />
        </Suspense>
      ),
    },
    {
      path: "memberDetail/:id",
      element: <MemberDetailWrapper />, // 컴포넌트로 감싸서 useParams()를 호출하도록 변경
    },
    {
      path: "mentorDetail/:id",
      element: (
        <ProtectedRoute>
          <Suspense fallback={Loading}>
            <MentorDetailPage />
          </Suspense>
        </ProtectedRoute>
      ),
    },
    {
      path: "mentorList",
      element: (
        <Suspense fallback={Loading}>
          <MentorListPage />
        </Suspense>
      ),
    },
    {
      path: "kakao",
      element: (
        <Suspense fallback={Loading}>
          <KakaoRedirectPage />
        </Suspense>
      ),
    },
    {
      path: "modify",
      element: (
        <Suspense fallback={Loading}>
          <KakaoModify />
        </Suspense>
      ),
    },
  ]
}

export default toMemberRouter

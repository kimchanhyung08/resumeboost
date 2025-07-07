import React, { Suspense } from "react"
import Index from "../components/member/Index"
import { useSelector } from "react-redux"
import MyCartPage from "../pages/cart/MyCartPage"
import { Navigate, useParams } from "react-router-dom"

const Loading = <div className='loading'>Loading...</div>

const ProtectedRoute = ({ children, requiredId }) => {
  const isLogin = useSelector((state) => state.loginSlice)

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to='/auth/login' replace />
  }

  if (requiredId && String(isLogin.id) !== requiredId) {
    return <Navigate to='/main' replace />
  }

  return children
}

const Wrapper = () => {
  const { id } = useParams()

  return (
    <Suspense fallback={Loading}>
      <ProtectedRoute requiredId={id}>
        <MyCartPage />
      </ProtectedRoute>
    </Suspense>
  )
}

const toCartRouter = () => {
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
      path: "myCartList/:id",
      element: <Wrapper />,
    },
  ]
}

export default toCartRouter

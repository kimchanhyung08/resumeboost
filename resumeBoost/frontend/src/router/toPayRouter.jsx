import React, { Suspense } from "react"
import { useSelector } from "react-redux"
import { Navigate, useParams } from "react-router-dom"
import AddPayPage from "./../pages/pay/AddPayPage"
import Index from "../components/member/Index"
import MyPayPage from "../pages/pay/MyPayPage"

const Loading = <div className='loading'>Loading...</div>

const ProtectedRoute = ({ children, requiredId }) => {
  const isLogin = useSelector((state) => state.loginSlice)

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to='/auth/login' replace />
  }

  if (requiredId && isLogin.id !== requiredId) {
    return <Navigate to='/main' replace />
  }

  return children
}

const Wrapper = () => {
  const { id } = useParams()

  return (
    <Suspense fallback={Loading}>
      <ProtectedRoute requiredId={id}>
        <AddPayPage />
      </ProtectedRoute>
    </Suspense>
  )
}
const Wrapper2 = () => {
  const { id } = useParams()

  return (
    <Suspense fallback={Loading}>
      <ProtectedRoute requiredId={id}>
        <MyPayPage />
      </ProtectedRoute>
    </Suspense>
  )
}

const toPayRouter = () => {
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
      path: "addPay/:id",
      element: <Wrapper />,
    },
    {
      path: "myPay/:id",
      element: <Wrapper2 />,
    },
  ]
}

export default toPayRouter

import React, { Suspense } from "react"
import ItemPage from "./../pages/item/ItemPage"
import ItemInsertPage from "../pages/item/ItemInsertPage"
import { useSelector } from "react-redux"
import { Navigate } from "react-router-dom"

const Loading = <div className='loading'>Loading...</div>

const ProtectedRoute = ({ children, requiredRole, requiredAdmin }) => {
  const isLogin = useSelector((state) => state.loginSlice)

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to='/auth/login' replace />
  }

  if (
    requiredRole &&
    isLogin.role !== requiredRole &&
    requiredAdmin &&
    isLogin.role !== requiredAdmin
  ) {
    return <Navigate to='/main' replace />
  }

  return children
}

const toItemRouter = () => {
  return [
    {
      path: "",
      element: (
        <Suspense fallback={Loading}>
          <ItemPage />
        </Suspense>
      ),
    },
    {
      path: "insert",
      element: (
        <ProtectedRoute
          requiredRole={"ROLE_MENTOR"}
          requiredAdmin={"ROLE_ADMIN"}
        >
          <Suspense fallback={Loading}>
            <ItemInsertPage />
          </Suspense>
        </ProtectedRoute>
      ),
    },
  ]
}

export default toItemRouter

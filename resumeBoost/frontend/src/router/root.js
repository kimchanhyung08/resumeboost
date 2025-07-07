import React, { lazy, Suspense } from "react"
import { createBrowserRouter, Navigate, redirect } from "react-router-dom"
import DefaultLayout from "../layouts/basic/DefaultLayout"
import IndexPage from "../pages/IndexPage"
import toBoardRouter from "./toBoardRouter"
import toAuthRouter from "./toAuthRouter"
import AuthLayout from "../layouts/auth/AuthLayout"
import toItemRouter from "./toItemRouter"
import toMemberRouter from "./toMemberRouter"
import toAdminRouter from "./toAdminRouter"
import AdminLayout from "../layouts/admin/AdminLayout"
import InquiryPage from "../pages/inquiry/InquiryPage"
import { useSelector } from "react-redux"
import toCartRouter from "./toCartRouter"
import Work24Page from "../pages/work24/Work24Page"
import toPayRouter from "./toPayRouter"

const MainPage = lazy(() => import("../pages/basic/MainPage"))

const Loading = <div className='loading'>Loading...</div>

const ProtectedRoute = ({ children, requiredRole }) => {
  const isLogin = useSelector((state) => state.loginSlice)

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to='/auth/login' replace />
  }

  // 역할이 requiredRole과 일치하지 않으면 접근 불가
  if (requiredRole && isLogin.role !== requiredRole) {
    return <Navigate to='/main' replace />
  }

  return children
}

const root = createBrowserRouter([
  {
    path: "/",
    element: (
      <Suspense fallback={Loading}>
        <IndexPage /> {/* 시작 페이지 */}
      </Suspense>
    ),
  },
  {
    path: "/main",
    element: (
      <Suspense fallback={Loading}>
        <DefaultLayout /> {/* 메인 페이지  */}
      </Suspense>
    ),
    children: [
      {
        path: "",
        element: (
          <Suspense fallback={Loading}>
            <MainPage />
          </Suspense>
        ),
      },
    ],
  },
  {
    path: "/board",
    element: (
      <Suspense fallback={Loading}>
        <DefaultLayout />
      </Suspense>
    ),
    children: toBoardRouter(),
  },
  {
    path: "/work24",
    element: (
      <Suspense fallback={Loading}>
        <DefaultLayout />
      </Suspense>
    ),
    children: [
      {
        path: "",
        element: (
          <Suspense fallback={Loading}>
            <Work24Page />
          </Suspense>
        ),
      },
    ],
  },
  {
    path: "/auth",
    element: (
      <Suspense fallback={Loading}>
        <AuthLayout />
      </Suspense>
    ),
    children: toAuthRouter(),
  },
  {
    path: "/item",
    element: (
      <ProtectedRoute>
        <Suspense fallback={Loading}>
          <DefaultLayout />
        </Suspense>
      </ProtectedRoute>
    ),
    children: toItemRouter(),
  },
  {
    path: "/member",
    element: (
      <Suspense fallback={Loading}>
        <DefaultLayout />
      </Suspense>
    ),
    children: toMemberRouter(),
  },
  {
    path: "/admin",
    element: (
      <ProtectedRoute requiredRole='ROLE_ADMIN'>
        <Suspense fallback={Loading}>
          <AdminLayout />
        </Suspense>
      </ProtectedRoute>
    ),
    children: toAdminRouter(),
  },
  {
    path: "/inquiry",
    element: (
      <Suspense fallback={Loading}>
        <DefaultLayout />
      </Suspense>
    ),
    children: [
      {
        path: "",
        element: (
          <Suspense fallback={Loading}>
            <InquiryPage />
          </Suspense>
        ),
      },
    ],
  },
  {
    path: "/cart",
    element: (
      <ProtectedRoute>
        <Suspense fallback={Loading}>
          <DefaultLayout />
        </Suspense>
      </ProtectedRoute>
    ),
    children: toCartRouter(),
  },
  {
    path: "/pay",
    element: (
      <ProtectedRoute>
        <Suspense fallback={Loading}>
          <DefaultLayout />
        </Suspense>
      </ProtectedRoute>
    ),
    children: toPayRouter(),
  },
])

export default root

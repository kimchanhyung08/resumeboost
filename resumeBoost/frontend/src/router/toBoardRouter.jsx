import React, { Suspense } from 'react'
import BoardPage from '../pages/board/BoardPage';
import BoardWritePage from '../pages/board/BoardWritePage';
import BoardDetailPage from '../pages/board/BoardDetailPage';
import BoardUpdatePage from '../pages/board/BoardUpdatePage';
import BoardMyPage from '../pages/board/BoardMyPage';
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';


const Loading = <div className='loading'>Loading...</div>;

const ProtectedRoute = ({ children }) => {
  const isLogin = useSelector((state) => state.loginSlice);

  // 인증되지 않은 사용자일 경우 로그인 페이지로 리디렉션
  if (!isLogin.id) {
    return <Navigate to="/auth/login" replace />;
  }

  return children;
};

const toBoardRouter = () => {
  return (
    [
      {
        path: "",
        element: (
          <Suspense fallback={Loading}>
            <BoardPage />
          </Suspense>
        )
      },
      {
        path: "write",
        element:(
          <ProtectedRoute>
            <Suspense fallback={Loading}>
              <BoardWritePage />
            </Suspense>
          </ProtectedRoute>
        )
      },
      {
        path: "detail/:id",
        element:(
          <ProtectedRoute>
            <Suspense fallback={Loading}>
              <BoardDetailPage />
            </Suspense>
          </ProtectedRoute>
        )
      },
      {
        path: "update/:id",
        element:(
          <ProtectedRoute>
            <Suspense fallback={Loading}>
              <BoardUpdatePage />
            </Suspense>
          </ProtectedRoute>
        )
      },
      {
        path: "my",
        element:(
          <Suspense fallback={Loading}>
            <BoardMyPage />
          </Suspense>
        )
      }
    ]
  )
}

export default toBoardRouter
import React, { Suspense } from 'react'
import JoinPage from '../pages/auth/JoinPage';
import LoginPage from '../pages/auth/LoginPage';
import { Navigate } from 'react-router-dom';

const Loading = <div className='loading'>Loading...</div>;

const toAuthRouter = () => {
  return (
    [
      {
        path: "",
        element: <Navigate replace to="login" />,
      },
      {
        path: "join",
        element: (
          <Suspense fallback={Loading}>
            <JoinPage/>
          </Suspense>
        )
      },
      {
        path: "join/mentor",
        element: (
          <Suspense fallback={Loading}>
            {<JoinPage isMentor={true} />}
          </Suspense>
        )
      },
      {
        path: "login",
        element: (
          <Suspense fallback={Loading}>
            <LoginPage />
          </Suspense>
        )
      }
    ]
  )
}

export default toAuthRouter
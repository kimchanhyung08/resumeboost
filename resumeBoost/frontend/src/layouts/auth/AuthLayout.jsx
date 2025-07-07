import React from 'react'
import { Outlet } from 'react-router-dom'

const AuthLayout = () => {
  return (
    <>
      <div className="auth">
        <div className="auth-con">
          <Outlet />
        </div>
      </div>
    </>
  )
}

export default AuthLayout
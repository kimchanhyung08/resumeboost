import React from 'react'
import LeftBar from '../../components/admin/LeftBar'
import { Link, Outlet } from 'react-router-dom'
import Header from '../../components/admin/Header'

const AdminLayout = () => {
  return (
    <>
      <div className='admin'>
        <div className="admin-con">

          <LeftBar/>

          <div className="admin-page">
            <Header/>
            <Outlet/>
          </div>

          
        </div>
      </div>
    </>
  )
}

export default AdminLayout
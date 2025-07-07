import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getCookie } from '../../util/cookieUtil';
import useCustomLogin from '../../hook/useCustomLogin';

const Header = () => {

  const [nickName, setNickName] = useState('');

  const { doLogout, moveToPath } = useCustomLogin();

  useEffect(() => {
    const cookie = getCookie("member");
    const cookieData = cookie.NickName;

    setNickName(cookieData);
    

  }, [])


  const handleLogout = async (e) => {
    e.preventDefault();

    if (window.confirm("로그아웃 하시겠습니까?")) {
      
      await doLogout()

      moveToPath("/main")
      // localStorage.removeItem("userRole") // 로그아웃 시 role 정보 삭제
    }
  }


  return (
    <>
      <div className="admin-header">
        <ul>
          <li className='admin-icons'>
            <Link to={'/main'}> <img src={'/images/exit.png'}/> </Link>
            {/* <Link to={'/main'}> <img src={'/images/exit.png'}/> </Link> */}
          </li>
          <li className='admin-icons'>
            <Link to={'/admin/fullcalendar'}><img src={'/images/calendar.png'}/></Link>
          </li>
          <li>
            <Link onClick={(e)=>{handleLogout(e)}}>로그아웃</Link>
          </li>
          <li>
            <Link to={'/'}>{nickName}</Link>
          </li>
        </ul>
      </div>
    </>
  )
}

export default Header
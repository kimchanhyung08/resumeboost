import React, { useEffect, useState } from 'react'
import { getCookie } from '../../util/cookieUtil';
import jwtAxios from '../../util/jwtUtils';
import { EC2_URL } from '../../constans';



const Index = () => {

  const [login, setLogin] = useState({});


  const memberDetail = async (loginId) => {
    const res = await jwtAxios.get(`http://${EC2_URL}:8090/admin/member/detail/${loginId}`);

    const data = res.data.member;

    console.log(data)
  
    setLogin(data)
  }  


  useEffect(() => {
      const cookie = getCookie("member");
      const loginId = cookie.id;
    
      memberDetail(loginId);
  
    }, [])

    console.log(login)


  return (
    <>
      <div className='admin-index'>
        <div className='admin-index-con'>

          <div>

          </div>

        </div>
      </div>
    </>
  )
}

export default Index
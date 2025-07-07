import React, { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import jwtAxios from "../../util/jwtUtils"
import { useNavigate } from "react-router-dom"
import { EC2_URL } from "../../constans"

const ItemInsert = () => {
  const loginState = useSelector((state) => state.loginSlice)
  const myId = loginState.id
  const navigate = useNavigate()
  const [member, setMember] = useState("")
  const [formData, setFormData] = useState({
    memberId: "",
    category: "",
    itemPrice: 0,
  })

  const memberAxiosFn = async (myId) => {
    try {
      const result = await jwtAxios.get(
        `http://${EC2_URL}:8090/member/myDetail/${myId}`
      )
      setMember(result.data.member)
    } catch (err) {
      console.log(err)
    }
  }

  const inputChangeFn = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const submitFn = async (e) => {
    e.preventDefault()

    try {
      console.log(formData)
      const response = await jwtAxios.post(
        `http://${EC2_URL}:8090/item/insert`,
        formData
      )

      if (response.status === 200) {
        alert("상품 등록 성공")
        navigate(-1)
      }
    } catch (err) {
      alert("등록 오류")
      console.error(err)
    }
  }

  useEffect(() => {
    memberAxiosFn(myId)
    setFormData((prev) => ({
      ...prev,
      memberId: myId,
    }))
  }, [])

  return (
    <div className='itemInsert'>
      <div className='itemInsert-con'>
        <div className='memberProfile'>
          <div className='memberImgAndName'>
            <div className='memberImg'>
              <img src='/images/profile.png' alt='profile' />
            </div>
            <div className='memberName'>
              <h3>{member.userName}</h3>
              <div>
                <span>{member.age}세 · </span>
                <span>{member.address}</span>
              </div>
            </div>
          </div>
        </div>
        <div className='insert-con'>
          <h2>상품 등록</h2>
          <form onSubmit={submitFn}>
            <div className='itemInfo'>
              <div>
                <label>유형 선택</label>
                <select
                  name='category'
                  id='category'
                  value={formData.category}
                  onChange={inputChangeFn}
                >
                  <option value=''>선택</option>
                  <option value='면접'>면접</option>
                  <option value='이력서'>이력서</option>
                  <option value='자기소개서'>자기소개서</option>
                </select>
              </div>
              <div>
                <label>가격</label>
                <input
                  type='text'
                  name='itemPrice'
                  id='itemPrice'
                  value={formData.itemPrice}
                  onChange={inputChangeFn}
                />
              </div>
              <button type='submit'>등록하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  )
}

export default ItemInsert

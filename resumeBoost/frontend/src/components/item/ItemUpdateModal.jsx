import React, { useState } from "react"
import jwtAxios from "../../util/jwtUtils"
import { EC2_URL } from "../../constans"

const ItemUpdateModal = ({ setUpdateModal, item, setItems }) => {
  const [updateItem, setUpdateItem] = useState(item)
  console.log(updateItem)

  const handleUpdate = async () => {
    try {
      const response = await jwtAxios.post(
        `http://${EC2_URL}:8090/item/update`,
        updateItem
      )
      if (response.status === 200) {
        alert("수정 성공")
        setUpdateModal(false)
        setItems((prevItems) =>
          prevItems.map((item) =>
            item.id === updateItem.id ? updateItem : item
          )
        )
      }
    } catch (error) {
      console.log(error)
      alert("수정 실패")
    }
  }
  return (
    <div
      className='ItemUpdateModal-overlay'
      onClick={() => setUpdateModal(false)}
    >
      <div className='ItemUpdateModal-con' onClick={(e) => e.stopPropagation()}>
        <span onClick={() => setUpdateModal(false)} className='close-btn'>
          X
        </span>
        <div className='modal-content'>
          {/* 여기에 수정할 상품 정보를 보여주는 내용을 넣으면 됩니다. */}
          <h3>상품 수정</h3>
          <div>
            <label>카테고리</label>
            <select
              type='text'
              value={updateItem.category}
              onChange={(e) =>
                setUpdateItem({ ...updateItem, category: e.target.value })
              }
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
              value={updateItem.itemPrice}
              onChange={(e) =>
                setUpdateItem({ ...updateItem, itemPrice: e.target.value })
              }
            />
          </div>
          <div>
            <button onClick={handleUpdate}>수정 완료</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ItemUpdateModal

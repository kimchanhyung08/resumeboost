import React, { useState } from "react"
import jwtAxios from "../../util/jwtUtils"
import ItemUpdateModal from "./ItemUpdateModal"
import { EC2_URL } from "../../constans"

const MyItems = ({ items, itemAxiosFn, setItems }) => {
  const [updateModal, setUpdateModal] = useState(false)
  const [selectedItem, setSelectedItem] = useState(null)
  const itemDeleteFn = async (id) => {
    if (!window.confirm("상품을 삭제하시겠습니까?")) return

    try {
      await jwtAxios.delete(`http://${EC2_URL}:8090/item/delete/${id}`)
      alert("상품이 삭제되었습니다")

      // itemAxiosFn()
      setItems((prevItems) => prevItems.filter((item) => item.id !== id))
    } catch (error) {
      console.log(error)
      alert("삭제 실패")
    }
  }
  const itemUpdateFn = async (item) => {
    setUpdateModal(true)
    setSelectedItem(item)
  }
  return (
    <div className='myItemContainer'>
      <div className='myItemHeader'>
        <h3>내가 등록한 상품</h3>
      </div>
      {items.length > 0 ? (
        <ul className='myItemList'>
          {items.map((item) => (
            <li key={item.id}>
              <div className='itemP'>
                <p>
                  <span>카테고리:</span> {item.category}
                </p>
                <p>
                  <span>상품가격 :</span> {item.itemPrice}원
                </p>
              </div>
              <div className='itemB'>
                <button onClick={() => itemUpdateFn(item)}>수정</button>
                <button onClick={() => itemDeleteFn(item.id)}>삭제</button>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className='noItemText'>등록된 상품이 없습니다.</p>
      )}
      {updateModal ? (
        <ItemUpdateModal
          setUpdateModal={setUpdateModal}
          item={selectedItem}
          setItems={setItems}
        />
      ) : (
        <></>
      )}
    </div>
  )
}

export default MyItems

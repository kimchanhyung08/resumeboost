import React from "react"
import MyCart from "../../components/cart/MyCart"
import { useParams } from "react-router-dom"

const MyCartPage = () => {
  const param = useParams()
  return <MyCart param={param} />
}
export default MyCartPage

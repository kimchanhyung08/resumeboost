import React from "react"
import AddPay from "../../components/pay/AddPay"
import { useParams } from "react-router-dom"

const AddPayPage = () => {
  const param = useParams()
  return <AddPay param={param} />
}

export default AddPayPage

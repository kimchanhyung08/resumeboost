import React from "react"
import Mypay from "../../components/pay/Mypay"
import { useParams } from "react-router-dom"

const MyPayPage = () => {
  const param = useParams()
  return <Mypay param={param} />
}

export default MyPayPage

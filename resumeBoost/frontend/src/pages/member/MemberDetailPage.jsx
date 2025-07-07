import React from "react"
import Member from "./../../components/member/Member"
import { useParams } from "react-router-dom"

const MemberDetailPage = () => {
  const param = useParams()
  return (
    <>
      <Member param={param} />
    </>
  )
}

export default MemberDetailPage

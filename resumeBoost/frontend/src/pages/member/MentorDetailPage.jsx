import React from "react"
import Mentor from "./../../components/member/Mentor"
import { useParams } from "react-router-dom"

const MentorDetailPage = () => {
  const param = useParams()
  return (
    <>
      <Mentor param={param} />
    </>
  )
}

export default MentorDetailPage

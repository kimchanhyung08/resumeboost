import React from 'react'
import BoardDetail from '../../components/board/BoardDetail'
import { useParams } from 'react-router-dom'

const BoardDetailPage = () => {
  const param = useParams()
  return (
    <>
      <BoardDetail param={param}/>
    </>
  )
}

export default BoardDetailPage
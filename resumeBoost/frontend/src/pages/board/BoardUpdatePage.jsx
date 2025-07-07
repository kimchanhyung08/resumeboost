import React from 'react'
import BoardUpdate from '../../components/board/BoardUpdate'
import { useParams, useLocation } from 'react-router-dom'

const BoardUpdatePage = () => {
  const params = useParams();
  const location = useLocation();
  const boardDetail = location.state?.boardDetail;

  return (
    <>
      <BoardUpdate params={params} boardDetail={boardDetail} />
    </>
  )
}

export default BoardUpdatePage
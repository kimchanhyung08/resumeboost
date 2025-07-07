import React from 'react'
import BoardDetail from '../../components/admin/BoardDetail'
import { useParams } from 'react-router-dom'

const BoardDetailPage = () => {
  const { id } = useParams();
  console.log(id)
  return (
    <BoardDetail param={ id }/>
  )
}

export default BoardDetailPage
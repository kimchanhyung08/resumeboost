import React from 'react'
import PayDetail from '../../components/admin/PayDetail'
import { useParams } from 'react-router-dom';

const PayDetailPage = () => {

  const { id } = useParams();
  console.log(id)

  return (
    <PayDetail param={ id }/>
  )
}

export default PayDetailPage
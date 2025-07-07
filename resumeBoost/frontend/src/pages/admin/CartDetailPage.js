import React from 'react'
import CartDetail from '../../components/admin/CartDetail'
import { useParams } from 'react-router-dom';

const CartDetailPage = () => {
  const { id } = useParams();
  console.log(id)
  return (
    <CartDetail param={ id }/>
  )
}

export default CartDetailPage
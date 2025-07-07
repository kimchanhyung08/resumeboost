import React from 'react'
import Item from '../../components/admin/Item'
import ItemDetail from '../../components/admin/ItemDetail'
import { useParams } from 'react-router-dom';

const ItemDetailPage = () => {
  const { id } = useParams();
  console.log(id)

  return (
    <ItemDetail param={ id }/>
  )
}

export default ItemDetailPage
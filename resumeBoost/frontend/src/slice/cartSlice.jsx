import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import { getCookie } from "../util/cookieUtil"
import jwtAxios from "../util/jwtUtils"
import { EC2_URL } from "../constans"

const initState = {
  items: [],
}

export const cartData = createAsyncThunk(
  "cartData",
  async (_, { getState }) => {
    try {
      const memberInfo = getCookie("member")
      const userId = memberInfo?.id

      if (!userId) throw new Error("User not logged in")

      const res = await jwtAxios.get(
        `http://${EC2_URL}:8090/cart/myCart/${userId}`
      )
      const cartItems = res.data.cart.itemListEntities

      const items = cartItems.map((item) => item.itemEntity)
      return items
    } catch (error) {
      throw new Error(error.message || "Failed to fetch cart data")
    }
  }
)

const cartSlice = createSlice({
  name: "cartSlice",
  initialState: initState,
  reducers: {
    addItemCart: (state, action) => {
      const existingItem = state.items.find(
        (item) => item.id === action.payload.id
      )
      if (!existingItem) {
        state.items = [...state.items, action.payload]
      }
    },
    removeItemCart: (state, action) => {
      state.items = state.items.filter((item) => item.id !== action.payload)
    },
    clearCart: (state) => {
      state.items = []
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(cartData.pending, (state) => {
        state.status = "loading"
      })
      .addCase(cartData.fulfilled, (state, action) => {
        state.status = "succeeded"
        state.items = action.payload // 서버에서 가져온 장바구니 정보 저장
      })
      .addCase(cartData.rejected, (state, action) => {
        state.status = "failed"
        state.error = action.error.message
      })
  },
})

export const { addItemCart, removeItemCart, clearCart } = cartSlice.actions
export default cartSlice.reducer

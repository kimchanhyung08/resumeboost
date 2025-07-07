import { configureStore } from "@reduxjs/toolkit"
import { persistStore, persistReducer } from "redux-persist"
import storage from "redux-persist/lib/storage" // 로컬 스토리지 사용
import loginSlice from "./../slice/loginSlice"
import cartSlice from "./../slice/cartSlice"

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["loginSlice", "cartSlice"],
}

const persistedLoginReducer = persistReducer(persistConfig, loginSlice)
const persistedCartReducer = persistReducer(persistConfig, cartSlice)

const store = configureStore({
  reducer: {
    loginSlice: persistedLoginReducer,
    cartSlice: persistedCartReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: ["persist/PERSIST"],
      },
    }),
})

export const persistor = persistStore(store)
export default store

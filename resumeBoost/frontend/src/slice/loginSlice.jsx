import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import { setCookie, getCookie, removeCookie } from "../util/cookieUtil"
import { loginPost } from "../util/memberApi"

const initState = {
  userEmail: "",
}

const loadMemberCookie = () => {
  const memberInfo = getCookie("member")

  if (memberInfo && memberInfo.userEmail) {
    return {
      ...memberInfo,
      userEmail: decodeURIComponent(memberInfo.userEmail),
      role: memberInfo.role || null, // 단일 값으로 처리
    }
  }
  return null
}

export const loginPostAsync = createAsyncThunk(
  "loginPostAsync",
  async (param) => {
    try {
      const response = await loginPost(param)
      return response
    } catch (error) {
      return Promise.reject(error.message) // 에러 메시지 반환
    }
  }
)

const loginSlice = createSlice({
  name: "LoginSlice",
  initialState: loadMemberCookie() || initState,
  reducers: {
    login: (state, action) => {
      const payload = action.payload
      const newState = {
        ...payload,
        role: payload.role || null,
      }
      setCookie("member", JSON.stringify(newState), 1)
      return payload
    },
    logout: (state, action) => {
      removeCookie("member")
      return { ...initState }
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        const payload = action.payload

        if (!payload.error) {
          const newState = {
            ...payload,
            role: payload.role || null, // 단일 값으로 처리
          }
          setCookie("member", JSON.stringify(newState), 1) // 쿠키 저장
          return newState
        }

        return state
      })
      .addCase(loginPostAsync.pending, (state, action) => {
        console.log("pending")
      })
      .addCase(loginPostAsync.rejected, (state, action) => {
        console.log("rejected")
      })
  },
})

export const { login, logout } = loginSlice.actions
export default loginSlice.reducer

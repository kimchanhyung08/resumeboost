import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import loginSlice, { loginPostAsync, logout } from "./../slice/loginSlice";

const useCustomLogin = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const loginState = useSelector((state) => state.loginSlice);
  const isLogin = loginState.userEmail ? true : false;

  //로그인 함수
  const doLogin = async (loginParam) => {
    const action = await dispatch(loginPostAsync(loginParam));
    return action.payload;
  };

  //로그아웃
  const doLogout = () => {
    dispatch(logout());
  };

  //커스텀 경로이동
  const moveToPath = (path) => {
    navigate({ pathname: path }, { replace: true });
  };

  // 로그인페이지로 이동
  const moveToLogin = () => {
    navigate({ pathname: "/login" }, { replace: true });
  };

  return { loginState, isLogin, doLogin, doLogout, moveToPath, moveToLogin };
};

export default useCustomLogin;

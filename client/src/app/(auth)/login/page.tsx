"use client";

import Link from "next/link";
import React, { useState } from "react";
import axios from "axios";
import { handleSubmit } from "./actions";

interface LoginTypes {
  email: string;
  password: string;
}

const Login = () => {
  const [post, setPost] = useState<LoginTypes | null>(null);

  // const getPostByLogin = async (email: string, password: string) => {
  //   try {
  //     const response = await axios.post("/auth/login", {
  //       email: email,
  //       password: password,
  //     });
  //     console.log("서버 응답", response.data);
  //   } catch (error) {
  //     console.log("오류 발생");
  //   }
  // };

  // const handleSubmit = async (formData: any) => {
  //   console.log(formData.get("email"));
  //   console.log(formData.get("password"));
  // };
  console.log("hello");

  return (
    <div>
      <h2>login page</h2>
      <button onClick={() => console.log("hello world")}>확인</button>
      <form action={handleSubmit}>
        <input
          type="text"
          placeholder="이메일을 입력하세요"
          name="email"
        ></input>
        <input
          type="text"
          placeholder="비밀번호를 입력하세요"
          name="password"
        ></input>
        <input type="submit" value={"로그인"}></input>
      </form>
      <Link href={"/register"}>회원가입</Link>
      <Link href={"/forgot-password"}>비밀번호 찾기</Link>
      <section>
        <h4>소셜로그인</h4>
        <Link href="/social-register">kakao</Link>
      </section>
    </div>
  );
};

export default Login;

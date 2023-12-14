import Link from "next/link";
import React from "react";

const Login = () => {
  return (
    <div>
      <h2>login page</h2>
      <input type="text" placeholder="이메일을 입력하세요"></input>
      <input type="text" placeholder="비밀번호를 입력하세요"></input>
      <input type="submit" value={"로그인"}></input>
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

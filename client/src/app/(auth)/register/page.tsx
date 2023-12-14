import React from "react";

const Register = () => {
  return (
    <div>
      <h2>Register page</h2>
      <ul>
        <li>
          <label htmlFor="email_input">이메일</label>
          <input
            id="email_input"
            type="text"
            placeholder="이메일을 입력하세요"
          ></input>
          <button>이메일 중복 확인</button>
        </li>
        <li>
          <label htmlFor="password_input">비밀번호</label>
          <input
            id="password_input"
            type="password"
            placeholder="비밀번호를 입력하세요"
          ></input>
        </li>
        <li>
          <label htmlFor="password-check_input">비밀번호 재확인</label>
          <input
            id="password-check_input"
            type="password"
            placeholder="비밀번호를 입력하세요"
          ></input>
        </li>
        <li>
          <label htmlFor="nickname_input">닉네임</label>
          <input
            id="nickname_input"
            type="text"
            placeholder="닉네임을 입력하세요"
          ></input>
        </li>
      </ul>

      <input type="submit" value={"회원가입"}></input>
    </div>
  );
};

export default Register;

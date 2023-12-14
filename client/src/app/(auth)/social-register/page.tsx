import React from "react";

const SocialRegister = () => {
  return (
    <div>
      <h2>SocialRegister page</h2>
      <ul>
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

export default SocialRegister;

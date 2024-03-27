'use client';

import React, { useState } from 'react';
import { handleFindPassword } from './actions';
import Link from 'next/link';

const Find = () => {
  const [isPost, setIsPost] = useState(false);
  const [isError, setIsError] = useState(false);
  const [message, setMessage] = useState<String | null>(null);
  const [errorMessage, setErrorMessage] = useState<String | null>(null);

  const handleSubmit = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    setIsError(false);
    setErrorMessage(null);
    const form = e.target as HTMLFormElement;
    const postData = {
      email: form.email?.value,
    };
    const response = await handleFindPassword(postData)
    if (response.statusCode == 200) {
      setMessage(response.message);
      setIsPost(true);
    } else {
      setIsError(true);
      setErrorMessage(response.message);
    }
  };
  
  return (
    <div>
      <h2 className="w-full text-center text-3xl font-extrabold mt-20 h-24 font-['MoiraiOne'] text-orange-400">
        골키퍼스
      </h2>
      <h2 className="w-full text-center text-2xl h-16 font-[''] text-orange-400">
        비밀번호 찾기
      </h2>
      <h3 className="text-left text-[15px] h-16">
        가입한 이메일을 입력하면 이메일로 임시 비밀번호가 발송됩니다.
      </h3>
      {!isPost ? <form onSubmit={handleSubmit} className="flex flex-col w-full gap-2">
        <input
          type="email"
          placeholder="example@example.com"
          name="email"
          className="w-full h-11 border rounded-md p-3 mb-2"
        ></input>
        {isError ? 
        <h1 className="text-left text-[15px] mb-1 mx-1 text-amber-600 font-extrabold">
          {errorMessage}
        </h1> : <></>}
        <input
          className="gk-primary-login-button"
          type="submit"
          value={'이메일 보내기'}
        ></input>
      </form> : 
      <>
        <h3 className="text-left text-[15px] h-16">{message}</h3>
      </>}
      <div className="flex flex-col w-full gap-1 mt-3">
        <Link
          href={'/login'}
          className="w-full text-center h-9 border rounded-md leading-9 text-sm text-gray-600"
        >
          로그인 하러 가기
        </Link>
      </div>
    </div>
  );
};

export default Find;
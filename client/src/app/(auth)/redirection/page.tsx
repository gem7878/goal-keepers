'use client';

import React, { useEffect, useState } from 'react';
import { handleKakaoLogin } from './actions';
import { useRouter } from 'next/navigation';
import { type } from 'os';

const Redirection = () => {
  const router = useRouter();
  useEffect(() => {
    onKakaoCode();
  }, []);

  const onKakaoCode = async () => {
    const url = new URL(window.location.href);
    const code = url.searchParams.get('code');
    if (code !== null) {
      return onKakaoLogin(code);
    }
  };

  const onKakaoLogin = async (reqCode: string) => {
    try {
      const response = await handleKakaoLogin(reqCode);
      if (response?.ok) {
        router.push('/');
      }
    } catch (error) {
      return console.log(error);
    }
  };

  return (
    <div className="h-full w-full flex flex-col items-center justify-center pb-10">
      <h2 className="w-full text-center text-2xl font-extrabold ">
        로그인 중입니다...
      </h2>
    </div>
  );
};

export default Redirection;

'use server';

import { POST } from '@/app/api/auth/login/route';
import { serialize } from 'cookie';
import Cookies from 'js-cookie';
import { cookies } from 'next/headers';

// 액세스 토큰을 쿠키에 저장하는 함수
export const setAccessTokenCookie = (token: string) => {
  // 쿠키 만료 날짜 설정
  const oneDayInSeconds = 24 * 60 * 60;
  const expiresDate = new Date();
  expiresDate.setTime(expiresDate.getTime() + oneDayInSeconds * 1000);

  cookies().set({
    name: 'accessToken',
    value: token,
    httpOnly: true,
    expires: expiresDate,
    path: '/',
    secure: process.env.NEXT_PUBLIC_ENV === 'production',
    sameSite: 'strict',
  });
};
export const handleLogin = async (postData: {
  email: string;
  password: string;
}) => {
  return POST(postData)
    .then((response) => {
      if (response.statusCode == 200) {
        setAccessTokenCookie(JSON.parse(response.body).data.accessToken);
        return { ok: true };
      } else {
        return { ok: false };
      }
    })
    .catch((error) => console.log(error));
};

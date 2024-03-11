'use server';

import axios from 'axios';
import { cookies } from 'next/headers';

// 액세스 토큰을 쿠키에 저장하는 함수
export const setAccessTokenCookie = (token: string) => {
  // 쿠키 만료 날짜 설정
  const oneDayInSeconds = 24 * 60 * 60;
  const expiresDate = new Date();
  expiresDate.setTime(expiresDate.getTime() + oneDayInSeconds * 1000);

  // Cookies.set('accessToken', token, {
  //   expires: expiresDate,
  //   path: '/',
  //   httpOnly: true,
  //   secure: process.env.NEXT_PUBLIC_ENV === 'production',
  //   sameSite: process.env.NEXT_PUBLIC_ENV === 'production' ? 'none' : 'lax',
  // });

  cookies().set({
    name: 'accessToken',
    // name: '_vercel_jwt',
    value: token,
    httpOnly: true,
    expires: expiresDate,
    path: '/',
    secure: process.env.NEXT_PUBLIC_ENV === 'production',
    sameSite: process.env.NEXT_PUBLIC_ENV === 'production' ? 'none' : 'lax',
  });
};

export const handleLogin = async (postData: {
  email: string;
  password: string;
}) => {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/auth/login`,
      {
        email: postData.email,
        password: postData.password,
      },
      {
        headers: { 'Content-Type': 'application/json' },
        withCredentials: true,
      },
    );
    setAccessTokenCookie(response.data.data.accessToken);
    return { ok: true };
  } catch (error: any) {
    return { ok: false };
  }
};

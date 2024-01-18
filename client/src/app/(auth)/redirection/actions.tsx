'use server';

import { setAccessTokenCookie } from '../login/actions';
import axios from 'axios';

export const handleKakaoLogin = async (code: string) => {
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/api/kakao/login?code=${code}`,
      {
        headers: { 'Content-Type': 'application/json' },
      },
    );

    const accessToken = response.data.data.accessToken;

    console.log(response);
    console.log(accessToken);

    setAccessTokenCookie(accessToken);

    return { ok: true };
  } catch (error) {
    return { ok: true };
  }
};

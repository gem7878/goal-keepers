'use server';

import { GET } from '@/app/api/auth/kakao/route';
import { setAccessTokenCookie } from '../login/actions';

export const handleKakaoLogin = async (code: string) => {
  try {
    const response = await GET(code);
    if ('statusCode' in response && response.statusCode === 200) {
      const accessToken = JSON.parse(response.body).data.accessToken;
      setAccessTokenCookie(accessToken);

      return { ok: true };
    }
  } catch (error) {
    console.error(error);
    return { ok: false };
  }
};

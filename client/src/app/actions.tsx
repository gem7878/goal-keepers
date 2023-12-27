'use server';

import { cookies } from 'next/headers';
import { GET as AllGET } from '@/app/api/goal-list/all/route';

export const handleConfirmToken = async () => {
  const cookieStore = cookies();
  const hasCookie = cookieStore.has('accessToken');
  const accessToken: string | undefined = cookieStore.get('accessToken')?.value;

  function isTokenExpired(token: any) {
    const decodedToken = decodeToken(token);

    // 만료 시간이 없거나 현재 시간이 만료 시간 이후이면 토큰이 만료되지 않았음
    return !decodedToken.exp || Date.now() >= decodedToken.exp * 1000;
  }

  function decodeToken(token: any) {
    // Base64 디코딩 후 JSON 파싱
    const payload = token.split('.')[1];
    const decodedPayload = Buffer.from(payload, 'base64').toString('utf-8');
    return JSON.parse(decodedPayload);
  }

  // 사용 예시
  if (!hasCookie || isTokenExpired(accessToken)) {
    return false;
  } else {
    console.log(accessToken);

    return true;
  }
};

export const handleGetGoalListAll = async () => {
  return AllGET()
    .then((response) => {
      console.log(response);
    })
    .catch((error) => console.log(error));
};

'use server';

import { cookies } from 'next/headers';
import { GET as AllGET } from '@/app/api/goal-list/all/route';
// import { PUT } from '@/app/api/goal-list/goal/route';

export const handleGetAccessToken = () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  return token;
};

export const handleConfirmToken = async () => {
  const cookieStore = cookies();
  const hasCookie = cookieStore.has('accessToken');
  const accessToken: string | undefined = cookieStore.get('accessToken')?.value;

  console.log(accessToken);

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
    console.log('토큰이 만료되었습니다');

    return false;
  } else {
    console.log('토큰이 유효합니다');
    return true;
  }
};

export const handleGetGoalListAll = async () => {
  return AllGET()
    .then((response: any) => {
      return JSON.parse(response.body);
    })
    .catch((error) => console.log(error));
};
// export const handleUpdateGoal = async (putData: {
//   goalId: number | undefined;
//   title: string | undefined;
//   description: string | undefined;
//   startDate: string | undefined;
//   endDate: string | undefined;
//   imageUrl: any;
// }) => {
//   return PUT(putData)
//     .then((response) => console.log(response))
//     .catch((error) => console.log(error));
// };

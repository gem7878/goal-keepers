'use server';

import { cookies } from 'next/headers';
import { GET as AllGET } from '@/app/api/goal-list/all/route';
import { GET as UserGET } from '@/app/api/member/me/route';
import { DELETE, PUT } from '@/app/api/goal-list/goal/route';

export const handleGetAccessToken = () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  return token;
};

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

  if (!hasCookie || isTokenExpired(accessToken)) {
    return false;
  } else {
    return true;
  }
};
export const handleGetUserInfo = async () => {
  return UserGET()
    .then((response: any) => {
      if (response.statusCode === 200) {
        return JSON.parse(response.body).data;
      }
    })
    .catch((error) => console.log(error));
};
export const handleGetGoalListAll = async () => {
  return AllGET()
    .then((response: any) => {
      return JSON.parse(response.body);
    })
    .catch((error) => console.log(error));
};

export const handleUpdateGoal = async (putData: {
  goalId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  startDate: string | undefined;
  endDate: string | undefined;
  imageUrl: any;
}) => {
  return PUT(putData)
    .then((reponse) => {
      return JSON.parse(reponse.body);
    })
    .catch((error) => console.log(error));
};

export const handleDeleteGoal = async (deleteData: {
  goalId: number | undefined;
}) => {
  return DELETE(deleteData)
    .then((reponse) => {
      if (typeof reponse.body === 'string') {
        return JSON.parse(reponse.body);
      } else {
        return reponse.body;
      }
    })
    .catch((error) => console.log(error));
};

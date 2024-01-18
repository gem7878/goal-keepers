'use server';

import { cookies } from 'next/headers';
import axios from 'axios';

// const token: string | undefined = cookieStore.get('_vercel_jwt')?.value;

export const handleGetAccessToken = () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  return token;
};

export const handleConfirmToken = async () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  const hasCookie = cookieStore.has('accessToken');
  // const hasCookie = cookieStore.has('_vercel_jwt');

  function isTokenExpired(token: any) {
    // const decodedToken = decodeToken(token);
    // 만료 시간이 없거나 현재 시간이 만료 시간 이후이면 토큰이 만료되지 않았음
    return !token.exp || Date.now() >= token.exp * 1000;
    // return !decodedToken.exp || Date.now() >= decodedToken.exp * 1000;
  }

  // function decodeToken(token: any) {
  //   // Base64 디코딩 후 JSON 파싱
  //   const payload = token.split('.')[1];
  //   const decodedPayload = Buffer.from(payload, 'base64').toString('utf-8');
  //   return JSON.parse(decodedPayload);
  // }
  console.log(hasCookie);
  console.log(token);
  console.log(isTokenExpired(token));

  if (!hasCookie || !token || !isTokenExpired(token)) {
    return false;
  } else {
    return true;
  }
};
export const handleGetUserInfo = async () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;

  console.log(token);

  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/member/me`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    );
    return response.data.data;
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};
export const handleGetGoalListAll = async (getData: { pageNum: number }) => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  // const token: string | undefined = cookieStore.get('_vercel_jwt')?.value;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/goal-list/all?page=${getData.pageNum}`,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};
export const handleUpdateGoal = async (putData: any) => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  try {
    const id = putData.goalId;
    const response = await axios.put(
      `${process.env.NEXT_PUBLIC_API_URL}/goal-list/goal?id=${id}`,
      putData.formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    return response.data;
  } catch (error: any) {
    console.error('Error during request setup:', error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: 'Internal Server Error' }),
    };
  }
};

export const handleDeleteGoal = async (deleteData: {
  goalId: number | undefined;
}) => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;
  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/goal-list/goal?id=${deleteData.goalId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    return response.data;
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

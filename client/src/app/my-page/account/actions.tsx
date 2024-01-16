'use server';

import { setAccessTokenCookie } from '@/app/(auth)/login/actions';
import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

export const handleChangeNickname = async (nickname: string) => {
  const formData = {
    nickname: nickname,
  };
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/member/nickname`,
      {
        nickname: formData.nickname,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    return response.data;
  } catch (error: any) {
    return {
      statusCode: error.response.status,
      body: JSON.stringify(error.response.data),
    };
  }
};
export const handleChangePassword = async (formData: {
  email: string;
  exPassword: string;
  newPassword: string;
}) => {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/member/password`,
      {
        email: formData.email,
        exPassword: formData.exPassword,
        newPassword: formData.newPassword,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    setAccessTokenCookie(response.data.data.accessToken);
    return response.data;
  } catch (error: any) {
    return {
      statusCode: 400,
      body: JSON.stringify(error.response.data),
    };
  }
};
export const handleRemoveMember = async (formData: {
  email: string;
  password: string;
}) => {
  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/member/me`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        data: {
          email: formData.email,
          password: formData.password,
        },
      },
    );
    return { statusCode: 200, ...response.data };
  } catch (error: any) {
    const statusCode = error.response.status;
    const statusText = error.response.data.code;
    const message = error.response.data.message;
    const validation = error.response.data.validation;
    return {
      statusCode: statusCode,
      statusText: statusText,
      message: message,
      validation: validation,
    };
  }
};

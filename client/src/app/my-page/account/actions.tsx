'use server';

import { setAccessTokenCookie } from '@/app/(auth)/login/actions';
import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';

export const handleChangeNickname = async (nickname: string) => {
  const token = handleGetToken().token;
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
  const token = handleGetToken().token;
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
  const token = handleGetToken().token;
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

export const handleLogout = async () => {
  const cookieStore = handleGetToken().cookieStore;
  cookieStore.delete('accessToken');
  return { ok: true };
};

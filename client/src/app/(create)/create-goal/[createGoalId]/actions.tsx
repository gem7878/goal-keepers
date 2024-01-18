'use server';
import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';

export const handlePostGoalData = async (formData: any) => {
  const token = handleGetToken().token;
  const postData = {
    formData: formData,
  };
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/goal-list/goal`,
      postData.formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    return { ok: true };
  } catch (error: any) {
    return { ok: false };
  }
};

'use server';
import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

export const handlePostGoalData = async (formData: any) => {
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

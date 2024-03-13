'use server';

import { handleGetToken } from '@/utils/getCookie';
import axios from 'axios';

export const handleCreateContent = async (postData: {
  goalId: number;
  privated: boolean;
  content: string;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/post`,
      {
        content: postData.content,
        goalId: postData.goalId,
        privated: postData.privated,
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
    console.error('Error during request setup:', error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: 'Internal Server Error' }),
    };
  }
};

export const handleSelectMyPost = async (getData: { pageNum: number }) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/post/all/me/select?page=${getData.pageNum}`,
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
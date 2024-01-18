'use server';

import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';

export const handleCreatePost = async (postData: {
  goalId: number;
  title: string;
  content: string;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/post`,
      {
        title: postData.title,
        content: postData.content,
        goalId: postData.goalId,
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

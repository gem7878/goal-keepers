'use server';

import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';

export const handleGetPostAll = async (getData: { pageNum: number }) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/post/all?page=${getData.pageNum}&sort=LIKE`,
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

export const handleDeletePost = async (postData: { postId: number }) => {
  const token = handleGetToken().token;
  try {
    const id = postData.postId;
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/board/post?post-id=${id}`,
      {
        headers: {
          'Content-Type': 'application/json',
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

export const handleLikePost = async (postId: number) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/post/like`,
      {
        postId: postId,
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

export const handleGetMyPostAll = async (getData: { pageNum: number }) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/post/all/me?page=${getData.pageNum}&sort=NEW`,
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

export const handleCreatePostContent = async (formData: {
  content: string;
  goalId: number;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/post/like`,
      {
        content: formData.content,
        goalId: formData.goalId,
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

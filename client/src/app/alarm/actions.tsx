'use server';

import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';
import { EventSourcePolyfill, NativeEventSource } from 'event-source-polyfill';

export const handleGetAlarm = async (getData: {
  pageNum: number;
  type: string;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/alarm?type=${getData.type}&page=${getData.pageNum}`,
      {
        headers: {
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

export const handleReadAlarm = async () => {
  const token = handleGetToken().token;
  try {
    const response = await axios.put(
      `${process.env.NEXT_PUBLIC_API_URL}/alarm`,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    console.log(response);

    return response.data;
  } catch (error: any) {
    console.error('Error during request setup:', error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: 'Internal Server Error' }),
    };
  }
};

export const handleDeleteAlarm = async (formData: {
  all: boolean;
  deleteList: number[];
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/alarm`,
      {
        data: { all: formData.all, deleteList: formData.deleteList },
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

export const handlePostCommentAlarm = async (formData: {
  type: string;
  targetId: number;
  commentId: number;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/alarm/target`,
      {
        type: formData.type,
        targetId: formData.targetId,
        commentId: formData.commentId,
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

export const tokenValue = async () => {
  const token = handleGetToken().token;

  return token;
};

'use server';

import { handleGetToken } from '@/utils/getCookie';
import axios from 'axios';

export const handleGetShare = async (goalId: number) => {
  const token = handleGetToken().token;
  const formData = {
    goalId: goalId,
  };
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/goal/share?goal-id=${formData.goalId}`,
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

export const handleCreateShare = async (goalId: number) => {
  const token = handleGetToken().token;
  const formData = {
    goalId: goalId,
  };
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/goal/share`,
      {
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

export const handleDeleteShare = async (goalId: number) => {
  const token = handleGetToken().token;
  const formData = {
    goalId: goalId,
  };
  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/goal/share`,
      {
        data: {
          goalId: formData.goalId,
        },
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

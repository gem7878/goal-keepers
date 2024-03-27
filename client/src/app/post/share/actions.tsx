'use server';

import { handleGetToken } from '@/utils/getCookie';
import axios from 'axios';

export const handleFindConnectedGoal = async (goalId: number) => {
  const token = handleGetToken().token;

  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/goal/share?goal-id=${goalId}`,
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
    return error.response.data;
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
    return error.response.data;
  }
};

export const handleDeleteShare = async (goalId: number) => {
  const token = handleGetToken().token;

  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/goal/share`,
      {
        data: {
          goalId: goalId,
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

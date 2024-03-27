'use server';

import { handleGetToken } from '@/utils/getCookie';
import axios from 'axios';

export const handleGetAlarmSetting = async () => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/setting/me`,
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

export const handleChangeAlarmSetting = async (formData: {
  commentAlarm: boolean;
  contentLikeAlarm: boolean;
  postCheerAlarm: boolean;
  goalShareAlarm: boolean;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.put(
      `${process.env.NEXT_PUBLIC_API_URL}/setting/me`,
      {
        commentAlarm: formData.commentAlarm,
        contentLikeAlarm: formData.contentLikeAlarm,
        postCheerAlarm: formData.postCheerAlarm,
        goalShareAlarm: formData.goalShareAlarm,
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

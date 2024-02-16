'use server';

import { handleGetToken } from '@/utils/getToken';
import axios from 'axios';

export const handleGetCommunityAll = async (getData: {
  pageNum: number;
  sort: string;
}) => {
  const token = handleGetToken().token;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/community?page=${getData.pageNum}&sort=${getData.sort}`,
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

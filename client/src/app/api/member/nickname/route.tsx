import axios from "axios";

import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

interface POSTTypes {
  nickname: string;
}

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/member/nickname`,
      {
        nickname: request.nickname,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );
    
    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error: any) {
    return {
      statusCode: error.response.status,
      body: JSON.stringify(error.response.data)
    }
  }
};

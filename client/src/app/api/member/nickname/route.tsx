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
      'http://localhost:8080/member/nickname',
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
    console.error("Error during request setup:", error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: "Internal Server Error" }),
    };
  }
};

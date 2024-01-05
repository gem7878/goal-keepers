import axios from 'axios';

import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

interface POSTTypes {
  email: string;
  exPassword: string;
  newPassword: string;
}

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/member/password',
      {
        email: request.email,
        exPassword: request.exPassword,
        newPassword: request.newPassword,
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
      statusCode: 400,
      body: JSON.stringify(error.response.data),
    };
  }
};

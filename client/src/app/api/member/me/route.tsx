import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;
const uri = 'http://localhost:8080';

interface DELETETypes {
  email: string;
  password: string;
}

export const GET = async () => {
  try {
    const response = await axios.get(`${uri}/member/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

export const DELETE = async (request : DELETETypes) => {
  try {
    const response = await axios.delete(`${uri}/member/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      data: {
        email: request.email,
        password: request.password
      }
    })
    return { statusCode: 200, ...response.data };
  } catch (error : any) {
    const statusCode = error.response.status;
    const statusText = error.response.data.code;
    const message = error.response.data.message;
    const validation = error.response.data.validation;
    return {
        statusCode: statusCode,
        statusText: statusText,
        message: message,
        validation: validation
    }
  }
}
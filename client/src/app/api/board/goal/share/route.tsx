import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;
interface GETTypes {
  goalId: number;
}
interface POSTTypes {
  goalId: number;
}
interface DELETETypes {
  goalId: number;
}

export const GET = async (request: GETTypes) => {
  const id = request.goalId;
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/board/goal/share?goal-id=${id}`,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );

    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/goal/share`,
      {
        goalId: request.goalId,
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
    console.error('Error during request setup:', error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: 'Internal Server Error' }),
    };
  }
};

export const DELETE = async (request: DELETETypes) => {
  try {
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/board/goal/share`,
      {
        data: {
          goalId: request.goalId,
        },
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );

    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

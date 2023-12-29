import axios from 'axios';
import { cookies } from 'next/headers';

export const GET = async () => {
  const cookieStore = cookies();
  const token: string | undefined = cookieStore.get('accessToken')?.value;

  try {
    const response = await axios.get('http://localhost:8080/goal-list/all', {
      headers: {
        'Content-Type': 'application/json',
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

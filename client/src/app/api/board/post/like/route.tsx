import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

export const POST = async (postId: number) => {
  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/post/like`,
      {
        postId: postId,
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

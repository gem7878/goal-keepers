import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

interface GetTypes {
  postId: number;
  page: number;
}

export const GET = async (request: GetTypes) => {
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/board/all-comment?post-id=${request.postId}&page=${request.page}`,
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

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
      `http://localhost:8080/board/all-comment?post-id=${request.postId}&page=${request.page}`,
      {
        headers: {
          'Content-Type': 'application/json',
          // 'X-Content-Type-Options': 'nosniff',
          // 'X-XSS-Protection': 0,
          // 'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
          // Pragma: 'no-cache',
          // Expires: 0,
          // 'X-Frame-Options': 'DENY',
          // 'Transfer-Encoding': 'chunked',
          // Date: 'Sun, 24 Dec 2023 12:55:28 GMT',
          // 'Keep-Alive': 'timeout=60',
          // Connection: 'keep-alive',
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

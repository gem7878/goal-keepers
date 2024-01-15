import axios from 'axios';

import { cookies } from 'next/headers';
const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

interface GETTypes {
  pageNum: number;
}

export const GET = async (request: GETTypes) => {
  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_API_URL}/board/all?page=${request.pageNum}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
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

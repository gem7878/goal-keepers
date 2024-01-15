import axios from 'axios';
import { cookies } from 'next/headers';

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

interface POSTTypes {
  content: string;
  postId: number;
}
interface PUTTypes {
  content: string;
  commentId: number;
}
interface DELETETypes {
  commentId: number;
}

export const POST = async (request: POSTTypes) => {
  try {
    const id = request.postId;
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/board/comment?post-id=${id}`,
      {
        content: request.content,
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

export const PUT = async (request: PUTTypes) => {
  try {
    const id = request.commentId;
    const response = await axios.put(
      `${process.env.NEXT_PUBLIC_API_URL}/board/comment?comment-id=${id}`,
      {
        content: request.content,
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
    const id = request.commentId;
    const response = await axios.delete(
      `${process.env.NEXT_PUBLIC_API_URL}/board/comment?comment-id=${id}`,
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

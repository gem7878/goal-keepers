import axios from 'axios';

import { cookies } from 'next/headers';
interface GETTypes {
  postId: number;
}
interface POSTTypes {
  title: string;
  content: string;
  goalId: number;
}
interface PUTTypes {
  title: string;
  content: string;
  goalId: number;
  postId: number;
}
interface DELETETypes {
  postId: number;
}
const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;


export const GET = async (request: GETTypes) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/board/post?post-id=${request.postId}`,
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


export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      `http://localhost:8080/board/post`,
      {
        title: request.title,
        content: request.content,
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

export const PUT = async (request: PUTTypes) => {
  try {
    const response = await axios.put(
      `http://localhost:8080/board/post?post-id=${request.postId}`,
      {
        title: request.title,
        content: request.content,
        goalId: request.goalId,
      },
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
    const id = request.postId;
    const response = await axios.delete(
      `http://localhost:8080/board/post?post-id=${id}`,
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

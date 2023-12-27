import axios from 'axios';

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
      `http://localhost:8080/board/comment?post-id=${id}`,
      {
        content: request.content,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'X-Content-Type-Options': 'nosniff',
          'X-XSS-Protection': 0,
          'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
          Pragma: 'no-cache',
          Expires: 0,
          'X-Frame-Options': 'DENY',
          'Transfer-Encoding': 'chunked',
          Date: 'Sun, 24 Dec 2023 12:55:28 GMT',
          'Keep-Alive': 'timeout=60',
          Connection: 'keep-alive',
          Authorization:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjg4Nzk1MCwiaWF0IjoxNzAyODc3MTUwfQ.g0SkCtaEIAzynkxCPq_tBT233oG1eV-Oz-8pmi7bMqc',
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
      `http://localhost:8080/board/comment?comment-id=${id}`,
      {
        content: request.content,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'X-Content-Type-Options': 'nosniff',
          'X-XSS-Protection': 0,
          'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
          Pragma: 'no-cache',
          Expires: 0,
          'X-Frame-Options': 'DENY',
          'Transfer-Encoding': 'chunked',
          Date: 'Sun, 24 Dec 2023 12:55:28 GMT',
          'Keep-Alive': 'timeout=60',
          Connection: 'keep-alive',
          Authorization:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjg4Nzk1MCwiaWF0IjoxNzAyODc3MTUwfQ.g0SkCtaEIAzynkxCPq_tBT233oG1eV-Oz-8pmi7bMqc',
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
      `http://localhost:8080/board/comment?comment-id=${id}`,
      {
        headers: {
          'Content-Type': 'application/json',
          'X-Content-Type-Options': 'nosniff',
          'X-XSS-Protection': 0,
          'Cache-Control': 'no-cache, no-store, max-age=0, must-revalidate',
          Pragma: 'no-cache',
          Expires: 0,
          'X-Frame-Options': 'DENY',
          'Transfer-Encoding': 'chunked',
          Date: 'Sun, 24 Dec 2023 12:55:28 GMT',
          'Keep-Alive': 'timeout=60',
          Connection: 'keep-alive',
          Authorization:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjg4Nzk1MCwiaWF0IjoxNzAyODc3MTUwfQ.g0SkCtaEIAzynkxCPq_tBT233oG1eV-Oz-8pmi7bMqc',
        },
        withCredentials: true,
      },
    );

    const data = response.data;

    return new Response(JSON.stringify({ data }));
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

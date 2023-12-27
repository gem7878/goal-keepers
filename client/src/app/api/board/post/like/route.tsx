import axios from 'axios';

interface POSTTypes {
  postId: number;
}

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/board/post/like',
      {
        postId: request.postId,
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

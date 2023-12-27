import axios from 'axios';

export const GET = async (request: Request) => {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get('postId');
    const response = await axios.get(
      `http://localhost:8080/all-comment?post-id=${id}`,
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

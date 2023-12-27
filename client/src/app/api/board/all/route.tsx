import axios from 'axios';

export const GET = async (request: Request) => {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get('id');
    const response = await axios.get('http://localhost:8080/board/all', {
      headers: {
        Authorization:
          'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjg4Nzk1MCwiaWF0IjoxNzAyODc3MTUwfQ.g0SkCtaEIAzynkxCPq_tBT233oG1eV-Oz-8pmi7bMqc',
      },
    });

    const data = response.data;

    return new Response(JSON.stringify({ data }));
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

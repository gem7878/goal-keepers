import axios from 'axios';

export const GET = async (code: string) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/api/kakao/login?code=${code}`,
      {
        headers: { 'Content-Type': 'application/json' },
      },
    );
    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error) {
    console.log(error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

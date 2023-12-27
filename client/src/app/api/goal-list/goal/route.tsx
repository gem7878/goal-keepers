import axios from 'axios';
import { cookies } from 'next/headers';

interface POSTTypes {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  imageUrl: string;
}
interface PUTTypes {
  goalId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  startDate: string | undefined;
  endDate: string | undefined;
  imageUrl: any;
}

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

export const GET = async (request: Request) => {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get('id');
    const response = await axios.get(
      `http://localhost:8080/goal-list/goal?id=${id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
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

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/goal-list/goal',
      {
        title: request.title,
        description: request.description,
        startDate: request.startDate,
        endDate: request.endDate,
        imageUrl: request.imageUrl,
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );

    return { statusCode: 200, body: JSON.stringify(response.data.data) };
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
    // const { searchParams } = new URL(request.url);
    // const id = searchParams.get('id');
    const id = request.goalId;
    const response = await axios.put(
      `http://localhost:8080/goal-list/goal?id=${id}`,
      {
        title: request.title,
        description: request.description,
        startDate: request.startDate,
        endDate: request.endDate,
        imageUrl: request.imageUrl,
      },
      {
        headers: {
          'Content-Type': 'application/json',
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
export const DELETE = async (request: Request) => {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get('id');
    const response = await axios.delete(
      `http://localhost:8080/goal-list/goal?id=${id}`,
      {
        headers: {
          Authorization:
            'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjg4Nzk1MCwiaWF0IjoxNzAyODc3MTUwfQ.g0SkCtaEIAzynkxCPq_tBT233oG1eV-Oz-8pmi7bMqc',
        },
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

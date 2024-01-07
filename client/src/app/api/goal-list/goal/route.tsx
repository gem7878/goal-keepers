import axios from 'axios';
import { cookies } from 'next/headers';

interface GETTypes {
  goalId: number | undefined;
}
interface POSTTypes {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  imageUrl: File;
}
interface PUTTypes {
  goalId: number | undefined;
  title: string | undefined;
  description: string | undefined;
  startDate: string | undefined;
  endDate: string | undefined;
  imageUrl: any;
}
interface DELETETypes {
  goalId: number | undefined;
}

const cookieStore = cookies();
const token: string | undefined = cookieStore.get('accessToken')?.value;

export const GET = async (request: GETTypes) => {
  try {
    const response = await axios.get(
      `http://localhost:8080/goal-list/goal?id=${request.goalId}`,
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );

    return { statusCode: 200, body: JSON.stringify(response.data.data) };
  } catch (error) {
    console.log('error', error);
    return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
      status: 500,
    });
  }
};

export const POST = async (request: any) => {
  try {
    const response = await axios.post(
      'http://localhost:8080/goal-list/goal',
      request.formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
        withCredentials: true,
      },
    );

    return { statusCode: 200, body: JSON.stringify(response.data.data) };
  } catch (error: any) {
    console.error('Error during request setup:', error);
    // return {
    //   statusCode: 500,
    //   body: JSON.stringify({ error: 'Internal Server Error' }),
    // };
  }
};

export const PUT = async (request: any) => {
  try {
    const id = request.goalId;
    const response = await axios.put(
      `http://localhost:8080/goal-list/goal?id=${id}`,
      request.formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data',
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
    console.log(request.goalId);

    const response = await axios.delete(
      `http://localhost:8080/goal-list/goal?id=${request.goalId}`,
      {
        headers: {
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

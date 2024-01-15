import axios from "axios";

interface POSTTypes {
  nickname: string;
}

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      "http://localhost:8080/auth/nickname",
      {
        nickname: request.nickname,
      },
      {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      }
    );
    return { statusCode: 200, body: JSON.stringify(response.data) };
  } catch (error: any) {
    return {
        statusCode: error.response.status,
        body: JSON.stringify(error.response.data)
    }
  }
};

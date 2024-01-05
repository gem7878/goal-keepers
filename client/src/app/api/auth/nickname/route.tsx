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
    console.error("Error during request setup:", error.message);
    return {
      statusCode: 500,
      body: JSON.stringify({ error: "Internal Server Error" }),
    };
  }
};

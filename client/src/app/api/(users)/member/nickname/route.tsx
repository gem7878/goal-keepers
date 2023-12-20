import axios from "axios";

interface POSTTypes {
  email: string;
  password: string;
  nickname: string;
}

export const POST = async (request: POSTTypes) => {
  try {
    const response = await axios.post(
      "http://localhost:8080/member/nickname",
      {
        email: request.email,
        password: request.password,
        nickname: request.nickname,
      },
      {
        headers: {
          "Content-Type": "application/json",
          Authorization:
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjU1OTI2MiwiaWF0IjoxNzAyNTQ4NDYyfQ.XhEGH5blxSOfpAJymR0pCpTQ0JGyacTd4c4bfSQLu-A",
        },
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

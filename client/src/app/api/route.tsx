import axios from "axios";
import { error } from "console";

export const GET = async (request: Request) => {
  try {
    const { searchParams } = new URL(request.url);
    const id = searchParams.get("id");
    const response = await axios.get("http://localhost:8080/member/me", {
      headers: {
        Authorization:
          "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTcwMjU2MjgyMiwiaWF0IjoxNzAyNTUyMDIyfQ.3CxoWqXwGIOIM7MfzGedlE-NISMPAkeppUpC_OYxfds",
      },
    });

    const data = response.data;

    return new Response(JSON.stringify({ data }));
  } catch (error) {
    console.log("error", error);
    return new Response(JSON.stringify({ error: "Internal Server Error" }), {
      status: 500,
    });
  }
};

export const POST = async (email: string, password: string) => {
  console.log("연동됐니?");

  try {
    const response = await axios.post(
      "https://localhost:8080/auth/login",
      {
        email: email,
        password: password,
      },
      {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      }
    );
    // .then((response) => {
    //   console.log(response.data);
    // })
    // .catch((error) => {
    //   console.log("post error", error);
    // });

    const data = response.data;
    console.log(data);

    return { statusCode: 200, body: JSON.stringify(data) };
  } catch (error: any) {
    if (error.response) {
      console.error("Response data:", error.response.data);
      console.error("Response status:", error.response.status);
      console.error("Response headers:", error.response.headers);
    } else if (error.request) {
      console.error("No response received. Request:", error.request);
    } else {
      console.error("Error during request setup:", error.message);
    }

    return {
      statusCode: 500,
      body: JSON.stringify({ error: "Internal Server Error" }),
    };
  }
};

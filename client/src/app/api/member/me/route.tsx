import axios from "axios";

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

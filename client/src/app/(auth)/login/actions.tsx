"use server";

import { POST } from "@/app/api/(users)/auth/login/route";

export const handleLogin = async (postData: {
  email: string;
  password: string;
}) => {
  return POST(postData)
    .then((response) => {
      if (response.statusCode == 200) {
        return JSON.parse(response.body);
      }
    })
    .catch((error) => console.log(error));
};

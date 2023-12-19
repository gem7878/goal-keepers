"use server";

import { POST } from "@/app/api/(users)/auth/login/route";

export const handleSubmit = async (formData: any) => {
  const email = formData.get("email");
  const password = formData.get("password");

  const postData = {
    email: formData.get("email"),
    password: formData.get("password"),
  };
  return POST(postData)
    .then((response) => console.log("Response Success"))
    .catch((error) => console.log(error));
};

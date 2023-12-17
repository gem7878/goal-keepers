"use server";

import { POST } from "@/app/api/route";

export const handleSubmit = async (formData: any) => {
  const email = formData.get("email");
  const password = formData.get("password");
  console.log(formData.get("email"));
  console.log(formData.get("password"));
  POST(email, password);
};

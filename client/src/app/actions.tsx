"use server";

import { cookies } from "next/headers";

export const handleConfirmToken = async () => {
  const cookieStore = cookies();
  const hasCookie = cookieStore.has("accessToken");
  if (hasCookie) {
    return true;
  } else {
    return false;
  }
};
